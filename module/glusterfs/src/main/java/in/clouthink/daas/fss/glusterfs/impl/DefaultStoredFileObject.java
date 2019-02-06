package in.clouthink.daas.fss.glusterfs.impl;

import in.clouthink.daas.fss.core.StoreFileRequest;
import in.clouthink.daas.fss.core.StoredFileObject;
import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.support.DefaultFileObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.io.OutputStream;

public class DefaultStoredFileObject extends DefaultFileObject implements StoredFileObject {

    public static DefaultStoredFileObject from(StoreFileRequest request) {
        if (request == null) {
            return null;
        }
        DefaultStoredFileObject result = new DefaultStoredFileObject();
        BeanUtils.copyProperties(request, result);
        return result;
    }

    public static DefaultStoredFileObject from(FileObject fileObject) {
        if (fileObject == null) {
            return null;
        }
        DefaultStoredFileObject result = new DefaultStoredFileObject();
        BeanUtils.copyProperties(fileObject, result);
        return result;
    }

    private static final Log logger = LogFactory.getLog(DefaultStoredFileObject.class);

    private String providerName;

    private GlusterFile glusterFile;

    @Override
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public GlusterFile getImplementation() {
        return glusterFile;
    }

    public void setImplementation(GlusterFile s3Object) {
        this.glusterFile = s3Object;
    }

    @Override
    public void writeTo(OutputStream outputStream, int bufferSize) throws IOException {
        if (getImplementation() == null) {
            throw new UnsupportedOperationException("The stored file implementation is not supplied.");
        }

        logger.warn(String.format("The bufferSize is not supported in current provider, the value[%d] will be ignored.",
                                  bufferSize));

        glusterFile.writeTo(outputStream);
    }

}
