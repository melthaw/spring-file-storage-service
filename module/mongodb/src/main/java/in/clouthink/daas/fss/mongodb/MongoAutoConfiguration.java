package in.clouthink.daas.fss.mongodb;

import in.clouthink.daas.fss.domain.service.FileObjectService;
import in.clouthink.daas.fss.mongodb.impl.FileObjectServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author dz
 */
@Configuration
@EnableMongoRepositories({"in.clouthink.daas.fss.mongodb.repository"})
@EnableMongoAuditing
public class MongoAutoConfiguration {

    @Bean(name = "mongoFileObjectService")
    public FileObjectService fileObjectService() {
        return new FileObjectServiceImpl();
    }

}
