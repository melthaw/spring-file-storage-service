package in.clouthink.daas.fss.alioss.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import in.clouthink.daas.fss.alioss.exception.AliossStoreException;
import in.clouthink.daas.fss.alioss.support.OssProperties;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import in.clouthink.daas.fss.util.IOUtils;
import in.clouthink.daas.fss.util.MetadataUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
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
 * @author dz
 */
public class FileStorageImpl implements FileStorage, InitializingBean, DisposableBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "alioss";

    @Autowired
    private OssProperties ossProperties;

    private OSSClient ossClient;

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
        String ossBucket = resolveBucket(request);
        String ossObjectName = MetadataUtils.generateFilename(request, true);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(request.getContentType());
        objectMetadata.setUserMetadata(MetadataUtils.buildMetadata(request));

        PutObjectResult putObjectResult = ossClient.putObject(ossBucket, ossObjectName, inputStream, objectMetadata);

        if (putObjectResult == null) {
            throw new StoreFileException(String.format("Fail to upload %s", request.getOriginalFilename()));
        }

        if (putObjectResult.getResponse() != null) {
            if (putObjectResult.getResponse().getStatusCode() != 200) {
                throw new StoreFileException(String.format("Fail to upload %s", request.getOriginalFilename()));
            }

            try {
                logger.debug(String.format("Response for uploading %s : %s",
                                           request.getOriginalFilename(),
                                           IOUtils.readAsString(putObjectResult.getResponse().getContent())));
            } catch (Throwable e) {
                logger.error("Fail to parse uploading response content", e);
                IOUtils.close(putObjectResult.getResponse().getContent());
            }
        }

        logger.debug(String.format("The uploading %s stored as oss-object[bucket=%s,object=%s]",
                                   request.getOriginalFilename(),
                                   ossBucket,
                                   ossObjectName));

        DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

        String url = new StringBuilder("https://").append(ossBucket)
                                                  .append(".")
                                                  .append(this.ossProperties.getEndpoint())
                                                  .append("/")
                                                  .append(ossObjectName)
                                                  .toString();

        fileObject.getAttributes().put("oss-bucket", ossBucket);
        fileObject.getAttributes().put("oss-object", ossObjectName);
        fileObject.getAttributes().put("oss-url", url);

        String uploadedAt = objectMetadata.getUserMetadata().get("fss-uploadedAt");
        fileObject.setUploadedAt(uploadedAt != null ? new Date(Long.parseLong(uploadedAt)) : null);

        fileObject.setFileUrl(url);
        fileObject.setStoredFilename(ossBucket + ":" + ossObjectName);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(new OssObjectProxy(ossClient, ossBucket, ossObjectName));

        return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new AliossStoreException(file.getName() + " not found.", e);
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
            throw new AliossStoreException(String.format(
                    "Invalid filename %s , the format should be bucket_name:object_name",
                    filename));
        }

        String ossBucket = filename.split(":")[0];
        String ossObjectKey = filename.split(":")[1];

        OSSObject ossObject = ossClient.getObject(ossBucket, ossObjectKey);
        if (ossObject == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        buildStoreFileObject(ossObject, fileObject);

        String fileUrl = ossObject.getObjectMetadata().getUserMetadata().get("oss-url");

        fileObject.setFileUrl(fileUrl);
        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(new OssObjectProxy(ossObject));

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
            throw new AliossStoreException(String.format(
                    "Invalid filename %s , the format should be bucket_name:object_name",
                    filename));
        }

        String ossBucket = filename.split(":")[0];
        String ossObjectKey = filename.split(":")[1];

        OSSObject ossObject = ossClient.getObject(ossBucket, ossObjectKey);
        if (ossObject == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        buildStoreFileObject(ossObject, fileObject);
        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(null);

        try {
            ossClient.deleteObject(ossBucket, ossObjectKey);
            logger.debug(String.format("The oss-object[bucket=%s,object=%s] is deleted.", ossBucket, ossObjectKey));
        } catch (Throwable e) {
            logger.error(String.format("Fail to delete the oss-object[bucket=%s,object=%s]", ossBucket, ossObjectKey),
                         e);
        }

        return fileObject;
    }

    private void buildStoreFileObject(OSSObject ossObject, DefaultStoredFileObject fileObject) {
        Map<String, String> userMetadata = ossObject.getObjectMetadata().getUserMetadata();

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
        String bucket = ossProperties.getBuckets().get(category);
        if (StringUtils.isEmpty(bucket)) {
            bucket = ossProperties.getDefaultBucket();
        }
        return bucket;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.ossProperties);
        this.ossClient = new OSSClient("http://" + this.ossProperties.getEndpoint(),
                                       this.ossProperties.getKeyId(),
                                       this.ossProperties.getKeySecret());

        if (this.ossProperties.getClientConfiguration() != null) {
            BeanUtils.copyProperties(this.ossProperties.getClientConfiguration(),
                                     this.ossClient.getClientConfiguration());
        }
    }

    @Override
    public void destroy() {
        if (this.ossClient != null) {
            this.ossClient.shutdown();
        }
    }
}
