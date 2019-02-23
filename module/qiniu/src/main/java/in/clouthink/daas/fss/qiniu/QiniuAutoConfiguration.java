package in.clouthink.daas.fss.qiniu;

import in.clouthink.daas.fss.qiniu.impl.FileStorageImpl;
import in.clouthink.daas.fss.core.FileStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dz
 * @since 3
 */
@Configuration
public class QiniuAutoConfiguration {

    @Bean(name = "qiniuStorage")
    public FileStorage fileStorage() {
        return new FileStorageImpl();
    }

}
