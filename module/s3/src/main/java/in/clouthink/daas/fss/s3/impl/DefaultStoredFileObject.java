package in.clouthink.daas.fss.s3.impl;

import com.amazonaws.services.s3.model.S3Object;
import in.clouthink.daas.fss.core.StoreFileRequest;
import in.clouthink.daas.fss.core.StoredFileObject;
import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.support.DefaultFileObject;
import in.clouthink.daas.fss.util.IOUtils;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.io.InputStream;
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

    private String providerName;

    private S3Object s3Object;

    @Override
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public S3Object getImplementation() {
        return s3Object;
    }

    public void setImplementation(S3Object s3Object) {
        this.s3Object = s3Object;
    }

    @Override
    public void writeTo(OutputStream outputStream, int bufferSize) throws IOException {
        if (getImplementation() == null) {
            throw new UnsupportedOperationException("The stored file implementation is not supplied.");
        }
        if (bufferSize <= 0) {
            bufferSize = 1024 * 4;
        }

        InputStream is = s3Object.getObjectContent();
        IOUtils.copy(is, outputStream, bufferSize);
        IOUtils.close(is);
    }

    @Override
    public String toString() {
        return "DefaultStoredFileObject{" +
                "providerName='" + providerName + '\'' +
                ", s3Object=" + s3Object +
                "} " + super.toString();
    }
}
