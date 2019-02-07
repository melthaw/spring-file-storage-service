package in.clouthink.daas.fss.glusterfs;

import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.glusterfs.impl.FileStorageImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlusterfsAutoConfiguration {

    @Bean(name = "glusterfsStorage")
    public FileStorage fileStorage() {
        return new FileStorageImpl();
    }

}
