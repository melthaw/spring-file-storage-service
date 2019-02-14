package in.clouthink.daas.fss.support;

import in.clouthink.daas.fss.core.StoreFileResponse;
import in.clouthink.daas.fss.core.StoredFileObject;

public class DefaultStoreFileResponse implements StoreFileResponse {

    private String providerName;

    private StoredFileObject fileObject;

    public DefaultStoreFileResponse(String provider, StoredFileObject fileObject) {
        this.providerName = provider;
        this.fileObject = fileObject;
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public StoredFileObject getStoredFileObject() {
        return fileObject;
    }

}
