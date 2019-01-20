package in.clouthink.daas.fss.domain.model;

import in.clouthink.daas.fss.core.StoreFileRequest;

import java.util.Date;

/**
 * The file object which contains the uploaded-data's information saved in backend
 *
 * @author dz
 */
public interface FileObject extends StoreFileRequest {

	/**
	 * The provider to supply the store service , for example : zimg or alioss
	 *
	 * @return the storage provider
	 */
	String getStoreProvider();

	/**
	 * The final stored physical file name in backend (the final file name must be unique)
	 *
	 * for example : /2019/01/01/32179432792779432.jpg
	 *
	 * @return final file name
	 */
	String getStoredFilename();

	/**
	 * The full final name which can be downloaded directly.
	 *
	 * for example : http://oss.aliyun.com/fss/2019/01/01/32179432792779432.jpg
	 *
	 * @return full file name
	 */
	String getFullFilename();

	/**
	 * The date time when the file is uploaded
	 *
	 * @return the uploaded date time
	 */
	Date getUploadedAt();

	/**
	 * The version of the uploaded file if the implementation
	 * support the file object version management
	 *
	 * @return version
	 */
	int getVersion();


}
