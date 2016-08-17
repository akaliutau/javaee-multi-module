package org.akalu.test.dao;



import javax.sql.DataSource;

import org.akalu.dao.DepartmentDAO;
import org.akalu.dao.DepartmentDAOImpl;
import org.akalu.dao.EmployeeDAO;
import org.akalu.dao.EmployeeDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This class configures Spring beans via java code.
 *
 * @author Alex Kalutov
 * @since Version 1.0
 */

@Configuration
@ComponentScan(basePackages = "org.akalu.test.dao")
@EnableTransactionManagement
public class TestDAOContext{
	
	@Autowired
	@Bean(name = "transactionManager")
	public DataSourceTransactionManager getTransactionManager(DataSource dataSource){
		return new DataSourceTransactionManager(dataSource);
		
	}
	
	@Autowired
	@Bean(name = "jdbcTemplate")
	public JdbcTemplate getJdbcTemplate(DataSource dataSource) {

		    
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}


	@Bean
	public DataSource dataSource(){

	    return  new EmbeddedDatabaseBuilder()
	    		.generateUniqueName(true)
	    		.ignoreFailedDrops(true) 
	    		.setType(EmbeddedDatabaseType.H2)
	    		.build(); 

	} 
	
	   @Autowired
	   @Bean(name = "depDao")
	   public DepartmentDAO getDepartmentDao(JdbcTemplate jdbcTemplate) {
	   	return new DepartmentDAOImpl(jdbcTemplate);
	   }

	   @Autowired
	   @Bean(name = "emplDao")
	   public EmployeeDAO getEmployeeDao(JdbcTemplate jdbcTemplate) {
	   	return new EmployeeDAOImpl(jdbcTemplate);
	   }


} 