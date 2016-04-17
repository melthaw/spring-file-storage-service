package in.clouthink.daas.fss.alioss.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.oss.model.ObjectMetadata;

import in.clouthink.daas.fss.alioss.model.OssFileObject;
import in.clouthink.daas.fss.alioss.support.OssConfig;
import in.clouthink.daas.fss.alioss.support.OssStrategy;
import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileStorageRequest;
import in.clouthink.daas.fss.repackage.org.apache.commons.io.FilenameUtils;

/**
 * Created by LiangBin on 16/4/16.
 */
public class DefaultOssStrategy implements OssStrategy {
    
    private static final String METADATA_KEY_PREFIX = "daas-fss-";
    
    @Autowired
    private OssConfig ossConfig;
    
    @Override
    public String generateKey(FileStorageRequest request) {
        String prefix = null;
        if (request instanceof OssFileObject) {
            prefix = ((OssFileObject) request).getId();
        }
        if (StringUtils.isEmpty(prefix)) {
            prefix = UUID.randomUUID().toString().replace("-", "");
        }
        return generateFilename(prefix, request.getOriginalFilename());
    }
    
    @Override
    public String getBucket(FileStorageRequest request) {
        String bucket = null;
        if (request instanceof OssFileObject) {
            bucket = ((OssFileObject) request).getBucket();
        }
        if (StringUtils.isEmpty(bucket)) {
            bucket = ossConfig.getDefaultBucket();
        }
        
        return bucket;
    }
    
    @Override
    public String getBucket(String key) {
        return ossConfig.getDefaultBucket();
    }
    
    @Override
    public ObjectMetadata createObjectMeta(FileStorageRequest request) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(request.getContentType());
        metadata.setUserMetadata(createUserMetadata(request));
        return metadata;
    }
    
    @Override
    public FileObject revertFromObjectMeta(ObjectMetadata metadata,
                                           String key) {
        OssFileObject fileObject = new OssFileObject();
        fileObject.setId(key);
        fileObject.setContentType(metadata.getContentType());
        Map<String, String> userMetadata = metadata.getUserMetadata();
        if (userMetadata != null) {
            fileObject.setBizId(getFromUserMetadata("BizId", userMetadata));
            fileObject.setCategory(getFromUserMetadata("Category",
                                                       userMetadata));
            fileObject.setCode(getFromUserMetadata("Code", userMetadata));
            fileObject.setFinalFilename(getFromUserMetadata("FinalFilename",
                                                            userMetadata));
            if (StringUtils.isBlank(key)) {
                fileObject.setId(fileObject.getFinalFilename());
            }
            fileObject.setOriginalFilename(getFromUserMetadata("OriginalFilename",
                                                               userMetadata));
            fileObject.setPrettyFilename(getFromUserMetadata("PrettyFilename",
                                                             userMetadata));
            fileObject.setUploadedBy(getFromUserMetadata("UploadedBy",
                                                         userMetadata));
            String uploadAtStr = getFromUserMetadata("UploadAt", userMetadata);
            if (StringUtils.isNumeric(uploadAtStr)) {
                fileObject.setUploadedAt(new Date(Long.parseLong(uploadAtStr)));
            }
            String versionStr = getFromUserMetadata("Version", userMetadata);
            if (StringUtils.isNumeric(versionStr)) {
                fileObject.setVersion(Integer.parseInt(versionStr));
            }
            
            fileObject.setAttributes(userMetadata);
        }
        
        return fileObject;
    }
    
    private String getFromUserMetadata(String property,
                                       Map<String, String> userMetadata) {
        String key = (METADATA_KEY_PREFIX + property).toLowerCase();
        String value = null;
        if (userMetadata.containsKey(key)) {
            value = userMetadata.remove(key);
        }
        return value;
    }
    
    private Map<String, String> createUserMetadata(FileStorageRequest request) {
        Map<String, String> metadata = new HashMap<String, String>();
        if (request.getAttributes() != null) {
            metadata.putAll(request.getAttributes());
        }
        
        putUserMetadata("BizId", request.getBizId(), metadata);
        putUserMetadata("Category", request.getCategory(), metadata);
        putUserMetadata("Code", request.getCode(), metadata);
        putUserMetadata("FinalFilename", request.getFinalFilename(), metadata);
        putUserMetadata("OriginalFilename",
                        request.getOriginalFilename(),
                        metadata);
        putUserMetadata("PrettyFilename",
                        request.getPrettyFilename(),
                        metadata);
        putUserMetadata("UploadedBy", request.getUploadedBy(), metadata);
        if (request instanceof OssFileObject) {
            OssFileObject fileObject = (OssFileObject) request;
            if (fileObject.getUploadedAt() != null) {
                putUserMetadata("UploadedAt",
                                String.valueOf(fileObject.getUploadedAt()
                                                         .getTime()),
                                metadata);
            }
            metadata.put(METADATA_KEY_PREFIX + "Version",
                         String.valueOf(fileObject.getVersion()));
        }
        
        return metadata;
    }
    
    private void putUserMetadata(String property,
                                 String value,
                                 Map<String, String> metadata) {
        if (value != null) {
            metadata.put(METADATA_KEY_PREFIX + property, value);
        }
    }
    
    private String generateFilename(String prefix, String originalFilename) {
        String extName = FilenameUtils.getExtension(originalFilename);
        if (StringUtils.isEmpty(extName)) {
            return prefix;
        }
        return prefix + "." + extName;
    }
}
