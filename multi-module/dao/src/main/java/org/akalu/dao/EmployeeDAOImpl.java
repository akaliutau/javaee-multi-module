package org.akalu.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.akalu.model.Employee;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * An utility class to perform basic CRUD operations in database. In all queries
 * a standard SQL is used.
 * 
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

@Repository
@Transactional(transactionManager = "transactionManager")
public class EmployeeDAOImpl implements EmployeeDAO {

	private static final Logger logger = Logger.getLogger(EmployeeDAOImpl.class);

	private static Long emplSize = null;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	private SimpleJdbcInsert insertEmployee;

	public EmployeeDAOImpl() {
	}

	public EmployeeDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	@Transactional
	public List<Employee> list(int first, int n, Long depid) {

		size(depid);

		List<Employee> listEmpl = new ArrayList<Employee>();
		if (first < 0)
			first = 0;
		// return empty list in case of wrong arguments
		if (first > emplSize)
			return listEmpl;
		if (n < 0)
			return listEmpl;
		if (first + n > emplSize)
			n = (int) (emplSize - first);

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("first", first).addValue("amount", n)
				.addValue("depid", depid);

		try {
			listEmpl = jdbcTemplate.query(Queries.GET_EMPLOYEES_LIST, namedParameters, new EmployeeMapper());

			logger.debug("Return list of employees, records:" + listEmpl.size());

			return listEmpl;

		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return null;
		}

	}

	@Override
	@Transactional
	public Boolean update(Employee em) {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("firstname", em.getFirstName())
				.addValue("secondname", em.getSecondName()).addValue("surname", em.getSurname())
				.addValue("dob", em.getDob()).addValue("salary", em.getSalary())
				.addValue("department_id", em.getDepId()).addValue("employee_id", em.getId());

		try {
			int res = jdbcTemplate.update(Queries.UPDATE_EMPLOYEE, namedParameters);

			logger.debug("Update Employee with id=" + em.getId());

			return (res == 1) ? true : false;

		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return false;
		}

	}

	@Override
	@Transactional
	public Long addnew(final Employee em) {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("firstname", em.getFirstName())
				.addValue("secondname", em.getSecondName()).addValue("surname", em.getSurname())
				.addValue("dob", em.getDob()).addValue("salary", em.getSalary())
				.addValue("department_id", em.getDepId()).addValue("employee_id", em.getId());

		try {
			final Number newId = insertEmployee.executeAndReturnKey(namedParameters);

			final long id = newId.longValue();
			logger.debug("Add new Employee with id=" + newId);

			return id;

		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return -1L;
		}

	}

	/**
	 * Deleting a single employee record from database. In case of success
	 * method returns true.
	 * 
	 * 
	 */

	@Override
	@Transactional
	public Boolean delete(Long id) {

		Map<String, Long> namedParameters = Collections.singletonMap("employee_id", id);

		try {

			int rowsAffected = jdbcTemplate.update(Queries.DELETE_EMPLOYEE, namedParameters);
			logger.debug("delete Employee with id=" + id);

			return (rowsAffected > 0) ? true : false;
		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return false;
		}

	}

	@Override
	@Transactional
	public Employee getEmplbyId(Long id) {

		Map<String, Long> namedParameters = Collections.singletonMap("employee_id", id);

		try {
			Employee em = jdbcTemplate.queryForObject(Queries.GET_EMPLOYEE, namedParameters, new EmployeeMapper());

			logger.debug("return Employee with id=" + id);

			return em;
		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return null;
		}
	}

	/**
	 * This method searches of employees born in particular date.
	 * 
	 * @param dob1
	 *            - Date of Birth
	 */

	@Override
	@Transactional
	public List<Employee> search1(Date dob1) {

		Map<String, Date> namedParameters = Collections.singletonMap("dob", dob1);

		List<Employee> listEmpl = new ArrayList<Employee>();

		try {
			listEmpl = jdbcTemplate.query(Queries.SEACH_EMPLOYEE_1, namedParameters, new EmployeeMapper());

			return listEmpl;

		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return null;
		}
	}

	/**
	 * This method searches of employees born between two particular dates.
	 * 
	 * @param dob1
	 *            - first date (lower boundary)
	 * @param dob2
	 *            - second date (upper boundary)
	 */

	@Override
	@Transactional
	public List<Employee> search2(Date dob1, Date dob2) {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("dob1", dob1).addValue("dob2", dob2);

		List<Employee> listEmpl = new ArrayList<Employee>();

		try {
			listEmpl = jdbcTemplate.query(Queries.SEACH_EMPLOYEE_2, namedParameters, new EmployeeMapper());

			return listEmpl;

		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return null;
		}

	}

	@Override
	@Transactional
	public Long size(Long id) {

		Map<String, Long> namedParameters = Collections.singletonMap("department_id", id);

		try {

			emplSize = jdbcTemplate.queryForObject(Queries.COUNT_EMPLOYEES, namedParameters, Long.class);
			logger.debug("Return size from employees:" + emplSize);

			return emplSize;

		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return -1L;
		}

	}

	/**
	 * Private inner class, implementation of the RowMapper<T> interface. Used
	 * for mapping ResultSet values on Employee's fields
	 * 
	 */

	private class EmployeeMapper implements RowMapper<Employee> {

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
	}

	/**
	 * A converter between different Date types.
	 * 
	 * @param dob
	 *            - Date of type java.util.Date
	 * @return dob - Date of type java.sql.Date
	 */

	private java.sql.Date util2SqlDate(Date dob) {

		return new java.sql.Date(dob.toInstant().toEpochMilli());

	}
}
