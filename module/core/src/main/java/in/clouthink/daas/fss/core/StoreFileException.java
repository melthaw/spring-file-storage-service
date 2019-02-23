package in.clouthink.daas.fss.core;

/**
* @author dz
 */
public class StoreFileException extends RuntimeException {

	public StoreFileException() {
	}

	public StoreFileException(String message) {
		super(message);
	}

	public StoreFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public StoreFileException(Throwable cause) {
		super(cause);
	}

}
