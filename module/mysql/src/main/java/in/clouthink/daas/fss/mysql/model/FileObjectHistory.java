package in.clouthink.daas.fss.mysql.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
@Entity
@Table(name = "fss_file_object_history")
public class FileObjectHistory implements in.clouthink.daas.fss.domain.model.FileObjectHistory {

    public static FileObjectHistory from(in.clouthink.daas.fss.mysql.model.FileObject fileObjectImpl) {
        FileObjectHistory result = new FileObjectHistory();
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
    @Access(AccessType.PROPERTY)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_object_id")
    private FileObject fileObject;

    private String storedFilename;

    private String originalFilename;

    private String prettyFilename;

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

    @Override
    public FileObject getFileObject() {
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
