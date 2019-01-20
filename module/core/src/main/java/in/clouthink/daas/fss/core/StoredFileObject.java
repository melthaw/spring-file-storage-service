package in.clouthink.daas.fss.core;

import in.clouthink.daas.fss.domain.model.FileObject;

import java.io.IOException;
import java.io.OutputStream;

public interface StoredFileObject extends FileObject {

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
