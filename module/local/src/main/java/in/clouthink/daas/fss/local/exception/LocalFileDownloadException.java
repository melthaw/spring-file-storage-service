package in.clouthink.daas.fss.local.exception;

/**
 * @author dz
 */
public class LocalFileDownloadException extends LocalFileStoreException {

    public LocalFileDownloadException() {
    }

    public LocalFileDownloadException(String message) {
        super(message);
    }

    public LocalFileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalFileDownloadException(Throwable cause) {
        super(cause);
    }
}
