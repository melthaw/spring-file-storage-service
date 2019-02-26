package in.clouthink.daas.fss.mysql;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author dz
 */
@Configuration
@ComponentScan({"in.clouthink.daas.fss.mysql"})
@EnableJpaRepositories({"in.clouthink.daas.fss.mysql.repository"})
@EnableTransactionManagement
public class MysqlAutoConfiguration {

}
