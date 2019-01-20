package in.clouthink.daas.fss.domain.request;

import java.util.Date;

public interface FileObjectSearchRequest {

    int getStart();

    int getLimit();

    String getUploadedFilename();

    String getStoredFilename();

    String getProviderName();

    String getUploadedBy();

    Date getUploadedAtFrom();

    Date getUploadedAtTo();

}
