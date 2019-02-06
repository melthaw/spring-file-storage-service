package in.clouthink.daas.fss.qiniu.impl;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
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
import java.util.UUID;

/**
 * TODO: metadata
 *
 * @author dz
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
        return true;
    }

    @Override
    public boolean isImageSupported() {
        return true;
    }

    @Override
    public StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException {
        try {
            String qiniuBucket = this.resolveBucket(request);
            String qiniuKey = UUID.randomUUID().toString().replace("-", "");

            Map<String, String> metadata = MetadataUtils.buildMetadata(request);

            StringMap params = new StringMap();
            metadata.entrySet().stream().forEach(entry -> params.put(entry.getKey(), entry.getValue()));

            String upToken = this.auth.uploadToken(qiniuBucket);

            Response res = uploadManager.put(inputStream, qiniuKey, upToken, params, request.getContentType());
            String storedFilename = this.processUploadResponse(res);

            logger.debug(String.format("%s is stored", storedFilename));

            DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

            fileObject.getAttributes().put("qiniu-bucket", qiniuBucket);
            fileObject.getAttributes().put("qiniu-filename", storedFilename);
            String uploadedAt = metadata.get("fss-uploadedAt");
            fileObject.setUploadedAt(uploadedAt != null ? new Date(Long.parseLong(uploadedAt)) : null);
            fileObject.setStoredFilename(storedFilename);
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(new QiniuFile(this.getFullPath(qiniuKey), auth));

            return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
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
        String qiniuBucket = qiniuProperties.getDefaultBucket();
        String qiniuKey = filename;
        int posOfSplitter = filename.indexOf(":");
        if (posOfSplitter > 1) {
            qiniuBucket = filename.substring(0, posOfSplitter);
            qiniuKey = filename.substring(posOfSplitter);
        }

        try {

            FileInfo fileInfo = bucketManager.stat(qiniuBucket, qiniuKey);
            if (fileInfo == null) {
                return null;
            }

            DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

            //TODO resolve metadata
            fileObject.getAttributes().put("qiniu-bucket", qiniuBucket);
            fileObject.getAttributes().put("qiniu-key", qiniuKey);
            fileObject.setUploadedAt(new Date(fileInfo.putTime));
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(new QiniuFile(this.getFullPath(qiniuKey), auth));

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
            logger.error(String.format("Delete the object[bucket=%s,key=%s] failed.", qiniuBucket, qiniuKey), e);
        }

        return null;
    }

    @Override
    public StoredFileObject delete(String filename) {
        String qiniuBucket = qiniuProperties.getDefaultBucket();
        String qiniuKey = filename;
        int posOfSplitter = filename.indexOf(":");
        if (posOfSplitter > 1) {
            qiniuBucket = filename.substring(0, posOfSplitter);
            qiniuKey = filename.substring(posOfSplitter);
        }

        try {

            FileInfo fileInfo = bucketManager.stat(qiniuBucket, qiniuKey);
            if (fileInfo == null) {
                return null;
            }

            //TODO resolve metadata
            DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

            fileObject.getAttributes().put("qiniu-bucket", qiniuBucket);
            fileObject.getAttributes().put("qiniu-key", qiniuKey);
            fileObject.setUploadedAt(new Date(fileInfo.putTime));
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(null);

            bucketManager.delete(qiniuBucket, qiniuKey);
            logger.info(String.format("The qiniu file object[bucket=%s,key=%s] is deleted.", qiniuBucket, qiniuKey));

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
            logger.error(String.format("Delete the object[bucket=%s,key=%s] failed.", qiniuBucket, qiniuKey), e);
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

    protected String getFullPath(String file) {
        return !file.startsWith("http://") && !file.startsWith("https://") ?
                this.qiniuProperties.getEndpoint() + file : file;
    }

    private String processUploadResponse(Response res) throws QiniuException {
        if (res.isOK()) {
            UploadResult ret = res.jsonToObject(UploadResult.class);
            return this.getFullPath(ret.getKey());
        }
        else {
            throw new QiniuStoreException(res.toString());
        }
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
