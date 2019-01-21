package in.clouthink.daas.fss.mongodb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.clouthink.daas.fss.mysql.model.FileObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
@Document(collection = "FssFileObjectHistories")
public class FileObjectHistory implements in.clouthink.daas.fss.domain.model.FileObjectHistory {

    public static in.clouthink.daas.fss.mysql.model.FileObjectHistory from(
            in.clouthink.daas.fss.mysql.model.FileObject fileObjectImpl) {
        in.clouthink.daas.fss.mysql.model.FileObjectHistory result = new in.clouthink.daas.fss.mysql.model.FileObjectHistory();
        result.setFileObject(fileObjectImpl);
        result.setStoredFilename(fileObjectImpl.getStoredFilename());
        result.setOriginalFilename(fileObjectImpl.getOriginalFilename());
        result.setPrettyFilename(fileObjectImpl.getPrettyFilename());

        result.setContentType(fileObjectImpl.getContentType());
        result.setUploadedBy(fileObjectImpl.getUploadedBy());
        result.setUploadedAt(fileObjectImpl.getUploadedAt());
        result.setVersion(fileObjectImpl.getVersion());
        result.setSize(fileObjectImpl.getSize());
        result.setAttributes(fileObjectImpl.getAttributes());
        return result;
    }

    @Id
    private String id;

    @JsonIgnore
    @Indexed
    @DBRef(lazy = true)
    private in.clouthink.daas.fss.mysql.model.FileObject fileObject;

    @Indexed
    private String storedFilename;

    private String originalFilename;

    private String prettyFilename;

    private String contentType;

    private String uploadedBy;

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

    @Override
    public in.clouthink.daas.fss.mysql.model.FileObject getFileObject() {
        return fileObject;
    }

    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
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
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
