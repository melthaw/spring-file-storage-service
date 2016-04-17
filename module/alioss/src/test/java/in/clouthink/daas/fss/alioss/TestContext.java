package in.clouthink.daas.fss.alioss;

import in.clouthink.daas.fss.alioss.support.OssConfig;
import in.clouthink.daas.fss.alioss.support.OssConfigImpl;
import org.springframework.context.annotation.Bean;

/**
 * Created by LiangBin on 16/4/17.
 */
public class TestContext extends AliossModuleConfiguration {
    
    @Bean
    public OssConfig ossConfig() {
        return new OssConfigImpl();
    }
}
