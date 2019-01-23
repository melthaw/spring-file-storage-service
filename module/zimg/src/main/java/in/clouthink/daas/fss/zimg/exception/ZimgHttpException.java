package in.clouthink.daas.fss.zimg.exception;

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
