package in.clouthink.daas.fss.glusterfs.exception;

import in.clouthink.daas.fss.core.StoreFileException;

/**
 * @author dz
 */
public class GlusterfsStoreException extends StoreFileException {

	public GlusterfsStoreException() {
	}

	public GlusterfsStoreException(String message) {
		super(message);
	}

	public GlusterfsStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public GlusterfsStoreException(Throwable cause) {
		super(cause);
	}

}
