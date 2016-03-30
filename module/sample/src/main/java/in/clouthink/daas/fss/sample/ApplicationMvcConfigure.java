package in.clouthink.daas.fss.sample;

import in.clouthink.daas.fss.sample.spring.mvc.ResultResponseHandlerMethodProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
public class ApplicationMvcConfigure extends WebMvcConfigurerAdapter {

	@Override
	public void addReturnValueHandlers(final List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		returnValueHandlers.add(new ResultResponseHandlerMethodProcessor(messageConverters));
	}

}
