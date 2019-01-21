package in.clouthink.daas.fss.mongodb.model;

import in.clouthink.daas.fss.mysql.model.FileObjectSearchRequest;
import in.clouthink.daas.fss.support.FileObjectSearchParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

public class DefaultFileObjectSearchRequest extends FileObjectSearchParam implements FileObjectSearchRequest {

    private String bizId;

    private String category;

    private String code;

    private String sort;

    @Override
    public String getBizId() {
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

    @Override
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public Pageable toPageable() {
        return StringUtils.isEmpty(getSort()) ? new PageRequest(getStart(), getLimit()) :
                new PageRequest(getStart(), getLimit(), new Sort(getSort()));
    }

}
