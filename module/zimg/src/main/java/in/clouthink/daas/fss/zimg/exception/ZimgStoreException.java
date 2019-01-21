package in.clouthink.daas.fss.zimg.exception;

import in.clouthink.daas.fss.core.StoreFileException;

/**
 * @author dz
 */
public class ZimgStoreException extends StoreFileException {

	public ZimgStoreException() {
	}

	public ZimgStoreException(String message) {
		super(message);
	}

	public ZimgStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZimgStoreException(Throwable cause) {
		super(cause);
	}

}
