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

    private String adminEndpoint;

    private String infoEndpoint;

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
    public String getAdminEndpoint() {
        return adminEndpoint;
    }

    public void setAdminEndpoint(String adminEndpoint) {
        this.adminEndpoint = adminEndpoint;
    }

    @Override
    public String getInfoEndpoint() {
        return infoEndpoint;
    }

    public void setInfoEndpoint(String infoEndpoint) {
        this.infoEndpoint = infoEndpoint;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(uploadEndpoint, "Please specify the zimg upload endpoint");
        Assert.notNull(downloadEndpoint, "Please specify the zimg download endpoint");
        Assert.notNull(adminEndpoint, "Please specify the zimg admin endpoint");
        Assert.notNull(infoEndpoint, "Please specify the zimg info endpoint");
    }

}
