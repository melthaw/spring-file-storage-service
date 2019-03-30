package in.clouthink.daas.fss.local;

import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.local.impl.FileStorageImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalFileAutoConfiguration {

    @Bean(name = "localFileStorage")
    public FileStorage localFileStorage() {
        return new FileStorageImpl();
    }

}
