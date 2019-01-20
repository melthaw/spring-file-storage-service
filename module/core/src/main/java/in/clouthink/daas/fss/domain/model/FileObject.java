package in.clouthink.daas.fss.domain.model;

import java.util.Date;
import java.util.Map;

/**
 * The file object which contains the uploaded-data's information saved in backend
 *
 * @author dz
 */
public interface FileObject {

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
     * The final stored physical file name in backend (the final file name must be unique)
     * <p>
     * for example : /2019/01/01/32179432792779432.jpg
     *
     * @return final file name
     */
    String getStoredFilename();

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
     * The date time when the file is uploaded
     *
     * @return the uploaded date time
     */
    Date getUploadedAt();

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
    Map<String, String> getAttributes();

    /**
     * The final url which can be downloaded directly.
     * <p>
     * for example : http://oss.aliyun.com/fss/2019/01/01/32179432792779432.jpg
     *
     * @return full file name
     */
    String getUrl();

}
