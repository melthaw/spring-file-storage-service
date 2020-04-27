package in.clouthink.daas.fss.hwobs.exception;

import in.clouthink.daas.fss.core.StoreFileException;

/**
 * @author vanish
 */
public class HwObsStoreException extends StoreFileException {
	public HwObsStoreException() {
	}

	public HwObsStoreException(String message) {
		super(message);
	}

	public HwObsStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public HwObsStoreException(Throwable cause) {
		super(cause);
	}
}
