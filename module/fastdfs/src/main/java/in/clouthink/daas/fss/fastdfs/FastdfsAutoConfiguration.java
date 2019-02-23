package in.clouthink.daas.fss.fastdfs;

import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.fastdfs.impl.FileStorageImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FastdfsAutoConfiguration {

    @Bean(name = "fastdfsStorage")
    public FileStorage fileStorage() {
        return new FileStorageImpl();
    }

}
