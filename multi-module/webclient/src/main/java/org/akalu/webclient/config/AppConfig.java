package org.akalu.webclient.config;

import java.util.Locale;

import org.akalu.dataclient.DataClient;
import org.akalu.dataclient.DataClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "org.akalu.webclient", "org.akalu.dataclient" })
@PropertySource("classpath:server.properties")
// @ComponentScan("org.testtask.dataserver.DataController.class")
public class AppConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private Environment env;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/");
	}

	// @Bean configurations go here

	@Bean(name = "viewResolver")
	public InternalResourceViewResolver getViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	/*
	 * @Bean(name="dataServerSource") public DataServerSource
	 * getdataServerSource(){ return new
	 * DataServerSource(env.getProperty("ServerBaseURI"),
	 * env.getProperty("ServerName")); }
	 */

	@Autowired
	@Bean(name = "dataClient")
	public DataClient getDataClient() {
		return new DataClientImpl(env.getProperty("ServerHostURI") + env.getProperty("ServerBaseURI"));
	}

	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource getMessageSource() {
		ReloadableResourceBundleMessageSource MessageSource = new ReloadableResourceBundleMessageSource();
		MessageSource.setDefaultEncoding("UTF-8");
		MessageSource.setBasename("classpath:i18n/message");
		return MessageSource;
	}

	@Bean(name = "localeResolver")
	public SessionLocaleResolver getLocaleResolver() {
		SessionLocaleResolver LocaleResolver = new SessionLocaleResolver();
		LocaleResolver.setDefaultLocale(Locale.ENGLISH);

		return LocaleResolver;
	}

	/*
	 * @Bean public MappingJackson2HttpMessageConverter
	 * mappingJackson2HttpMessageConverter() {
	 * MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
	 * new MappingJackson2HttpMessageConverter();
	 * mappingJackson2HttpMessageConverter.setPrettyPrint(true); return
	 * mappingJackson2HttpMessageConverter; }
	 * 
	 * @Override public void
	 * configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	 * super.configureMessageConverters(converters);
	 * converters.add(mappingJackson2HttpMessageConverter()); //
	 * converters.add(mappingJackson2XmlHttpMessageConverter()); }
	 */
}
