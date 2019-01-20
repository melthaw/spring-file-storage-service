package in.clouthink.daas.fss.core;

import java.util.Map;

/**
 * The
 * <p>
 *
 * @author dz
 */
public interface StoreFileRequest {

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
	 * The customized attributes which as extra metadata for the uploaded file
	 *
	 * @return attributes
	 */
	Map<String, String> getAttributes();

}
