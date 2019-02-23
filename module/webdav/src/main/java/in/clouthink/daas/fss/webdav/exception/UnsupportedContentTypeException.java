package in.clouthink.daas.fss.webdav.exception;

/**
 * @author dz
 * @since 3
 */
public class UnsupportedContentTypeException extends WebDavStoreException {

	public UnsupportedContentTypeException() {
	}

	public UnsupportedContentTypeException(String message) {
		super(message);
	}

	public UnsupportedContentTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedContentTypeException(Throwable cause) {
		super(cause);
	}

}
