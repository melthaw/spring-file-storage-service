package in.clouthink.daas.fss.zimg.exception;

/**
 * @author dz
 * @since 3
 */
public class ZimgDownloadException extends ZimgStoreException {

    public ZimgDownloadException() {
    }

    public ZimgDownloadException(String message) {
        super(message);
    }

    public ZimgDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZimgDownloadException(Throwable cause) {
        super(cause);
    }

}
