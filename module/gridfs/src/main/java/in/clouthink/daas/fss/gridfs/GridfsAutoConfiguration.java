package in.clouthink.daas.fss.gridfs;

import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.gridfs.impl.FileStorageImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dz
 */
@Configuration
public class GridfsAutoConfiguration {

    @Bean(name = "gridfsStorage")
    public FileStorage gridfsStorage() {
        return new FileStorageImpl();
    }

}