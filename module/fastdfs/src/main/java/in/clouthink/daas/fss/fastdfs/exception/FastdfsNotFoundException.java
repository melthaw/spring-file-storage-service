package in.clouthink.daas.fss.fastdfs.exception;

/**
 * @author dz
 */
public class FastdfsNotFoundException extends FastdfsStoreException {

    public FastdfsNotFoundException() {
    }

    public FastdfsNotFoundException(String message) {
        super(message);
    }

    public FastdfsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastdfsNotFoundException(Throwable cause) {
        super(cause);
    }
}
