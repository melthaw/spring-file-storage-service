package in.clouthink.daas.fss.s3;

import in.clouthink.daas.fss.s3.impl.FileStorageImpl;
import in.clouthink.daas.fss.core.FileStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3AutoConfiguration {

    @Bean(name = "s3Storage")
    public FileStorage fileStorage() {
        return new FileStorageImpl();
    }

}
