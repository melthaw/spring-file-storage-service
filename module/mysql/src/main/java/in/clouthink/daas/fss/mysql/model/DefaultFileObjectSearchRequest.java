package in.clouthink.daas.fss.mysql.model;

import in.clouthink.daas.fss.support.FileObjectSearchParam;

public class DefaultFileObjectSearchRequest extends FileObjectSearchParam implements in.clouthink.daas.fss.mysql.model.FileObjectSearchRequest {

    private String bizId;

    private String category;

    private String code;

    @Override
    public String getAttachedId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
