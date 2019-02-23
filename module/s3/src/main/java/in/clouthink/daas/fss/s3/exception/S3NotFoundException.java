package in.clouthink.daas.fss.s3.exception;

/**
 * @author dz
 */
public class S3NotFoundException extends S3StoreException {

	public S3NotFoundException() {
	}

	public S3NotFoundException(String message) {
		super(message);
	}

	public S3NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public S3NotFoundException(Throwable cause) {
		super(cause);
	}

}
