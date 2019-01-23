package in.clouthink.daas.fss.zimg.exception;

/**
 * @author dz
 */
public class UnsupportedContentTypeException extends ZimgStoreException {

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
