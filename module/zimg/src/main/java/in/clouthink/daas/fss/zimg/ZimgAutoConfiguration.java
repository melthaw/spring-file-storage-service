package in.clouthink.daas.fss.zimg;

import in.clouthink.daas.fss.zimg.impl.FileStorageImpl;
import in.clouthink.daas.fss.core.FileStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZimgAutoConfiguration {

    @Bean
    public FileStorage fileStorage() {
        return new FileStorageImpl();
    }

}
