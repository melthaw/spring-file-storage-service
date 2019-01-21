package in.clouthink.daas.fss.mongodb;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
* @author dz
 */
@Configuration
@ComponentScan({"in.clouthink.daas.fss.mongodb"})
@EnableMongoRepositories({"in.clouthink.daas.fss.mongodb.repository"})
public class MongoAutoConfiguration {

}
