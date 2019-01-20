package in.clouthink.daas.fss.mongodb;

import in.clouthink.daas.fss.mongodb.service.GridFSService;
import in.clouthink.daas.fss.mongodb.service.impl.GridFSServiceImpl;
import in.clouthink.daas.fss.domain.service.FileObjectService;
import in.clouthink.daas.fss.core.FileStorageService;
import in.clouthink.daas.fss.mongodb.spiImpl.FileObjectServiceImpl;
import in.clouthink.daas.fss.mongodb.spiImpl.FileStorageServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
* @author dz
 */
@Configuration
@ComponentScan({"in.clouthink.daas.fss.mongodb.model"})
@EnableMongoRepositories({"in.clouthink.daas.fss.mongodb.repository"})
public class MongoModuleConfiguration {

	@Bean
	public FileObjectService fileObjectServiceMongoImpl() {
		return new FileObjectServiceImpl();
	}

	@Bean
	@DependsOn("gridFSServiceImpl")
	public FileStorageService fileStorageServiceMongoImpl() {
		return new FileStorageServiceImpl();
	}

	@Bean
	public GridFSService gridFSServiceImpl() {
		return new GridFSServiceImpl();
	}

}