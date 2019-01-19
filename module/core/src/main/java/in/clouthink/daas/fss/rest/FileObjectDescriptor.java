package in.clouthink.daas.fss.rest;

import java.util.Date;
import java.util.Map;

/**
 */
public interface FileObjectDescriptor {

	String getId();

	String getName();

	String getCode();

	String getCategory();

	String getDescription();

	String getBizId();

	long getSize();

	String getUploadedBy();

	Date getUploadedAt();

	/**
	 * The download url is based on the implementation, so it may be various in extra attributes .
	 */
	Map<String,Object> getExtraAttrs();

}
