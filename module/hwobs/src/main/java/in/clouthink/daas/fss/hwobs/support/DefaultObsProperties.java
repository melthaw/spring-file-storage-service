package in.clouthink.daas.fss.hwobs.support;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vanish
 */
@Data
public class DefaultObsProperties implements ObsProperties, InitializingBean {

    private String keyId;

    private String keySecret;

    private String securityToken;

    private String endpoint;

    private String defaultBucket;

    private Map<String, String> buckets = new HashMap<String, String>();


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(keyId);
        Assert.notNull(keySecret);
        Assert.notNull(endpoint);
        Assert.notNull(defaultBucket);
    }

}
