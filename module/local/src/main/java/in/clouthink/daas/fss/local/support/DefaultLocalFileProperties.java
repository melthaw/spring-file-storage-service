package in.clouthink.daas.fss.local.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class DefaultLocalFileProperties implements LocalFileProperties, InitializingBean {

    private String storePath;

    @Override
    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(storePath);
    }

}
