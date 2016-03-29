package in.clouthink.daas.fss.core;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by dz on 16/3/28.
 */
public interface FileStorage {

	/**
	 * @return the general file object which contains the information of stored file object.
	 */
	FileObject getFileObject();

	/**
	 * @return the underlying implementation object (based on the provider)
	 */
	Object getImplementation();

	/**
	 * @param outputStream
	 * @param bufferSize   default 4 * 1024b, if equal or less than 0, the default value is choosed
	 * @throws IOException
	 */
	void writeTo(OutputStream outputStream, long bufferSize) throws IOException;

}
