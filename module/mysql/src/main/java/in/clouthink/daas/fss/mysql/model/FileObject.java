package in.clouthink.daas.fss.mysql.model;

import in.clouthink.daas.fss.core.StoreFileRequest;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
@Entity
@Table(name = "FSS_FILE_OBJECT")
public class FileObject implements in.clouthink.daas.fss.domain.model.FileObject {

    public static FileObject from(in.clouthink.daas.fss.domain.model.FileObject fromObject) {
        FileObject result = new FileObject();
        BeanUtils.copyProperties(fromObject, result);
        if (result.getAttributes() == null) {
            result.setAttributes(new HashMap<String, String>());
        }
        return result;
    }

    public static FileObject from(StoreFileRequest request) {
        FileObject result = new FileObject();
        BeanUtils.copyProperties(request, result);
        if (result.getAttributes() == null) {
            result.setAttributes(new HashMap<String, String>());
        }
        return result;
    }

    @Id
    @Access(AccessType.PROPERTY)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    private String category;

    private String code;

    private String name;

    private String description;

    private String bizId;

    private String storedFilename;

    private String originalFilename;

    private String prettyFilename;

    private String url;

    private String contentType;

    private String uploadedBy;

    private Date uploadedAt;

    private long size;

    private int version;

    private transient Map<String, String> attributes = new HashMap<String, String>();

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
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
