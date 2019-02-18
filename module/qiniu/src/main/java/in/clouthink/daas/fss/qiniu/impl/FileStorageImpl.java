package in.clouthink.daas.fss.qiniu.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.qiniu.exception.QiniuStoreException;
import in.clouthink.daas.fss.qiniu.support.QiniuProperties;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import in.clouthink.daas.fss.util.MetadataUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Date;
import java.util.Map;

/**
 * @author dz
 * @see <a href="https://developer.qiniu.com/kodo/sdk/1239/java">offical sdk</a>
 */
public class FileStorageImpl implements FileStorage, InitializingBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "qiniu";

    @Autowired
    private QiniuProperties qiniuProperties;

    private Auth auth;

    private UploadManager uploadManager;

    private BucketManager bucketManager;

    public QiniuProperties getQiniuProperties() {
        return qiniuProperties;
    }

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean isMetadataSupported() {
        return false;
    }

    @Override
    public boolean isImageSupported() {
        return true;
    }

    @Override
    public StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException {
        String qiniuKey = MetadataUtils.generateFilename(request);
        String qiniuBucket = this.resolveBucket(request);

        try {
            //get up token
            String upToken = this.auth.uploadToken(qiniuBucket);

            Response res = uploadManager.put(inputStream, qiniuKey, upToken, null, request.getContentType());

            if (!res.isOK()) {
                throw new QiniuStoreException(res.toString());
            }

            DefaultPutRet uploadResult = new Gson().fromJson(res.bodyString(), DefaultPutRet.class);

            logger.debug(String.format("%s is uploaded and stored as %s",
                                       request.getOriginalFilename(),
                                       uploadResult.key));

            long size = request.getSize();
            if (request.getSize() <= 0) {
                try {
                    FileInfo fileInfo = bucketManager.stat(qiniuBucket, qiniuKey);
                    if (fileInfo != null) {
                        size = fileInfo.fsize;
                    }
                } catch (Throwable e) {
                }
            }

            DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

            String url = new StringBuilder("https://").append(this.qiniuProperties.getHost())
                                                      .append("/")
                                                      .append(uploadResult.key)
                                                      .toString();

            fileObject.getAttributes().put("qiniu-bucket", qiniuBucket);
            fileObject.getAttributes().put("qiniu-key", uploadResult.key);
            fileObject.getAttributes().put("qiniu-url", url);

            fileObject.setSize(size);
            fileObject.setUploadedAt(new Date());
            fileObject.setFileUrl(url);
            fileObject.setStoredFilename(qiniuBucket + ":" + uploadResult.key);
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(new QiniuFile(url, auth));

            return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
        } catch (QiniuStoreException e) {
            throw e;
        } catch (QiniuException e) {
            Response r = e.response;

            String message;
            try {
                message = r.bodyString();
            } catch (Exception ex) {
                message = ex.toString();
            }

            throw new QiniuStoreException(message);
        } catch (Throwable e) {
            throw new QiniuStoreException(String.format("Fail to upload file %s", request.getOriginalFilename()), e);
        }
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new QiniuStoreException(file.getName() + " not found.", e);
        }
    }

    @Override
    public StoreFileResponse store(byte[] bytes, StoreFileRequest request) throws StoreFileException {
        return store(new ByteArrayInputStream(bytes), request);
    }

    @Override
    public StoredFileObject findByStoredFilename(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return null;
        }

        if (filename.indexOf("?") > 0) {
            filename = filename.substring(0, filename.indexOf("?"));
        }

        if (filename.indexOf(":") <= 0) {
            throw new QiniuStoreException(String.format("Invalid filename %s , the format should be bucket:key",
                                                        filename));
        }

        String qiniuBucket = filename.split(":")[0];
        String qiniuKey = filename.split(":")[1];

        try {
            FileInfo fileInfo = bucketManager.stat(qiniuBucket, qiniuKey);
            if (fileInfo == null) {
                return null;
            }

            DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

            String url = new StringBuilder("http://").append(this.qiniuProperties.getHost())
                                                     .append("/")
                                                     .append(qiniuKey)
                                                     .toString();
            fileObject.setFileUrl(url);

            fileObject.setSize(fileInfo.fsize);
            fileObject.setContentType(fileInfo.mimeType);
            fileObject.setUploadedAt(new Date(fileInfo.putTime));
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(new QiniuFile(url, auth));

            return fileObject;
        } catch (QiniuException e) {
            Response r = e.response;

            String message;
            try {
                message = r.bodyString();
            } catch (Exception ex) {
                message = ex.toString();
            }

            throw new QiniuStoreException(message);
        } catch (Throwable e) {
            logger.error(String.format("Fail to get the file[bucket=%s,key=%s]", qiniuBucket, qiniuKey), e);
        }

        return null;
    }

    @Override
    public StoredFileObject delete(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return null;
        }

        if (filename.indexOf("?") > 0) {
            filename = filename.substring(0, filename.indexOf("?"));
        }

        if (filename.indexOf(":") <= 0) {
            throw new QiniuStoreException(String.format("Invalid filename %s , the format should be bucket:key",
                                                        filename));
        }

        String qiniuBucket = filename.split(":")[0];
        String qiniuKey = filename.split(":")[1];

        try {
            FileInfo fileInfo = bucketManager.stat(qiniuBucket, qiniuKey);
            if (fileInfo == null) {
                return null;
            }

            DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

            fileObject.getAttributes().put("qiniu-bucket", qiniuBucket);
            fileObject.getAttributes().put("qiniu-key", qiniuKey);

            fileObject.setSize(fileInfo.fsize);
            fileObject.setContentType(fileInfo.mimeType);
            fileObject.setUploadedAt(new Date(fileInfo.putTime));
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(null);

            bucketManager.delete(qiniuBucket, qiniuKey);
            logger.info(String.format("The qiniu file[bucket=%s,key=%s] is deleted.", qiniuBucket, qiniuKey));

            return fileObject;
        } catch (QiniuException e) {
            Response r = e.response;

            String message;
            try {
                message = r.bodyString();
            } catch (Exception ex) {
                message = ex.toString();
            }

            throw new QiniuStoreException(message);
        } catch (Throwable e) {
            logger.error(String.format("Fail to delete the file[bucket=%s,key=%s]", qiniuBucket, qiniuKey), e);
        }

        return null;
    }

    private String resolveBucket(StoreFileRequest request) {
        String category = request.getAttributes().get("category");
        String bucket = qiniuProperties.getBuckets().get(category);
        if (StringUtils.isEmpty(bucket)) {
            bucket = qiniuProperties.getDefaultBucket();
        }
        return bucket;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(qiniuProperties);

        this.auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        uploadManager = new UploadManager(c);
        bucketManager = new BucketManager(this.auth, c);
    }

}
