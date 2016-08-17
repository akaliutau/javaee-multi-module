package org.akalu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.akalu.model.Department;
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
public class DepartmentDAOImpl implements DepartmentDAO {

	private static final Logger logger = Logger.getLogger(DepartmentDAOImpl.class);
	
	private static Long depSize = null;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	public DepartmentDAOImpl() {
	}
	
	public DepartmentDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Method uses join clause and aggregation function AVG to retrieve
	 * a list of departments with average salary.
	 * <p> Before actual invoking query a simple validation of the arguments 
	 * for subList method is performed (in order to avoid IndexOutofRange exception.)
	 * <p> Method returns empty list in case of wrong arguments
	 * NB: in general, index of record is not  equal to its id  
	 * @param first - index of the first record in the list 
	 * @param amount - amount of records
	 * @return List<Department>
	 */
	
	@Override
	public List<Department> list(int first, int amount) {
		
		String query = "select d.department_id, d.title, avg(e.salary) as asalary " +
		"from departments d left outer join employees e " +
		"on d.department_id = e.department_id group by d.department_id";
		
		if (depSize == null) size();

		List<Department> listDep = new ArrayList<Department>();
		if (first < 0) first = 0;

		if (first > depSize) return listDep; 
		if (amount<0) return listDep; 
		if (first + amount > depSize) amount = (int) (depSize-first); 
		
		
		try{
		List<Map<String,Object>> depRows = jdbcTemplate.queryForList(query).subList(first, first+amount);
		
		for(Map<String,Object> depRow : depRows){
			Department d = new Department();
			d.setId(Long.parseLong(String.valueOf(depRow.get("department_id"))));
			d.setTitle(String.valueOf(depRow.get("title")));
			Object avgsal = depRow.get("asalary");
			if (avgsal == null){
				d.setAvrSalary(null);
			}else{
				d.setAvrSalary(Double.parseDouble(String.valueOf(avgsal)));
			}
			listDep.add(d);
		}
		logger.debug("Return list of departments, records:" + listDep.size());

		return listDep;
		
		}catch (DataAccessException e)
		{ 
			logger.debug("DAE error");
			return null;
		}
		
		
	}

	@Override
	public Boolean update(Department dep) {
		String query = "update departments set title=?  where department_id=?";

		Object[] args = new Object[] {dep.getTitle(), dep.getId()};
		
		try{
		int res = jdbcTemplate.update(query, args); 
		logger.debug("Update department with id=" + dep.getId());

		return (res == 1)?true:false;
		
		}catch (DataAccessException e)
		{
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
	public Long addnew(final Department dep) {
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
		   @Override
		   public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
		      final PreparedStatement ps =
		    		  connection.prepareStatement("INSERT INTO `departments`(`title`) VALUES(?)",
		        Statement.RETURN_GENERATED_KEYS);
		        ps.setString(1, dep.getTitle());
		        return ps;
		     }
		};

		final KeyHolder holder = new GeneratedKeyHolder();

		try{
	    jdbcTemplate.update(psc, holder);

	    final long newId = holder.getKey().longValue();
	    if (newId > 0) 	depSize++;
		logger.debug("Add new department with id=" + newId);

	    return newId;
	    
		}catch (DataAccessException e)
		{
			logger.debug("DAE error");
			return -1L;
		}
		

	}
	
	/**
	 * Deleting a single department record from database.
	 * <p>Before delete a verification is conducted are there any employees in department.
	 * In the case of positive answer method returns without invoking actual database operation.  
	 * 
	 * 
	 */

	@Override
	public Boolean delete(Long id) {
	
	String query = "select count(*) from employees e where e.department_id=?";
	try{
	Long emplCount = jdbcTemplate.queryForObject(query, new Object[]{id}, Long.class);

	if (emplCount != 0) return false;

	query = "delete from departments where department_id=?";

	int rowsAffected = jdbcTemplate.update(query, id);
	
	if (rowsAffected > 0){
		depSize--;
		logger.debug("Delete department with id=" + id);

		return true;
	}

	return false;
	
	}catch (DataAccessException e)
	{
		logger.debug("DAE error");
		return false;
	}
	
 
	}

	/**
	 * This method uses  RowMapper anonymous class for mapping ResultSet values on
	 * Department's fields.  
	 * 
	 * 
	 * 
	 */
	@Override
	public Department getDepbyId(Long id) {
		String query = "select department_id, title from departments where department_id=?";
		
		try{
		Department dep = jdbcTemplate.queryForObject(query, new Object[]{id},
			new RowMapper<Department>(){

			@Override
			public Department mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Department d = new Department();
				d.setId(rs.getLong("department_id"));
				d.setTitle(rs.getString("title"));
				return d;
				}
			});
		logger.debug("Return department with id=" + dep.getId());

		return dep; 	
		}catch (DataAccessException e)
		{
			logger.debug("DAE error");
			return null;
		}
	}
	
	/**
	 *	This method uses aggregation function COUNT to retrieve
	 *  the number of records in the departments table.
	 * 
	 */

	@Override
	public Long size() {
		String query = "select count(*) from departments";
		
		if (depSize != null) return depSize;
		
		try{
			
			depSize = jdbcTemplate.queryForObject(query, Long.class);
			logger.debug("Return size from departments:" + depSize);
			return depSize;
		
		}catch (DataAccessException e)
		{
			logger.debug("DAE error");
			return -1L;
		}
	}
}
