package org.akalu.dataserver.config;

import java.util.List;
import org.akalu.dao.DepartmentDAO;
import org.akalu.dao.DepartmentDAOImpl;
import org.akalu.dao.EmployeeDAO;
import org.akalu.dao.EmployeeDAOImpl;
import org.apache.tomcat.jdbc.pool.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * Configuration class to perform javaconfigure of the Spring beans. With this
 * approach there is no need in XML-based config files.
 * 
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "org.akalu.dao", "org.akalu.service", "org.akalu.dataserver" })
@PropertySource("classpath:db/database.properties")
@EnableTransactionManagement
public class ApplicationContextConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private Environment env;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/");
	}

	@Autowired
	@Bean(name = "transactionManager")
	public DataSourceTransactionManager getTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);

	}

	@Autowired
	@Bean(name = "jdbcTemplate")
	public NamedParameterJdbcTemplate getJdbcTemplate(DataSource dataSource) {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return jdbcTemplate;
	}

	@Autowired
	@Bean(name = "insertDepartment")
	public SimpleJdbcInsert getInsertDepartment(DataSource dataSource) {

		SimpleJdbcInsert insertDepartment = new SimpleJdbcInsert(dataSource).withTableName("departments")
				.usingGeneratedKeyColumns("id");
		return insertDepartment;
	}

	@Autowired
	@Bean(name = "insertEmployee")
	public SimpleJdbcInsert getInsertEmployee(DataSource dataSource) {

		SimpleJdbcInsert insertEmployee = new SimpleJdbcInsert(dataSource).withTableName("employees")
				.usingGeneratedKeyColumns("id");
		return insertEmployee;
	}

	@Autowired
	@Bean(name = "depDao")
	public DepartmentDAO getDepartmentDao(NamedParameterJdbcTemplate jdbcTemplate) {
		return new DepartmentDAOImpl(jdbcTemplate);
	}

	@Autowired
	@Bean(name = "emplDao")
	public EmployeeDAO getEmployeeDao(NamedParameterJdbcTemplate jdbcTemplate) {
		return new EmployeeDAOImpl(jdbcTemplate);
	}

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));

		return dataSource;
	}

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
