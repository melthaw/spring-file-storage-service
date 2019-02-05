package in.clouthink.daas.fss.webdav.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author dz
 */
public class DefaultWebDavProperties implements WebDavProperties, InitializingBean {

    private String username;

    private String password;

    private String uploadEndpoint;

    private String downloadEndpoint;

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
        Assert.notNull(username);
        Assert.notNull(password);
        Assert.notNull(uploadEndpoint);
        Assert.notNull(downloadEndpoint);
    }

}
