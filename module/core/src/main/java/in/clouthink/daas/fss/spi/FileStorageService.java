package in.clouthink.daas.fss.spi;

import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.core.FileStorageRequest;

import java.io.InputStream;
import java.util.Map;

/**
 * The file storage service abstraction which service for the store and withdraw the file object.
 * <p>
 *
 * @author dz on 16/3/28.
 */
public interface FileStorageService {

	/**
	 * @param id the id of the file object
	 * @return FileStorage
	 */
	FileStorage findById(String id);

	/**
	 * @param filename the final filename of the file object
	 * @return FileStorage
	 */
	FileStorage findByFilename(String filename);

	/**
	 * The extra attributes for the file object
	 *
	 * @param fileObject the file object
	 * @return Map<String,Object>
	 */
	Map<String,Object> buildExtraAttributes(FileObject fileObject);

	/**
	 * Store the file object
	 *
	 * @param inputStream just read from the input stream , but never close it
	 * @param request the file store request
	 * @return FileStorage
	 */
	FileStorage store(InputStream inputStream, FileStorageRequest request);

	/**
	 * Restore the file object, the previous file object will be moved into history
	 *
	 * @param previousId  the id of previous stored file object
	 * @param inputStream just read from the input stream , but never close it
	 * @param request the file store request
	 * @return FileStorage
	 */
	FileStorage restore(String previousId, InputStream inputStream, FileStorageRequest request);

}
