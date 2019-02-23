package in.clouthink.daas.fss.qiniu.exception;

import in.clouthink.daas.fss.core.StoreFileException;

/**
 * @author dz
 * @since 3
 */
public class QiniuStoreException extends StoreFileException {
    public QiniuStoreException() {
    }

    public QiniuStoreException(String message) {
        super(message);
    }

    public QiniuStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public QiniuStoreException(Throwable cause) {
        super(cause);
    }
}
