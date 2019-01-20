package in.clouthink.daas.fss.domain.model;

import java.util.Date;
import java.util.Map;

/**
 * The file object update history (version management).
 *
 * @author dz
 */
public interface FileObjectHistory {

    /**
     * @return the main file object
     */
    FileObject getFileObject();

    /**
     * @return stored file name
     */
    String getStoredFilename();

    /**
     * @return original file name
     */
    String getOriginalFilename();

    /**
     * @return pretty file name
     */
    String getPrettyFilename();

    /**
     * @return content type
     */
    String getContentType();

    /**
     * @return uploader
     */
    String getUploadedBy();

    /**
     * @return the uploaded time
     */
    Date getUploadedAt();

    /**
     * @return the size of file
     */
    long getSize();

    /**
     * @return the version of file
     */
    int getVersion();

    /**
     * @return the extended attributes
     */
    Map<String, String> getAttributes();

}
