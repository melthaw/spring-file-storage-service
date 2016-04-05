package in.clouthink.daas.fss.sample.spring.rest;

import in.clouthink.daas.fss.mongodb.repository.FileObjectQueryParameter;

import java.util.Date;

/**
 * Created by dz on 16/4/5.
 */
public class DefaultFileObjectQueryParameter implements FileObjectQueryParameter {

	private String category;

	private String code;

	private String bizId;

	private String uploadedBy;

	private Date uploadedAtFrom;

	private Date uploadedAtTo;

	@Override
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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
	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	@Override
	public Date getUploadedAtFrom() {
		return uploadedAtFrom;
	}

	public void setUploadedAtFrom(Date uploadedAtFrom) {
		this.uploadedAtFrom = uploadedAtFrom;
	}

	@Override
	public Date getUploadedAtTo() {
		return uploadedAtTo;
	}

	public void setUploadedAtTo(Date uploadedAtTo) {
		this.uploadedAtTo = uploadedAtTo;
	}
}
