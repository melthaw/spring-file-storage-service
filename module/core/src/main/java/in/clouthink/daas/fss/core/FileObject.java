package in.clouthink.daas.fss.core;

import java.util.Date;

/**
 * The file object which contains the uploaded-data's information saved in backend
 * <p>
 *
 * @author dz
 */
public interface FileObject extends FileStorageRequest {

	/**
	 * The identifier of the file object
	 *
	 * @return
	 */
	String getId();

	/**
	 * The final stored physical file name in backend (the final file name must be unique)
	 *
	 * @return
	 */
	String getFinalFilename();

	/**
	 * The date time when the file is uploaded
	 *
	 * @return
	 */
	Date getUploadedAt();

	/**
	 * The version of the uploaded file if the implementation
	 * support the file object version management
	 *
	 * @return
	 */
	int getVersion();

}
