package in.clouthink.daas.fss.s3.exception;

import in.clouthink.daas.fss.core.StoreFileException;

/**
 * @author dz
 */
public class S3StoreException extends StoreFileException {

	public S3StoreException() {
	}

	public S3StoreException(String message) {
		super(message);
	}

	public S3StoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public S3StoreException(Throwable cause) {
		super(cause);
	}

}
