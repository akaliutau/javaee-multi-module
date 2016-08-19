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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
public class TestDAOContext {

	@Autowired
	@Bean(name = "transactionManager")
	public DataSourceTransactionManager getTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);

	}

	@Autowired
	@Bean(name = "npJdbcTemplate")
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

	@Bean
	public DataSource dataSource() {

		return new EmbeddedDatabaseBuilder().generateUniqueName(true).ignoreFailedDrops(true)
				.setType(EmbeddedDatabaseType.H2).build();

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

}