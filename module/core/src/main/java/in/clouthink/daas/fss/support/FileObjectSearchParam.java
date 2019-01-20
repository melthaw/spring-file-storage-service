package in.clouthink.daas.fss.support;

import in.clouthink.daas.fss.domain.request.FileObjectSearchRequest;

import java.util.Date;

public class FileObjectSearchParam implements FileObjectSearchRequest {

    int start = 0;

    // limit to 1, max to 100
    int limit = 20;

    String uploadedFilename;

    String storedFilename;

    String providerName;

    Date uploadedAtFrom;

    Date uploadedAtTo;

    String uploadedBy;

    @Override
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit < 1) {
            limit = 1;
        }
        if (limit > 100) {
            limit = 100;
        }
        this.limit = limit;
    }

    @Override
    public String getUploadedFilename() {
        return uploadedFilename;
    }

    public void setUploadedFilename(String uploadedFilename) {
        this.uploadedFilename = uploadedFilename;
    }

    @Override
    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public Date getUploadedAtFrom() {
        return uploadedAtFrom;
    }

    public void setUploadedAtFrom(Date uploadedAtFrom) {
        this.uploadedAtFrom = uploadedAtFrom;
    }

    @Override
    public Date getUploadedAtTo() {
        return uploadedAtTo;
    }

    public void setUploadedAtTo(Date uploadedAtTo) {
        this.uploadedAtTo = uploadedAtTo;
    }

    @Override
    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
