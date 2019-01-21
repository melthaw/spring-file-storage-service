package in.clouthink.daas.fss.zimg.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
public class DefaultZimgProperties implements ZimgProperties, InitializingBean {

    private String uploadEndpoint;

    private String downloadEndpoint;

    @Override
    public String getUploadEndpoint() {
        return uploadEndpoint;
    }

    public void setUploadEndpoint(String uploadEndpoint) {
        this.uploadEndpoint = uploadEndpoint;
    }

    @Override
    public String getDownloadEndpoint() {
        return downloadEndpoint;
    }

    public void setDownloadEndpoint(String downloadEndpoint) {
        this.downloadEndpoint = downloadEndpoint;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(uploadEndpoint);
        Assert.notNull(downloadEndpoint);
    }

}
