package in.clouthink.daas.fss.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@Configuration
@ComponentScan({"in.clouthink.daas.fss.rest", "in.clouthink.daas.fss.sample.spring.rest"})
@Import({FssConfiguration.class, ApplicationMvcConfigure.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(new Object[]{Application.class}, args);
	}

}
