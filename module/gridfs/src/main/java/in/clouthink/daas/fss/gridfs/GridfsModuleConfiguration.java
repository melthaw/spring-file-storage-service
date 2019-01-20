package in.clouthink.daas.fss.gridfs;

import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.gridfs.impl.FileStorageImpl;
import in.clouthink.daas.fss.mongodb.service.GridFSService;
import in.clouthink.daas.fss.mongodb.service.impl.GridFSServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dz
 */
@Configuration
public class GridfsModuleConfiguration {

    @Bean
    public FileStorage gridfsStorage() {
        return new FileStorageImpl();
    }

    @Bean
    public GridFSService gridFSServiceImpl() {
        return new GridFSServiceImpl();
    }

}