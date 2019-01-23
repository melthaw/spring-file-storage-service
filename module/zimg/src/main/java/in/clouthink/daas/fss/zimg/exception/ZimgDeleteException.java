package in.clouthink.daas.fss.zimg.exception;

/**
 * @author dz
 * @since 3
 */

public class ZimgDeleteException extends ZimgStoreException {

    public ZimgDeleteException() {
    }

    public ZimgDeleteException(String message) {
        super(message);
    }

    public ZimgDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZimgDeleteException(Throwable cause) {
        super(cause);
    }

}
