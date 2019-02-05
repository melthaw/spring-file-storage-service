package in.clouthink.daas.fss.webdav.exception;

/**
 * @author dz
 * @since 3
 */
public class WebDavHttpException extends WebDavStoreException {

    public WebDavHttpException() {
    }

    public WebDavHttpException(String message) {
        super(message);
    }

    public WebDavHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebDavHttpException(Throwable cause) {
        super(cause);
    }

}
