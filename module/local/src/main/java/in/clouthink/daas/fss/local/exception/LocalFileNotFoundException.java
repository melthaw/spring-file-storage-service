package in.clouthink.daas.fss.local.exception;

/**
 * @author dz
 */
public class LocalFileNotFoundException extends LocalFileStoreException {

    public LocalFileNotFoundException() {
    }

    public LocalFileNotFoundException(String message) {
        super(message);
    }

    public LocalFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalFileNotFoundException(Throwable cause) {
        super(cause);
    }
}
