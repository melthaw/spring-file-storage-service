package in.clouthink.daas.fss.hwobs;

import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.hwobs.impl.FileStorageImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HwObsAutoConfiguration {

    @Bean
    public FileStorage fileStorage() {
        return new FileStorageImpl();
    }

}
