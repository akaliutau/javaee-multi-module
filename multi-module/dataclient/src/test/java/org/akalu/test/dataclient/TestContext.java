package org.akalu.test.dataclient;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * The main purpose of this class is to configure Spring beans via java code.
 * <p>
 * Bean {@code MappingJackson2HttpMessageConverter} is used for conversion from
 * POJO to data in JSON format, and vice versa.
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

@Configuration
@ComponentScan(basePackages = { "org.akalu.test.dataclient" })
public class TestContext extends WebMvcConfigurerAdapter {

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setPrettyPrint(true);
		return mappingJackson2HttpMessageConverter;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		converters.add(mappingJackson2HttpMessageConverter());
		// converters.add(mappingJackson2XmlHttpMessageConverter());
	}

}
