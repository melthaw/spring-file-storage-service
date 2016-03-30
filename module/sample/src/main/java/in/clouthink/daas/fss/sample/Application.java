package in.clouthink.daas.fss.sample;

import in.clouthink.daas.fss.mongodb.MongoModuleConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(value = "in.clouthink.daas.fss")
@SpringBootApplication
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@Import({ApplicationMvcConfigure.class, MongoModuleConfiguration.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(new Object[]{Application.class}, args);
	}

}
