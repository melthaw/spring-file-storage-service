package in.clouthink.daas.fss.mongodb.model;

import in.clouthink.daas.fss.core.StoreFileRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
@Document(collection = "FssFileObjects")
public class FileObject implements in.clouthink.daas.fss.domain.model.FileObject {

    public static FileObject from(StoreFileRequest request) {
        FileObject result = new FileObject();
        BeanUtils.copyProperties(request, result);
        if (request.getAttributes() != null) {
            result.setAttributes(request.getAttributes());
        }
        return result;
    }

    @Id
    private String id;

    private String category;

    private String code;

    @Indexed
    private String name;

    private String description;

    @Indexed
    private String bizId;

    @Indexed
    private String storedFilename;

    private String originalFilename;

    private String prettyFilename;

    private String fileUrl;

    private String imageUrl;

    private String contentType;

    @Indexed
    private String uploadedBy;

    @Indexed
    private Date uploadedAt;

    private long size;

    private int version;

    private Map<String, String> attributes = new HashMap<String, String>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

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
    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Override
    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
        final StringBuffer sb = new StringBuffer("FileObject{");
        sb.append("id='").append(id).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", bizId='").append(bizId).append('\'');
        sb.append(", storedFilename='").append(storedFilename).append('\'');
        sb.append(", originalFilename='").append(originalFilename).append('\'');
        sb.append(", prettyFilename='").append(prettyFilename).append('\'');
        sb.append(", fileUrl='").append(fileUrl).append('\'');
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", contentType='").append(contentType).append('\'');
        sb.append(", uploadedBy='").append(uploadedBy).append('\'');
        sb.append(", uploadedAt=").append(uploadedAt);
        sb.append(", size=").append(size);
        sb.append(", version=").append(version);
        sb.append(", attributes=").append(attributes);
        sb.append('}');
        return sb.toString();
    }
}
