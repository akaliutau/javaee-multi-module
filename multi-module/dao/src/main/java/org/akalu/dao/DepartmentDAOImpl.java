package org.akalu.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.akalu.model.Department;

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
public class DepartmentDAOImpl implements DepartmentDAO {

	private static final Logger logger = Logger.getLogger(DepartmentDAOImpl.class);

	private static Long depSize = null;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	private SimpleJdbcInsert insertDepartment;

	public DepartmentDAOImpl() {
	}

	public DepartmentDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Method uses join clause and aggregation function AVG to retrieve a list
	 * of departments with average salary.
	 * <p>
	 * Before actual invoking query a simple validation of the arguments for
	 * subList method is performed (in order to avoid IndexOutofRange
	 * exception.)
	 * <p>
	 * Method returns empty list in case of wrong arguments NB: in general,
	 * index of record is not equal to its id
	 * 
	 * @param first
	 *            - index of the first record in the list
	 * @param amount
	 *            - amount of records
	 * @return List<Department>
	 */

	@Override
	@Transactional
	public List<Department> list(int first, int amount) {

		if (depSize == null)
			size();

		List<Department> listDep = new ArrayList<Department>();
		if (first < 0)
			first = 0;

		if (first > depSize)
			return listDep;
		if (amount < 0)
			return listDep;
		if (first + amount > depSize)
			amount = (int) (depSize - first);

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("first", first).addValue("amount",
				amount);

		try {
			listDep = jdbcTemplate.query(Queries.GET_DEPARTMENTS_LIST, namedParameters, new RowMapper<Department>() {

				@Override
				public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
					Department d = new Department();
					d.setId(rs.getLong("department_id"));
					d.setTitle(rs.getString("title"));
					Object avgsal = rs.getObject("asalary");
					if (avgsal == null) {
						d.setAvrSalary(null);
					} else {
						d.setAvrSalary(Double.parseDouble(String.valueOf(avgsal)));
					}
					return d;
				}
			});

			logger.debug("Return list of departments, records:" + listDep.size());

			return listDep;

		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return null;
		}

	}

	@Override
	@Transactional
	public Boolean update(Department dep) {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("title", dep.getTitle())
				.addValue("department_id", dep.getId());

		try {
			int res = jdbcTemplate.update(Queries.UPDATE_DEPARTMENT, namedParameters);
			logger.debug("Update department with id=" + dep.getId());

			return (res == 1) ? true : false;

		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return false;
		}

	}

	/**
	 * This method creates a new department record in the database.
	 * 
	 * @return id of new department
	 */

	@Override
	@Transactional
	public Long addnew(final Department dep) {

		Map<String, String> namedParameters = Collections.singletonMap("title", dep.getTitle());

		try {
			final Number newId = insertDepartment.executeAndReturnKey(namedParameters);

			final long id = newId.longValue();
			if (id > 0)
				depSize++;
			logger.debug("Add new department with id=" + id);

			return id;

		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return -1L;
		}

	}

	/**
	 * Deleting a single department record from database.
	 * <p>
	 * Before delete a verification is conducted are there any employees in
	 * department. In the case of positive answer method returns without
	 * invoking actual database operation.
	 * 
	 * 
	 */

	@Override
	@Transactional
	public Boolean delete(Long id) {

		Map<String, Long> namedParameters = Collections.singletonMap("department_id", id);

		try {
			Long emplCount = jdbcTemplate.queryForObject(Queries.COUNT_EMPLOYEES, namedParameters, Long.class);

			if (emplCount != 0)
				return false;

			namedParameters = Collections.singletonMap("department_id", id);

			int rowsAffected = jdbcTemplate.update(Queries.DELETE_DEPARTMENT, namedParameters);

			if (rowsAffected > 0) {
				depSize--;
				logger.debug("Delete department with id=" + id);

				return true;
			}

			return false;

		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return false;
		}

	}

	/**
	 * This method uses RowMapper anonymous class for mapping ResultSet values
	 * on Department's fields.
	 * 
	 * 
	 * 
	 */
	@Override
	@Transactional
	public Department getDepbyId(Long id) {

		Map<String, Long> namedParameters = Collections.singletonMap("department_id", id);

		try {
			Department dep = jdbcTemplate.queryForObject(Queries.GET_DEPARTMENT, namedParameters,
					new RowMapper<Department>() {

						@Override
						public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
							Department d = new Department();
							d.setId(rs.getLong("department_id"));
							d.setTitle(rs.getString("title"));
							return d;
						}
					});
			logger.debug("Return department with id=" + dep.getId());

			return dep;
		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return null;
		}
	}

	/**
	 * This method uses aggregation function COUNT to retrieve the number of
	 * records in the departments table.
	 * 
	 */

	@Override
	@Transactional
	public Long size() {

		Map<String, Long> namedParameters = Collections.singletonMap("department_id", 0L);

		if (depSize != null)
			return depSize;

		try {

			depSize = jdbcTemplate.queryForObject(Queries.COUNT_DEPARTMENTS, namedParameters, Long.class);
			logger.debug("Return size from departments:" + depSize);
			return depSize;

		} catch (DataAccessException e) {
			logger.debug("DAE error");
			return -1L;
		}
	}
}
