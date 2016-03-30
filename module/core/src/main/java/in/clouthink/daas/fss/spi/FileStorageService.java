package in.clouthink.daas.fss.spi;

import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.core.FileStorageMetadata;
import in.clouthink.daas.fss.core.FileStorageRequest;

import java.io.InputStream;

/**
 * Created by dz on 16/3/28.
 */
public interface FileStorageService {

	/**
	 * Get the file storage metadata of the implementation
	 *
	 * @return
	 */
	FileStorageMetadata getStorageMetadata();

	/**
	 * Store the file object
	 *
	 * @param inputStream just read from the input stream , but never close it
	 * @param request
	 * @return
	 */
	FileStorage store(InputStream inputStream, FileStorageRequest request);

	/**
	 * Restore the file object, the previous file object will be moved into history
	 *
	 * @param previousId  the id of previous stored file object
	 * @param inputStream just read from the input stream , but never close it
	 * @param request
	 * @return
	 */
	FileStorage restore(String previousId, InputStream inputStream, FileStorageRequest request);

	/**
	 * @param id the id of the file object
	 * @return
	 */
	FileStorage findById(String id);

	/**
	 * @param filename the final filename of the file object
	 * @return
	 */
	FileStorage findByFilename(String filename);

}
