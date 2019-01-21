package in.clouthink.daas.fss.alioss;

import in.clouthink.daas.fss.alioss.impl.FileStorageImpl;
import in.clouthink.daas.fss.alioss.service.OssService;
import in.clouthink.daas.fss.alioss.service.impl.OssServiceImpl;
import in.clouthink.daas.fss.core.FileStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliossAutoConfiguration {

    @Bean
    public OssService ossService() {
        return new OssServiceImpl();
    }

    @Bean
    public FileStorage fileStorage() {
        return new FileStorageImpl();
    }

}
