package in.clouthink.daas.fss.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by dz on 16/3/29.
 */
@Document(collection = "FileObjectHistories")
public class FileObjectHistory implements in.clouthink.daas.fss.core.FileObjectHistory {

	@Id
	private String id;

	@Indexed
	@DBRef
	private FileObject fileObject;

	private String finalFilename;

	private String originalFilename;

	private String prettyFilename;

	private String contentType;

	private String uploadedBy;

	private Date uploadedAt;

	private int version;

	@Override
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
