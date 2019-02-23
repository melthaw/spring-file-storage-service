package in.clouthink.daas.fss.mysql.model;

public interface FileObjectSearchRequest extends in.clouthink.daas.fss.domain.request.FileObjectSearchRequest {

    String getBizId();

    String getCategory();

    String getCode();

}
