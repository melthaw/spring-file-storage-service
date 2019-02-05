package in.clouthink.daas.fss.alioss.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import in.clouthink.daas.fss.alioss.exception.AliossStoreException;
import in.clouthink.daas.fss.alioss.support.OssProperties;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author LiangBin & dz
 */
public class FileStorageImpl implements FileStorage, InitializingBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "alioss";

    @Autowired
    private OssProperties ossProperties;

    @Autowired
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
        String ossKey = MetadataUtils.generateKey(request);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(request.getContentType());
        objectMetadata.setUserMetadata(MetadataUtils.buildMetadata(request));

        PutObjectResult putObjectResult = ossClient
                .putObject(ossBucket, ossKey, inputStream, objectMetadata);

        logger.debug(String.format("%s is stored", ossKey));

        OSSObject ossObject = ossClient.getObject(ossBucket, ossKey);

        DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

        fileObject.getAttributes().put("oss-bucket", ossBucket);
        fileObject.getAttributes().put("oss-key", ossKey);

        String uploadedAt = objectMetadata.getUserMetadata().get("fss-uploadedAt");
        fileObject.setUploadedAt(uploadedAt != null ? new Date(Long.parseLong(uploadedAt)) : null);

        fileObject.setStoredFilename(ossKey);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(ossObject);

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
        String ossBucket = ossProperties.getDefaultBucket();
        String ossKey = filename;
        int posOfSplitter = filename.indexOf(":");
        if (posOfSplitter > 1) {
            ossBucket = filename.substring(0, posOfSplitter);
            ossKey = filename.substring(posOfSplitter);
        }

        OSSObject ossObject = ossClient.getObject(ossBucket, ossKey);
        if (ossObject == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();
        buildStoreFileObject(ossObject, fileObject);

        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(ossObject);

        return fileObject;
    }

    @Override
    public StoredFileObject delete(String filename) {
        String ossBucket = ossProperties.getDefaultBucket();
        String ossKey = filename;
        int posOfSplitter = filename.indexOf(":");
        if (posOfSplitter > 1) {
            ossBucket = filename.substring(0, posOfSplitter);
            ossKey = filename.substring(posOfSplitter);
        }

        OSSObject ossObject = ossClient.getObject(ossBucket, ossKey);
        if (ossObject == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();
        buildStoreFileObject(ossObject, fileObject);

        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(null);

        try {
            ossClient.deleteObject(ossBucket, ossKey);
            logger.info(String.format("The oss file object[bucket=%s,key=%s] is deleted.", ossBucket, ossKey));
        } catch (Throwable e) {
            logger.error(String.format("Delete the object[bucket=%s,key=%s] failed.", ossBucket, ossKey), e);
        }

        return fileObject;
    }

    private void buildStoreFileObject(OSSObject ossObject, DefaultStoredFileObject fileObject) {
        Map<String, String> userMetadata = ossObject.getObjectMetadata().getUserMetadata();

        if (userMetadata == null) {
            return;
        }

        try {
            fileObject.setStoredFilename(ossObject.getKey());
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
        String bucket = ossProperties.getBuckets().get(category);
        if (StringUtils.isEmpty(bucket)) {
            bucket = ossProperties.getDefaultBucket();
        }
        return bucket;
    }


    private Map<String, Object> buildExtraAttributes(String ossBucket, String ossKey) {
        StringBuilder imageUrl = new StringBuilder("http://");
        StringBuilder fileUrl = new StringBuilder("http://");

        imageUrl.append(ossBucket)
                .append(".")
                .append(ossProperties.getImgDomain())
                .append("/")
                .append(ossKey);

        fileUrl.append(ossBucket)
               .append(".")
               .append(ossProperties.getOssDomain())
               .append("/")
               .append(ossKey);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("imageUrl", imageUrl.toString());
        result.put("fileUrl", fileUrl.toString());
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(ossProperties);
        Assert.notNull(ossClient);
    }

}
