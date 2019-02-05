package in.clouthink.daas.fss.core;

import in.clouthink.daas.fss.domain.model.FileObject;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The stored file object which support write to output stream which is useful if the client want to download the file .
 *
 * @author dz
 */
public interface StoredFileObject extends FileObject {

    /**
     * The provider to supply the store service , for example : zimg or alioss
     *
     * @return the storage provider
     */
    String getProviderName();

    /**
     * @return the underlying implementation object (based on the provider)
     */
    Object getImplementation();

    /**
     * @param outputStream
     * @param bufferSize   default 4 * 1024b, if equal or less than 0, the default value is choosed
     * @throws IOException
     */
    void writeTo(OutputStream outputStream, int bufferSize) throws IOException;

}
