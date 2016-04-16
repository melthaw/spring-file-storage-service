package in.clouthink.daas.fss.alioss.model;

import java.util.Date;
import java.util.Map;

import in.clouthink.daas.fss.core.FileObject;

/**
 * Created by LiangBin on 16/4/16.
 */
public class OssFileObject implements FileObject {
    
    private String id;
    
    private String bucket;
    
    private String category;
    
    private String finalFilename;
    
    private String originalFilename;
    
    private String prettyFilename;
    
    private String contentType;
    
    private String uploadedBy;
    
    private String code;
    
    private String bizId;
    
    private Map<String, String> attributes;
    
    private Date uploadedAt;
    
    private int version;
    
    @Override
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getBucket() {
        return bucket;
    }
    
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
    
    @Override
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    @Override
    public String getFinalFilename() {
        return finalFilename;
    }
    
    public void setFinalFilename(String finalFilename) {
        this.finalFilename = finalFilename;
    }
    
    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }
    
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }
    
    @Override
    public String getPrettyFilename() {
        return prettyFilename;
    }
    
    public void setPrettyFilename(String prettyFilename) {
        this.prettyFilename = prettyFilename;
    }
    
    @Override
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    @Override
    public String getUploadedBy() {
        return uploadedBy;
    }
    
    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    @Override
    public String getBizId() {
        return bizId;
    }
    
    public void setBizId(String bizId) {
        this.bizId = bizId;
    }
    
    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    
    @Override
    public Date getUploadedAt() {
        return uploadedAt;
    }
    
    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
    
    @Override
    public int getVersion() {
        return version;
    }
    
    public void setVersion(int version) {
        this.version = version;
    }
    
}
