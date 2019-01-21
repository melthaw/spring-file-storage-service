package in.clouthink.daas.fss.mysql.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

/**
 * @author dz
 */
@Entity
@Table(name = "FSS_FILE_OBJECT_ATTR")
public class FileObjectAttribute {

    @Id
    @Access(AccessType.PROPERTY)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    private String key;

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_object_id")
    private FileObject fileObject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
    }
}
