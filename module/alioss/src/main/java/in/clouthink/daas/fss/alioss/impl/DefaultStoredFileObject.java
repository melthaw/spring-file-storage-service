package in.clouthink.daas.fss.alioss.impl;

import com.aliyun.oss.model.OSSObject;
import in.clouthink.daas.fss.core.StoreFileRequest;
import in.clouthink.daas.fss.core.StoredFileObject;
import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.support.DefaultFileObject;
import in.clouthink.daas.fss.util.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log logger = LogFactory.getLog(DefaultStoredFileObject.class);

    private String providerName;

    private OSSObject ossObject;

    @Override
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public OSSObject getImplementation() {
        return ossObject;
    }

    public void setImplementation(OSSObject ossObject) {
        this.ossObject = ossObject;
    }

    @Override
    public void writeTo(OutputStream outputStream, long bufferSize) throws IOException {
        if (getImplementation() == null) {
            throw new UnsupportedOperationException("The stored file implementation is not supplied.");
        }
        if (bufferSize <= 0) {
            bufferSize = 1024 * 4;
        }
        InputStream is = ossObject.getObjectContent();
        int length = -1;
        byte[] bytes = new byte[(int) bufferSize];
        while ((length = is.read(bytes)) > 0) {
            outputStream.write(bytes, 0, length);
        }
        IOUtils.close(is);
    }

}
