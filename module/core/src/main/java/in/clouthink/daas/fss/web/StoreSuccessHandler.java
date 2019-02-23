package in.clouthink.daas.fss.web;

import in.clouthink.daas.fss.core.StoreFileRequest;
import in.clouthink.daas.fss.core.StoreFileResponse;

public interface StoreSuccessHandler<T> {

    T onStoreSuccess(StoreFileRequest request, StoreFileResponse response);

}
