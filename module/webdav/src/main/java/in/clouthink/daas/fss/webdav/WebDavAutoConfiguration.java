package in.clouthink.daas.fss.webdav;

import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.webdav.impl.FileStorageImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDavAutoConfiguration {

    @Bean
    public FileStorage fileStorage() {
        return new FileStorageImpl();
    }

}
