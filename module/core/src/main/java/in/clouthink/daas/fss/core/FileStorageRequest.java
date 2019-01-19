package in.clouthink.daas.fss.core;

import java.util.Map;

/**
 * The
 * <p>
 *
 * @author dz
 */
public interface FileStorageRequest {

	/**
	 * for example :  android, ad, album, etc. we suggest to organize the file object by biz category
	 *
	 * @return category
	 */
	String getCategory();

	/**
	 * The auxiliary file object code can help to quick find the file object.
	 *
	 * @return code
	 */
	String getCode();

	/**
	 * The display name for the file object
	 *
	 * @return name
	 */
	String getName();

	/**
	 * The description of the file object
	 *
	 * @return description
	 */
	String getDescription();

	/**
	 * The biz object id which the uploaded file associated with.
	 *
	 * @return the id of referenced biz object
	 */
	String getBizId();

	/**
	 * The original file name of store request
	 *
	 * @return original file name
	 */
	String getOriginalFilename();

	/**
	 * The store request can specify the pretty file name for download in the future.
	 *
	 * @return pretty file name
	 */
	String getPrettyFilename();

	/**
	 * The value of http header content type ( when download in the future , we will set it back to http response)
	 *
	 * @return http header Content-Type
	 */
	String getContentType();

	/**
	 * Who upload the file
	 *
	 * @return who
	 */
	String getUploadedBy();

	/**
	 * The size of uploaded file
	 *
	 * @return size
	 */
	long getSize();

	/**
	 * The customized attributes which as extra metadata for the uploaded file
	 *
	 * @return attributes
	 */
	Map<String,String> getAttributes();

}
