package in.clouthink.daas.fss.core;

import in.clouthink.daas.fss.domain.model.FileObject;

public interface StoreFileResponse {

    String getStoreProvider();

    FileObject getStoreFileObject();

}
