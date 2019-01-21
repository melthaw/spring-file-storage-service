package in.clouthink.daas.fss.mysql.model;

import org.springframework.data.domain.Pageable;

public interface FileObjectSearchRequest extends in.clouthink.daas.fss.domain.request.FileObjectSearchRequest {

    String getBizId();

    String getCategory();

    String getCode();

    String getSort();

    Pageable toPageable();

}
