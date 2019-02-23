package in.clouthink.daas.fss.support;

import in.clouthink.daas.fss.domain.model.FileObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
public class DefaultFileObject implements FileObject {

    private String storedFilename;

    private String originalFilename;

    private String prettyFilename;

    private String fileUrl;

    private String imageUrl;

    private String contentType;

    private long size;

    private Date uploadedAt;

    private String uploadedBy;

    private Map<String, String> attributes = new HashMap<String, String>();

    @Override
    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
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
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DefaultFileObject{");
        sb.append("storedFilename='").append(storedFilename).append('\'');
        sb.append(", originalFilename='").append(originalFilename).append('\'');
        sb.append(", prettyFilename='").append(prettyFilename).append('\'');
        sb.append(", fileUrl='").append(fileUrl).append('\'');
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", contentType='").append(contentType).append('\'');
        sb.append(", size=").append(size);
        sb.append(", uploadedAt=").append(uploadedAt);
        sb.append(", uploadedBy='").append(uploadedBy).append('\'');
        sb.append(", attributes=").append(attributes);
        sb.append('}');
        return sb.toString();
    }

}
