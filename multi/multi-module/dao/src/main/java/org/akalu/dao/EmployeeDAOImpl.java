package org.akalu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.akalu.model.Employee;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * An utility class to perform basic CRUD operations in database.
 * In all queries a  standard SQL  is used.
 * 
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

	private static final Logger logger = Logger.getLogger(EmployeeDAOImpl.class);

	
	private static Long emplSize = null;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public EmployeeDAOImpl() {
	}
	
	public EmployeeDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Employee> list(int first, int n, Long depid) {
	
		String query = "select e.employee_id, e.firstname, e.secondname, e.surname, " +
		"e.dob, e.salary, e.department_id " +
		"from employees e " +
		"where e.department_id=?";
		

		size(depid);
		
		List<Employee> listEmpl = new ArrayList<Employee>();
		if (first < 0) first = 0;
		// return empty list in case of wrong arguments 
		if (first > emplSize) return listEmpl;
		if (n<0) return listEmpl; 
		if (first + n > emplSize) n = (int) (emplSize-first); 

		
		try{
		List<Map<String,Object>> eRows = 
				jdbcTemplate.queryForList(query, new Object[]{depid}).subList(first, first+n);
		
		for(Map<String,Object> eRow : eRows){
			Employee e = Map2Employee(eRow);

			listEmpl.add(e);
		}
		logger.debug("Return list of employees, records:" + listEmpl.size());

		return listEmpl;
		
		}catch (DataAccessException e)
		{ 
			logger.debug("DAE error");
			return null;
		}
		
	}

	@Override
	public Boolean update(Employee em) {
		String query = "update employees set firstname=?, secondname=?, surname=?, " +
		"dob=?, salary=?, department_id=? " +
		"where employee_id=?";

		Object[] args = new Object[] {em.getFirstName(), em.getSecondName(), em.getSurname(),
				em.getDob(), em.getSalary(), em.getDepId(), em.getId()};
		
		try{
		int res = jdbcTemplate.update(query, args); 
		
		logger.debug("Update Employee with id=" + em.getId());

		
		return (res == 1)?true:false;
		
		}catch (DataAccessException e)
		{
			logger.debug("DAE error");
			return false;
		}
			
	}

	@Override
	public Long addnew(final Employee em) {
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			   @Override
			   public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
			      final PreparedStatement ps =
			    		connection.prepareStatement(
			    		"INSERT INTO `employees`(`firstname`, `secondname`, `surname`,"+
			    		" `dob`, `salary`, `department_id`) " +
			    		"VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			        ps.setString(1, em.getFirstName());
			        ps.setString(2, em.getSecondName());
			        ps.setString(3, em.getSurname());
			        ps.setDate(4, util2SqlDate(em.getDob()));
			        ps.setDouble(5, em.getSalary());
			        ps.setLong(6, em.getDepId());
			        return ps;
			     }
			};

			final KeyHolder holder = new GeneratedKeyHolder();

			try{
		    jdbcTemplate.update(psc, holder);

		    final long newId = holder.getKey().longValue();
			logger.debug("Add new Employee with id=" + newId);

		    return newId;
		    
			}catch (DataAccessException e)
			{
				logger.debug("DAE error");
				return -1L;
			}
			
	}
	
	/**
	 * Deleting a single employee record from database.
	 * In case of success method returns true.  
	 * 
	 * 
	 */


	@Override
	public Boolean delete(Long id) {
		String query = "delete from employees where employee_id=?";

		try{

		int rowsAffected = jdbcTemplate.update(query, id);
		logger.debug("delete Employee with id=" + id);


		return (rowsAffected > 0)?true:false;
		}catch (DataAccessException e)
		{
			logger.debug("DAE error");
			return false;
		}
 
	}

	@Override
	public Employee getEmplbyId(Long id) {
		String query = "select employee_id, firstname, secondname, surname, dob, salary, department_id " +
					"from employees where employee_id=?";
		
		try{
			Employee em = jdbcTemplate.queryForObject(query, new Object[]{id},
			new RowMapper<Employee>(){

			@Override
			public Employee mapRow(ResultSet rs, int rowNum)
					throws SQLException {
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

		logger.debug("return Employee with id=" + id);

		return em; 	
		}catch (DataAccessException e)
		{
			logger.debug("DAE error");
			return null;
		}
	}

	@Override
	public List<Employee> search1(Date dob1) {
		String query = "select e.employee_id, e.firstname, e.secondname, e.surname, " +
		"e.dob, e.salary, e.department_id " +
		"from employees e " +
		"where e.dob=?";
		
		List<Employee> listEmpl = new ArrayList<Employee>();
		
		try{
		List<Map<String,Object>> eRows = 
				jdbcTemplate.queryForList(query, new Object[]{dob1});
		
		for(Map<String,Object> eRow : eRows){
			Employee e = Map2Employee(eRow);

			listEmpl.add(e);
		}
		return listEmpl;
		
		}catch (DataAccessException e)
		{ 
			logger.debug("DAE error");
			return null;
		}
	}

	@Override
	public List<Employee> search2(Date dob1, Date dob2) {
		String query = "select e.employee_id, e.firstname, e.secondname, e.surname, " +
		"e.dob, e.salary, e.department_id " +
		"from employees e " +
		"where e.dob > ? and e.dob < ?";
		
		List<Employee> listEmpl = new ArrayList<Employee>();
		
		try{
		List<Map<String,Object>> eRows = 
				jdbcTemplate.queryForList(query, new Object[]{dob1, dob2});
		
		for(Map<String,Object> eRow : eRows){
			Employee e = Map2Employee(eRow);
			listEmpl.add(e);
		}
		return listEmpl;
		
		}catch (DataAccessException e)
		{ 
			logger.debug("DAE error");
			return null;
		}

		}

	@Override
	public Long size(Long id) {
		String query = "select count(*) from employees e " +
				"where e.department_id=?";

		try{
			
			emplSize = jdbcTemplate.queryForObject(query, new Object[]{id}, Long.class);
			logger.debug("Return size from employees:" + emplSize);

			return emplSize;
		
		}catch (DataAccessException e)
		{
			logger.debug("DAE error");
			return -1L;
		}

	}
	
	/**
	 * This is an utility method to map retrieved values onto Employee object
	 * 
	 * @param eRow
	 * @return Employee object
	 */
	
	private Employee Map2Employee(Map<String,Object> eRow){
		
		Employee e = new Employee();
		e.setId(Long.parseLong(String.valueOf(eRow.get("employee_id"))));
		e.setFirstName(String.valueOf(eRow.get("firstname")));
		e.setSecondName(String.valueOf(eRow.get("secondname")));
		e.setSurname(String.valueOf(eRow.get("surname")));
		e.setSalary(Double.parseDouble(String.valueOf(eRow.get("salary"))));
		e.setDepId(Long.parseLong(String.valueOf(eRow.get("department_id"))));
		e.setDob(java.sql.Date.valueOf(String.valueOf(eRow.get("dob"))));
	
		return e;
		
	}
	
	/**
	 * A converter between different types.
	 * 
	 * @param dob - of type java.util.Date 
	 * @return dob - of type java.sql.Date
	 */
	
	private java.sql.Date util2SqlDate(Date dob){
		
		return new java.sql.Date(dob.toInstant().toEpochMilli());
		
	}
}
