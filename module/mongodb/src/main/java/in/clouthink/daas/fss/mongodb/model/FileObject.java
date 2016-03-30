package in.clouthink.daas.fss.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

/**
 * Created by dz on 16/3/29.
 */
@Document(collection = "FileObjects")
public class FileObject implements in.clouthink.daas.fss.core.FileObject {

	@Id
	private String id;

	@Indexed
	private String category;

	@Indexed
	private String finalFilename;

	private String originalFilename;

	private String prettyFilename;

	private String contentType;

	@Indexed
	private String uploadedBy;

	@Indexed
	private Date uploadedAt;

	private int version;

	private String code;

	@Indexed
	private String bizId;

	private Map<String, String> attributes;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}
