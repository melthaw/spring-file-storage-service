package in.clouthink.daas.fss.webdav.exception;

import in.clouthink.daas.fss.core.StoreFileException;

/**
 * @author dz
 * @since 3
 */

public class WebDavStoreException extends StoreFileException {

	public WebDavStoreException() {
	}

	public WebDavStoreException(String message) {
		super(message);
	}

	public WebDavStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebDavStoreException(Throwable cause) {
		super(cause);
	}

}
