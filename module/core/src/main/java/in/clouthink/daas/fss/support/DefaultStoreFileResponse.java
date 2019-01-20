package in.clouthink.daas.fss.support;

import in.clouthink.daas.fss.core.StoreFileResponse;
import in.clouthink.daas.fss.domain.model.FileObject;

public class DefaultStoreFileResponse implements StoreFileResponse {

    private String provider;

    private FileObject fileObject;

    public DefaultStoreFileResponse(String provider, FileObject fileObject) {
        this.provider = provider;
        this.fileObject = fileObject;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public FileObject getStoredFileObject() {
        return fileObject;
    }
}
