package in.clouthink.daas.fss.sample;

import com.aliyun.oss.OSSClient;
import in.clouthink.daas.fss.alioss.spiImpl.FileStorageServiceImpl;
import in.clouthink.daas.fss.alioss.support.OssService;
import in.clouthink.daas.fss.alioss.support.impl.OssServiceImpl;
import in.clouthink.daas.fss.mongodb.spiImpl.FileObjectServiceImpl;
import in.clouthink.daas.fss.spi.FileObjectService;
import in.clouthink.daas.fss.spi.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author dz on 16/7/11.
 */
@Configuration
@EnableConfigurationProperties(AliossSampleProperties.class)
@ComponentScan({"in.clouthink.daas.fss.mongodb.model"})
@EnableMongoRepositories({"in.clouthink.daas.fss.mongodb.repository"})
public class FssConfiguration {

	@Bean
	@Autowired
	public OSSClient ossClient(AliossSampleProperties aliossSampleProperties) {
		return new OSSClient("http://" + aliossSampleProperties.getOssDomain(),
							 aliossSampleProperties.getKeyId(),
							 aliossSampleProperties.getSecret());
	}

	@Bean
	public OssService ossStrategy() {
		return new OssServiceImpl();
	}

	@Bean
	public FileObjectService fileObjectService() {
		return new FileObjectServiceImpl();
	}

	@Bean
	public FileStorageService fileStorageService() {
		return new FileStorageServiceImpl();
	}


}
