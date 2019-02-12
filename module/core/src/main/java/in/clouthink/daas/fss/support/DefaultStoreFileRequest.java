package in.clouthink.daas.fss.support;

import in.clouthink.daas.fss.core.StoreFileRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * The default implementation for the <code>FileStorageRequest</code>
 *
 * @author dz
 */
public class DefaultStoreFileRequest implements StoreFileRequest {

    private String originalFilename;

    private String prettyFilename;

    private String contentType;

    private String uploadedBy;

    private long size;

    private Map<String, String> attributes = new HashMap<String, String>();

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    @Override
    public String getPrettyFilename() {
        return prettyFilename;
    }

    public void setPrettyFilename(String prettyFilename) {
        this.prettyFilename = prettyFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Override
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DefaultStoreFileRequest{");
        sb.append("originalFilename='").append(originalFilename).append('\'');
        sb.append(", prettyFilename='").append(prettyFilename).append('\'');
        sb.append(", contentType='").append(contentType).append('\'');
        sb.append(", uploadedBy='").append(uploadedBy).append('\'');
        sb.append(", size=").append(size);
        sb.append(", attributes=").append(attributes);
        sb.append('}');
        return sb.toString();
    }
}
