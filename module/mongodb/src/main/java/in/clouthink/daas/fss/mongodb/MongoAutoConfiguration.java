package in.clouthink.daas.fss.mongodb;

import in.clouthink.daas.fss.domain.service.FileObjectService;
import in.clouthink.daas.fss.mongodb.impl.FileObjectServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
* @author dz
 */
@Configuration
@ComponentScan({"in.clouthink.daas.fss.mongodb.model"})
@EnableMongoRepositories({"in.clouthink.daas.fss.mongodb.repository"})
public class MongoAutoConfiguration {

	@Bean
	public FileObjectService fileObjectServiceMongoImpl() {
		return new FileObjectServiceImpl();
	}

}
