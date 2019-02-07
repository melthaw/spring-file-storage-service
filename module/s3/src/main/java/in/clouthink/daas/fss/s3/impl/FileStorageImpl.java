package in.clouthink.daas.fss.s3.impl;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import in.clouthink.daas.fss.s3.exception.S3StoreException;
import in.clouthink.daas.fss.s3.support.S3Properties;
import in.clouthink.daas.fss.core.*;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
public class FileStorageImpl implements FileStorage, InitializingBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "s3";

    @Autowired
    private S3Properties s3Properties;

    private AmazonS3 s3Client;

    public S3Properties getS3Properties() {
        return s3Properties;
    }

    public AmazonS3 getS3Client() {
        return s3Client;
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
        String ossBucket = resolveBucket(request);
        String ossKey = MetadataUtils.generateKey(request);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(request.getContentType());
        objectMetadata.setUserMetadata(MetadataUtils.buildMetadata(request));

        PutObjectResult putObjectResult = s3Client
                .putObject(ossBucket, ossKey, inputStream, objectMetadata);

        logger.debug(String.format("%s is stored", ossKey));

        S3Object s3Object = s3Client.getObject(ossBucket, ossKey);

        DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

        fileObject.getAttributes().put("s3-bucket", ossBucket);
        fileObject.getAttributes().put("s3-key", ossKey);

        String uploadedAt = objectMetadata.getUserMetadata().get("fss-uploadedAt");
        fileObject.setUploadedAt(uploadedAt != null ? new Date(Long.parseLong(uploadedAt)) : null);

        fileObject.setStoredFilename(ossKey);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(s3Object);

        return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new S3StoreException(file.getName() + " not found.", e);
        }
    }

    @Override
    public StoreFileResponse store(byte[] bytes, StoreFileRequest request) throws StoreFileException {
        return store(new ByteArrayInputStream(bytes), request);
    }

    @Override
    public StoredFileObject findByStoredFilename(String filename) {
        String ossBucket = s3Properties.getDefaultBucket();
        String ossKey = filename;
        int posOfSplitter = filename.indexOf(":");
        if (posOfSplitter > 1) {
            ossBucket = filename.substring(0, posOfSplitter);
            ossKey = filename.substring(posOfSplitter);
        }

        S3Object s3Object = s3Client.getObject(ossBucket, ossKey);
        if (s3Object == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        buildStoreFileObject(s3Object, fileObject);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(s3Object);

        return fileObject;
    }

    @Override
    public StoredFileObject delete(String filename) {
        String ossBucket = s3Properties.getDefaultBucket();
        String ossKey = filename;
        int posOfSplitter = filename.indexOf(":");
        if (posOfSplitter > 1) {
            ossBucket = filename.substring(0, posOfSplitter);
            ossKey = filename.substring(posOfSplitter);
        }

        S3Object s3Object = s3Client.getObject(ossBucket, ossKey);
        if (s3Object == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        buildStoreFileObject(s3Object, fileObject);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(null);

        try {
            s3Client.deleteObject(ossBucket, ossKey);
            logger.info(String.format("The oss file object[bucket=%s,key=%s] is deleted.", ossBucket, ossKey));
        } catch (Throwable e) {
            logger.error(String.format("Delete the object[bucket=%s,key=%s] failed.", ossBucket, ossKey), e);
        }

        return fileObject;
    }

    private void buildStoreFileObject(S3Object s3Object, DefaultStoredFileObject fileObject) {
        Map<String, String> userMetadata = s3Object.getObjectMetadata().getUserMetadata();

        if (userMetadata == null) {
            return;
        }

        try {
            fileObject.setStoredFilename(s3Object.getKey());
            fileObject.setOriginalFilename(userMetadata.get("fss-originalFilename"));
            fileObject.setPrettyFilename(userMetadata.get("fss-prettyFilename"));
            fileObject.setContentType(userMetadata.get("fss-contentType"));
            fileObject.setUploadedBy(userMetadata.get("fss-uploadedBy"));

            String uploadedAt = userMetadata.get("fss-uploadedAt");
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
        String category = (String) request.getAttributes().get("category");
        String bucket = s3Properties.getBuckets().get(category);
        if (StringUtils.isEmpty(bucket)) {
            bucket = s3Properties.getDefaultBucket();
        }
        return bucket;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(s3Properties);

        AWSCredentials credentials = new BasicAWSCredentials(s3Properties.getAccessKey(),
                                                             s3Properties.getSecretKey());

        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.valueOf(s3Properties.getProtocol()));

        s3Client = new AmazonS3Client(credentials, clientConfig);
        s3Client.setEndpoint(s3Properties.getEndpoint());
    }

}
