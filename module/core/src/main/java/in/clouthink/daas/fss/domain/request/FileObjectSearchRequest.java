package in.clouthink.daas.fss.domain.request;

import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface FileObjectSearchRequest {

    String getUploadedFilename();

    String getStoredFilename();

    String getProviderName();

    String getUploadedBy();

    Date getUploadedAtFrom();

    Date getUploadedAtTo();

    int getStart();

    int getLimit();

    /**
     * If the format is wrong , it will be ignored.
     *
     * @return sort format "entity-field-name: DESC|ASC"
     */
    String getSortExpr();

    Pageable toPageable();

}
