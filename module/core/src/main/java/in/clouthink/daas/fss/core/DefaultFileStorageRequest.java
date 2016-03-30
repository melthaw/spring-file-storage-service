package in.clouthink.daas.fss.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dz on 16/3/30.
 */
public class DefaultFileStorageRequest implements FileStorageRequest {

	private String category;

	private String finalFilename;

	private String originalFilename;

	private String prettyFilename;

	private String contentType;

	private String uploadedBy;

	private String code;

	private String bizId;

	private Map<String, String> attributes = new HashMap<String, String>();

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
}
