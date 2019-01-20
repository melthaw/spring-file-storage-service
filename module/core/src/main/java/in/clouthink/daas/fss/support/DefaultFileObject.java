package in.clouthink.daas.fss.support;

import in.clouthink.daas.fss.domain.model.MutableFileObject;
import in.clouthink.daas.fss.core.StoreFileRequest;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
public class DefaultFileObject implements MutableFileObject {

	public static DefaultFileObject from(StoreFileRequest request) {
		if (request == null) {
			return null;
		}
		if (request instanceof DefaultFileObject) {
			return (DefaultFileObject) request;
		}
		DefaultFileObject fileObject = new DefaultFileObject();
		BeanUtils.copyProperties(request, fileObject);
		if (fileObject.getAttributes() == null) {
			fileObject.setAttributes(new HashMap<String,String>());
		}
		return fileObject;
	}

	private String category;

	private String code;

	private String name;

	private String description;

	private String bizId;

	private String finalFilename;

	private String originalFilename;

	private String prettyFilename;

	private String contentType;

	private long size;

	private Date uploadedAt;

	private String uploadedBy;

	private int version;

	private Map<String,String> attributes = new HashMap<String,String>();

	@Override
	public String getStoredFilename() {
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
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public Date getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(Date uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	@Override
	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	@Override
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public Map<String,String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String,String> attributes) {
		this.attributes = attributes;
	}

}
