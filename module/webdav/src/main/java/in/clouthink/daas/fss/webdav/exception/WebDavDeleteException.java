package in.clouthink.daas.fss.webdav.exception;

/**
 * @author dz
 * @since 3
 */

public class WebDavDeleteException extends WebDavStoreException {

    public WebDavDeleteException() {
    }

    public WebDavDeleteException(String message) {
        super(message);
    }

    public WebDavDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebDavDeleteException(Throwable cause) {
        super(cause);
    }

}
