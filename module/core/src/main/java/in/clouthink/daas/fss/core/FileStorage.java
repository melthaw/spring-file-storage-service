package in.clouthink.daas.fss.core;

import java.io.File;
import java.io.InputStream;

/**
 * The file storage abstraction ( store, search , delete physically )
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
     * @param filename the final stored filename of the file object
     * @return StoredFileObject
     */
    StoredFileObject findByStoredFilename(String filename);

    /**
     * Delete the stored file object physically.
     *
     * @param filename the final stored filename of the file to delete
     * @return StoredFileObject ( the writeTo method is not supported since the file is deleted )
     */
    StoredFileObject delete(String filename);

}
