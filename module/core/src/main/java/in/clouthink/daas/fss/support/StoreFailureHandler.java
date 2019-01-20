package in.clouthink.daas.fss.support;

import in.clouthink.daas.fss.core.StoreFileException;
import in.clouthink.daas.fss.core.StoreFileRequest;

public interface StoreFailureHandler {

    void onStoreFailure(StoreFileRequest request, StoreFileException exception) ;

}
