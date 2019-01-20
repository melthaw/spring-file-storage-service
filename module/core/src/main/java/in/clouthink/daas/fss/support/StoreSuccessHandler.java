package in.clouthink.daas.fss.support;

import in.clouthink.daas.fss.core.StoreFileRequest;
import in.clouthink.daas.fss.core.StoreFileResponse;

public interface StoreSuccessHandler {

    void onStoreSuccess(StoreFileRequest request, StoreFileResponse response);

}
