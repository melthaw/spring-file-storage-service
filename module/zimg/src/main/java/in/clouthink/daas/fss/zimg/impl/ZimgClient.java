package in.clouthink.daas.fss.zimg.impl;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.ArrayMap;
import in.clouthink.daas.fss.zimg.client.ZimgError;
import in.clouthink.daas.fss.zimg.client.ZimgInfo;
import in.clouthink.daas.fss.zimg.client.ZimgResult;
import in.clouthink.daas.fss.zimg.exception.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.math.BigDecimal;

/**
 * @author dz
 * @since 3
 */
public class ZimgClient {

    private static final Log logger = LogFactory.getLog(ZimgClient.class);

    /**
     * Zimg Raw Post allowed Content-Type
     */
    private static final String[] ZIMG_RAW_POST_ALLOWED_TYPE = new String[]{"jpeg", "jpg", "png", "gif", "webp"};

    private HttpClient httpClient = new HttpClient();

    public ZimgResult upload(InputStream inputStream, String contentType, long size, String uploadEndpoint) {
        checkContentType(contentType);
        try {
            GenericJson genericJson = httpClient.upload(uploadEndpoint, contentType, size, inputStream);

            return buildZimgResult(genericJson);
        } catch (IOException e) {
            throw new ZimgStoreException(e);
        }
    }

    public ZimgResult upload(File file, String contentType, String uploadEndpoint) {
        checkContentType(contentType);

        if (!file.exists()) {
            throw new ZimgStoreException(String.format("The file %s is not existed", file.getPath()));
        }

        try {
            GenericJson genericJson = httpClient.upload(uploadEndpoint,
                                                        contentType,
                                                        file.length(),
                                                        new FileInputStream(file));

            return buildZimgResult(genericJson);

        } catch (IOException e) {
            throw new ZimgStoreException(e);
        }
    }

    public void delete(String md5, String adminEndpoint) {
        try {
            httpClient.delete(String.format("%s?md5=%s&t=1", adminEndpoint, md5));
        } catch (ZimgHttpException e) {
            throw new ZimgDeleteException(e);
        } catch (IOException e) {
            throw new ZimgDeleteException(e);
        }
    }

    public boolean exists(String md5, String infoEndpoint) {
        try {
            GenericJson genericJson = httpClient.info(String.format("%s?md5=%s", infoEndpoint, md5));
            return (Boolean) genericJson.get("ret");
        } catch (Throwable e) {
            logger.error(e, e);
            return false;
        }
    }

    public void download(String filename, String downloadEndpoint, OutputStream outputStream) {
        try {
            httpClient.download(String.format("%s/%s", downloadEndpoint, filename), outputStream);
        } catch (ZimgHttpException e) {
            throw new ZimgDownloadException(e);
        } catch (IOException e) {
            throw new ZimgDeleteException(e);
        }
    }

    private ZimgResult buildZimgResult(GenericJson genericJson) {
        ZimgResult result = new ZimgResult();
        result.setRet((Boolean) genericJson.get("ret"));
        ArrayMap<String, Object> info = (ArrayMap) genericJson.get("info");
        if (info != null) {
            String md5 = (String) info.get("md5");
            BigDecimal size = (BigDecimal) info.get("size");
            result.setInfo(new ZimgInfo(md5, size.toBigInteger().intValue()));
        }

        ArrayMap<String, Object> error = (ArrayMap) genericJson.get("error");
        if (error != null) {
            BigDecimal code = (BigDecimal) error.get("code");
            String message = (String) error.get("message");
            result.setError(new ZimgError(code.toBigInteger().intValue(), message));
        }
        return result;
    }

    private void checkContentType(String contentType) {
        for (String item : ZIMG_RAW_POST_ALLOWED_TYPE) {
            if (item.equals(contentType)) {
                return;
            }
        }
        throw new UnsupportedContentTypeException(contentType);
    }

}
