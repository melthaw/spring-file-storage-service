package in.clouthink.daas.fss.s3.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.s3.exception.S3StoreException;
import in.clouthink.daas.fss.s3.support.S3Properties;
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
        String s3Bucket = resolveBucket(request);
        String s3ObjectKey = MetadataUtils.generateFilename(request);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(request.getContentType());
        objectMetadata.setUserMetadata(MetadataUtils.buildMetadata(request));

        PutObjectRequest putObjectRequest = new PutObjectRequest(s3Bucket, s3ObjectKey, inputStream, objectMetadata);
        putObjectRequest.getRequestClientOptions().setReadLimit(50000000);
        PutObjectResult putObjectResult = s3Client.putObject(putObjectRequest);

        logger.debug(String.format("%s is stored", s3ObjectKey));

        S3Object s3Object = s3Client.getObject(s3Bucket, s3ObjectKey);

        DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

        fileObject.getAttributes().put("s3-bucket", s3Bucket);
        fileObject.getAttributes().put("s3-key", s3ObjectKey);

        String uploadedAt = objectMetadata.getUserMetadata().get("fss-uploadedAt");
        fileObject.setUploadedAt(uploadedAt != null ? new Date(Long.parseLong(uploadedAt)) : null);

        fileObject.setStoredFilename(s3Bucket + ":" + s3ObjectKey);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(new S3ObjectProxy(s3Object));

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
        if (StringUtils.isEmpty(filename)) {
            return null;
        }

        if (filename.indexOf("?") > 0) {
            filename = filename.substring(0, filename.indexOf("?"));
        }

        if (filename.indexOf(":") <= 0) {
            throw new S3StoreException(String.format("Invalid filename %s , the format should be bucket_name:object_key",
                                                     filename));
        }

        String s3Bucket = filename.split(":")[0];
        String s3ObjectKey = filename.split(":")[1];

        S3Object s3Object = s3Client.getObject(s3Bucket, s3ObjectKey);
        if (s3Object == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        buildStoreFileObject(s3Object, fileObject);

        String fileUrl = s3Object.getObjectMetadata().getUserMetadata().get("s3-url");

        fileObject.setFileUrl(fileUrl);
        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(new S3ObjectProxy(s3Object));

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
            throw new S3StoreException(String.format("Invalid filename %s , the format should be bucket_name:object_key",
                                                     filename));
        }

        String s3Bucket = filename.split(":")[0];
        String s3ObjectKey = filename.split(":")[1];

        S3Object s3Object = s3Client.getObject(s3Bucket, s3ObjectKey);
        if (s3Object == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        buildStoreFileObject(s3Object, fileObject);
        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(null);

        try {
            s3Client.deleteObject(s3Bucket, s3ObjectKey);
            logger.info(String.format("The s3-object[bucket=%s,key=%s] is deleted.", s3Bucket, s3ObjectKey));
        } catch (Throwable e) {
            logger.error(String.format("Delete the s3-object[bucket=%s,key=%s] failed.", s3Bucket, s3ObjectKey), e);
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
        String category = request.getAttributes().get("category");
        String bucket = s3Properties.getBuckets().get(category);
        if (StringUtils.isEmpty(bucket)) {
            bucket = s3Properties.getDefaultBucket();
        }
        return bucket;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.s3Properties);

        AWSCredentials credentials = new BasicAWSCredentials(this.s3Properties.getAccessKey(),
                                                             this.s3Properties.getSecretKey());
        AmazonS3ClientBuilder builder = AmazonS3Client.builder()
                                                      .withCredentials((new AWSStaticCredentialsProvider(credentials)))
//                                                      .withClientConfiguration(new ClientConfiguration().withValidateAfterInactivityMillis(
//                                                              100)
//                                                                                                        .withRequestTimeout(
//                                                                                                                5000)
//                                                                                                        .withConnectionTimeout(
//                                                                                                                5000)
//                                                                                                        .withTcpKeepAlive(
//                                                                                                                true))
                                                      .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                                                              this.s3Properties.getEndpoint(),
                                                              this.s3Properties.getRegion()));
        if ("path".equalsIgnoreCase(this.s3Properties.getBucketStyle())) {
            builder.withPathStyleAccessEnabled(true);
        }
        s3Client = builder.build();
    }

}
