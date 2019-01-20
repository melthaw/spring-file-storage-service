package in.clouthink.daas.fss.domain.request;

import java.util.Date;

public interface FileObjectSearchRequest {

    int getStart();

    int getLimit();

    String getUploadedFilename();

    String getStoredFilename();

    String getProviderName();

    Date getUploadedAtFrom();

    Date getUploadedAtTo();

    String getUploadedBy();

}
