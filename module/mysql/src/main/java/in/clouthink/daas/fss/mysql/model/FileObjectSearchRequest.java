package in.clouthink.daas.fss.mysql.model;

public interface FileObjectSearchRequest extends in.clouthink.daas.fss.domain.request.FileObjectSearchRequest {

    String getAttachedId();

    String getCategory();

    String getCode();

}
