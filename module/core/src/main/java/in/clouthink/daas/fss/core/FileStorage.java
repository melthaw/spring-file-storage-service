package in.clouthink.daas.fss.core;

import java.io.File;
import java.io.InputStream;

/**
 * The file storage abstraction
 *
 * @author dz on 16/3/28.
 */
public interface FileStorage {

    /**
     * @return the name of file storage provider
     */
    String getName();

    /**
     * @param inputStream input stream of the uploaded file
     * @param request     the file store request
     * @return StoreFileResponse
     * @throws StoreFileException
     */
    StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException;

    /**
     * @param file    the uploaded file
     * @param request the file store request
     * @return StoreFileResponse
     * @throws StoreFileException
     */
    StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException;

    /**
     * @param bytes   the uploaded file in bytes
     * @param request the file store request
     * @return StoreFileResponse
     * @throws StoreFileException
     */
    StoreFileResponse store(byte[] bytes, StoreFileRequest request) throws StoreFileException;

    /**
     * @param id the id of the file object
     * @return StoredFileObject
     */
    StoredFileObject findById(String id);

    /**
     * @param filename the final filename of the file object
     * @return StoredFileObject
     */
    StoredFileObject findByFilename(String filename);

    /**
     * Delete the stored file object physically.
     *
     * @param fileObject the file to delete
     */
    void delete(StoredFileObject fileObject);

}
