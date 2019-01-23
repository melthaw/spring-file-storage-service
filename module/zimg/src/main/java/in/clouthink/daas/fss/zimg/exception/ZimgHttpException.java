package in.clouthink.daas.fss.zimg.exception;

/**
 * @author dz
 * @since 3
 */
public class ZimgHttpException extends ZimgStoreException {

    public ZimgHttpException() {
    }

    public ZimgHttpException(String message) {
        super(message);
    }

    public ZimgHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZimgHttpException(Throwable cause) {
        super(cause);
    }

}
