package in.clouthink.daas.fss.rest;

import in.clouthink.daas.fss.core.FileObject;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author dz on 16/7/13.
 */
public class DefaultFileObjectDescriptor implements FileObjectDescriptor {

	public static DefaultFileObjectDescriptor from(FileObject fileObject, Map<String,Object> extraAttrs) {
		if (fileObject == null) {
			return null;
		}
		DefaultFileObjectDescriptor result = new DefaultFileObjectDescriptor();
		BeanUtils.copyProperties(fileObject, result);
		result.setExtraAttrs(extraAttrs);
		return result;
	}

	private String id;

	private String code;

	private String name;

	private String category;

	private String description;

	private String bizId;

	private long size;

	private String uploadedBy;

	private Date uploadedAt;

	private Map<String,Object> extraAttrs = new HashMap<String,Object>();

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	@Override
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
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
	public Map<String,Object> getExtraAttrs() {
		return extraAttrs;
	}

	public void setExtraAttrs(Map<String,Object> extraAttrs) {
		this.extraAttrs = extraAttrs;
	}
}
