package in.clouthink.daas.fss.fastdfs.exception;

/**
 * @author dz
 */
public class FastdfsDownloadException extends FastdfsStoreException {

    public FastdfsDownloadException() {
    }

    public FastdfsDownloadException(String message) {
        super(message);
    }

    public FastdfsDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastdfsDownloadException(Throwable cause) {
        super(cause);
    }
}
