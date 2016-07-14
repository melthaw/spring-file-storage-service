package in.clouthink.daas.fss.alioss;

import in.clouthink.daas.fss.core.FileStorageException;

/**
 * @author dz
 */
public class OssStorageException extends FileStorageException {
	public OssStorageException() {
	}

	public OssStorageException(String message) {
		super(message);
	}

	public OssStorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public OssStorageException(Throwable cause) {
		super(cause);
	}
}
