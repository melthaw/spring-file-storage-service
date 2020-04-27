package in.clouthink.daas.fss.hwobs.impl;

import com.obs.services.ObsClient;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.hwobs.exception.HwObsStoreException;
import in.clouthink.daas.fss.hwobs.support.ObsProperties;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import in.clouthink.daas.fss.util.MetadataUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vanish
 */
public class FileStorageImpl implements FileStorage, InitializingBean, DisposableBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "hwobs";

    @Autowired
    private ObsProperties obsProperties;

    private ObsClient obsClient;

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean isMetadataSupported() {
        return true;
    }

    @Override
    public StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException {
        String bucket = resolveBucket(request);
        String ossObjectName = MetadataUtils.generateFilename(request, true);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(request.getContentType());
        Map<String, Object> metadata = (Map) MetadataUtils.buildMetadata(request);
        objectMetadata.setMetadata(metadata);

        PutObjectResult putObjectResult = obsClient.putObject(bucket, ossObjectName, inputStream, objectMetadata);

        if (putObjectResult == null) {
            throw new StoreFileException(String.format("Fail to upload %s", request.getOriginalFilename()));
        }

        if (putObjectResult.getStatusCode() != 200) {
            throw new StoreFileException(String.format("Fail to upload %s", request.getOriginalFilename()));
        }


        logger.debug(String.format("The uploading %s stored as oss-object[bucket=%s,object=%s]",
                request.getOriginalFilename(),
                bucket,
                ossObjectName));

        DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

        String url = new StringBuilder("https://").append(bucket)
                .append(".")
                .append(this.obsProperties.getEndpoint())
                .append("/")
                .append(ossObjectName)
                .toString();

        fileObject.getAttributes().put("oss-bucket", bucket);
        fileObject.getAttributes().put("oss-object", ossObjectName);
        fileObject.getAttributes().put("oss-url", url);

        String uploadedAt = (String) objectMetadata.getUserMetadata("fss-uploadedAt");
        fileObject.setUploadedAt(uploadedAt != null ? new Date(Long.parseLong(uploadedAt)) : null);

        fileObject.setFileUrl(url);
        fileObject.setStoredFilename(bucket + ":" + ossObjectName);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(new ObsObjectProxy(obsClient, bucket, ossObjectName));

        return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new HwObsStoreException(file.getName() + " not found.", e);
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
            throw new HwObsStoreException(String.format(
                    "Invalid filename %s , the format should be bucket_name:object_name",
                    filename));
        }

        String ossBucket = filename.split(":")[0];
        String ossObjectKey = filename.split(":")[1];

        ObsObject obsObject = obsClient.getObject(ossBucket, ossObjectKey);
        if (obsObject == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        buildStoreFileObject(obsObject, fileObject);

        String fileUrl = (String) obsObject.getMetadata().getUserMetadata("oss-url");

        fileObject.setFileUrl(fileUrl);
        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(new ObsObjectProxy(obsObject));

        return fileObject;
    }

    @Override
    public StoredFileObject findByStoredFilename(String filename, String downloadUrl) {
        logger.warn(String.format("Caution: The download url[%s] will be skipped", downloadUrl));
        return findByStoredFilename(filename);
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
            throw new HwObsStoreException(String.format(
                    "Invalid filename %s , the format should be bucket_name:object_name",
                    filename));
        }

        String ossBucket = filename.split(":")[0];
        String ossObjectKey = filename.split(":")[1];

        ObsObject obsObject = obsClient.getObject(ossBucket, ossObjectKey);
        if (obsObject == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        buildStoreFileObject(obsObject, fileObject);
        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(null);

        try {
            obsClient.deleteObject(ossBucket, ossObjectKey);
            logger.debug(String.format("The oss-object[bucket=%s,object=%s] is deleted.", ossBucket, ossObjectKey));
        } catch (Throwable e) {
            logger.error(String.format("Fail to delete the oss-object[bucket=%s,object=%s]", ossBucket, ossObjectKey),
                    e);
        }

        return fileObject;
    }

    private void buildStoreFileObject(ObsObject obsObject, DefaultStoredFileObject fileObject) {
        Map<String, String> userMetadata = (Map) obsObject.getMetadata().getMetadata();

        if (userMetadata == null) {
            return;
        }
        try {
            fileObject.setOriginalFilename(userMetadata.get("fss-originalfilename"));
            fileObject.setPrettyFilename(userMetadata.get("fss-prettyfilename"));
            fileObject.setContentType(userMetadata.get("fss-contenttype"));
            fileObject.setUploadedBy(userMetadata.get("fss-uploadedby"));

            String uploadedAt = userMetadata.get("fss-uploadedat");
            fileObject.setUploadedAt(uploadedAt != null ? new Date(Long.parseLong(uploadedAt)) : null);

            String size = userMetadata.get("fss-size");
            fileObject.setSize(size != null ? Long.parseLong(size) : -1);
        } catch (Throwable e) {
            logger.error(e, e);
        }

        try {
            Map<String, String> attributes = new HashMap<>();
            userMetadata.keySet().stream().filter(key -> key.startsWith("fss-attrs-")).forEach(key -> {
                String attributeName = key.substring("fss-attrs-".length());
                attributes.put(attributeName, userMetadata.get(key));
            });
            fileObject.setAttributes(attributes);
        } catch (Throwable e) {
            logger.error(e, e);
        }
    }

    private String resolveBucket(StoreFileRequest request) {
        String category = request.getAttributes().get("category");
        String bucket = obsProperties.getBuckets().get(category);
        if (StringUtils.isEmpty(bucket)) {
            bucket = obsProperties.getDefaultBucket();
        }
        return bucket;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.obsProperties);
        if (!StringUtils.isEmpty(this.obsProperties.getSecurityToken())) {
            this.obsClient = new ObsClient(
                    this.obsProperties.getKeyId(),
                    this.obsProperties.getKeySecret(),
                    this.obsProperties.getSecurityToken(),
                    "http://" + this.obsProperties.getEndpoint());
        } else {
            this.obsClient = new ObsClient(
                    this.obsProperties.getKeyId(),
                    this.obsProperties.getKeySecret(),
                    "http://" + this.obsProperties.getEndpoint());
        }


    }

    @Override
    public void destroy() {
        if (this.obsClient != null) {
            try {
                this.obsClient.close();
            } catch (IOException e) {
            }
        }
    }
}
