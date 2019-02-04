package in.clouthink.daas.fss.fastdfs.exception;

import in.clouthink.daas.fss.core.StoreFileException;

/**
 * @author dz
 */
public class FastdfsStoreException extends StoreFileException {

	public FastdfsStoreException() {
	}

	public FastdfsStoreException(String message) {
		super(message);
	}

	public FastdfsStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public FastdfsStoreException(Throwable cause) {
		super(cause);
	}
}
