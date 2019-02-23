package in.clouthink.daas.fss.webdav.exception;

/**
 * @author dz
 * @since 3
 */
public class WebDavDownloadException extends WebDavStoreException {

    public WebDavDownloadException() {
    }

    public WebDavDownloadException(String message) {
        super(message);
    }

    public WebDavDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebDavDownloadException(Throwable cause) {
        super(cause);
    }

}
