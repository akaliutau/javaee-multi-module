package org.akalu.test.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import org.akalu.model.Department;
import org.akalu.model.Employee;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.RowMapper;

/**
 * This is an utility class, which is used by EmployeeDAOTest and
 * DepartmentDAOTest classes.
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

public class DAOHelper {

	public static void dropTables(NamedParameterJdbcTemplate jdbcTemplate) {

		Map<String, Long> parameters = Collections.singletonMap("empty", 0L);

		final String query1 = "DROP TABLE IF EXISTS `departments`";
		jdbcTemplate.update(query1, parameters);
		final String query2 = "DROP TABLE IF EXISTS `employees`";
		jdbcTemplate.update(query2, parameters);

	}

	public static void createTables(NamedParameterJdbcTemplate jdbcTemplate) {

		Map<String, Long> parameters = Collections.singletonMap("empty", 0L);

		final String query1 = "CREATE TABLE `departments`(" + "`department_id` int(8) NOT NULL AUTO_INCREMENT,"
				+ " `title` varchar(255), " + "PRIMARY KEY (`department_id`))";
		jdbcTemplate.update(query1, parameters);

		final String query2 = "CREATE TABLE `employees` (" + "`employee_id` int(8) NOT NULL AUTO_INCREMENT,"
				+ "`firstname` varchar(30) NOT NULL, " + "`secondname` varchar(30) NOT NULL,"
				+ "`surname` varchar(50) NOT NULL," + "`dob` date NOT NULL," + "`salary` double NOT NULL,"
				+ "`department_id` int(8) NOT NULL," + "PRIMARY KEY (`employee_id`)," + "FOREIGN KEY (`department_id`) "
				+ "REFERENCES `departments` (`department_id`))";

		jdbcTemplate.update(query2, parameters);

	}

	public static Department findDep(NamedParameterJdbcTemplate jdbcTemplate, Long id) {
		String query = "select department_id, title from departments where department_id=:department_id";

		Map<String, Long> namedParameters = Collections.singletonMap("department_id", id);

		return jdbcTemplate.queryForObject(query, namedParameters, new RowMapper<Department>() {

			@Override
			public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
				Department d = new Department();
				d.setId(rs.getLong("department_id"));
				d.setTitle(rs.getString("title"));
				return d;
			}
		});

	}

	public static Employee findEmpl(NamedParameterJdbcTemplate jdbcTemplate, Long id) {
		String query = "select employee_id, firstname, secondname, surname, dob, salary, department_id "
				+ "from employees where employee_id=:employee_id";

		Map<String, Long> parameters = Collections.singletonMap("employee_id", id);

		return jdbcTemplate.queryForObject(query, parameters, new RowMapper<Employee>() {

			@Override
			public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
				Employee e = new Employee();
				e.setId(rs.getLong("employee_id"));
				e.setFirstName(rs.getString("firstname"));
				e.setSecondName(rs.getString("secondname"));
				e.setSurname(rs.getString("surname"));
				e.setSalary(rs.getDouble("salary"));
				e.setDob(rs.getDate("dob"));
				e.setDepId(rs.getLong("department_id"));
				return e;
			}
		});

	}

	public static Long saveDep(SimpleJdbcInsert insertDepartment, final Department dep) {

		Map<String, String> namedParameters = Collections.singletonMap("title", dep.getTitle());

		return insertDepartment.executeAndReturnKey(namedParameters).longValue();

	}

	public static Long saveEmpl(SimpleJdbcInsert insertEmployee, final Employee em) {

		SqlParameterSource params = new MapSqlParameterSource().addValue("firstname", em.getFirstName())
				.addValue("secondname", em.getSecondName()).addValue("surname", em.getSurname())
				.addValue("dob", new java.sql.Date(em.getDob().toInstant().toEpochMilli()))
				.addValue("salary", em.getSalary()).addValue("department_id", em.getDepId());

		return insertEmployee.executeAndReturnKey(params).longValue();

	}

}
