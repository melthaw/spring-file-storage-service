package in.clouthink.daas.fss.support;

import in.clouthink.daas.fss.core.StoreFileResponse;
import in.clouthink.daas.fss.core.StoredFileObject;

public class DefaultStoreFileResponse implements StoreFileResponse {

    private String provider;

    private StoredFileObject fileObject;

    public DefaultStoreFileResponse(String provider, StoredFileObject fileObject) {
        this.provider = provider;
        this.fileObject = fileObject;
    }

    @Override
    public String getProviderName() {
        return provider;
    }

    @Override
    public StoredFileObject getStoredFileObject() {
        return fileObject;
    }
}
