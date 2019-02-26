package in.clouthink.daas.fss.mysql.model;

import in.clouthink.daas.fss.core.StoreFileRequest;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
@Entity
@Table(name = "fss_file_object")
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

    @Basic
    private String category;

    @Basic
    private String code;

    @Basic
    private String name;

    @Basic
    private String description;

    @Basic
    private String attachedId;

    @Basic
    private String provider;

    @Basic
    private String storedFilename;

    @Basic
    private String originalFilename;

    @Basic
    private String prettyFilename;

    @Basic
    private String fileUrl;

    @Basic
    private String contentType;

    @Basic
    private String uploadedBy;

    @Basic
    private Date uploadedAt;

    @Basic
    private long size;

    @Basic
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String getAttachedId() {
        return attachedId;
    }

    public void setAttachedId(String attachedId) {
        this.attachedId = attachedId;
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
