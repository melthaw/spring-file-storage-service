package in.clouthink.daas.fss.webdav.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author dz
 */
public class DefaultWebDavProperties implements WebDavProperties, InitializingBean {

    private String username;

    private String password;

    private String endpoint;

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
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(username);
        Assert.notNull(password);
        Assert.notNull(endpoint);
    }

}
