package in.clouthink.daas.fss.zimg.exception;

/**
 * @author dz
 * @since 3
 */
public class ZimgUploadException extends ZimgStoreException {

    public ZimgUploadException() {
    }

    public ZimgUploadException(String message) {
        super(message);
    }

    public ZimgUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZimgUploadException(Throwable cause) {
        super(cause);
    }

}
