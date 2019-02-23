package in.clouthink.daas.fss.webdav.exception;

/**
 * @author dz
 * @since 3
 */
public class WebDavUploadException extends WebDavStoreException {

    public WebDavUploadException() {
    }

    public WebDavUploadException(String message) {
        super(message);
    }

    public WebDavUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebDavUploadException(Throwable cause) {
        super(cause);
    }

}
