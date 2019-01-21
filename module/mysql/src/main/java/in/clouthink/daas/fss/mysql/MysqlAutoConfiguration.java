package in.clouthink.daas.fss.mysql;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
* @author dz
 */
@Configuration
@ComponentScan({"in.clouthink.daas.fss.mysql"})
@EnableJpaRepositories({"in.clouthink.daas.fss.mysql.repository"})
public class MysqlAutoConfiguration {

}
