package org.akalu.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.akalu.model.Department;
import org.akalu.model.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * This is an utility class, which is used by EmployeeDAOTest and DepartmentDAOTest classes. 
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

public class DAOHelper {
	
	  public static  void dropTables(JdbcTemplate jdbcTemplate){
		  final String  query1 = "DROP TABLE IF EXISTS `departments`";
		  jdbcTemplate.update(query1);
		  final String query2 = "DROP TABLE IF EXISTS `employees`";
		  jdbcTemplate.update(query2);
		  
	  }
	  
	  public static void createTables(JdbcTemplate jdbcTemplate){

		  final String query1 = "CREATE TABLE `departments`("
		  		+ "`department_id` int(8) NOT NULL AUTO_INCREMENT,"
		  		+ " `title` varchar(255), "
		  		+ "PRIMARY KEY (`department_id`))";
		  jdbcTemplate.update(query1);
		  final String query2 = "CREATE TABLE `employees` ("
		  		+ "`employee_id` int(8) NOT NULL AUTO_INCREMENT,"
		  		+ "`firstname` varchar(30) NOT NULL, "
		  		+ "`secondname` varchar(30) NOT NULL,"
		  		+ "`surname` varchar(50) NOT NULL,"
		  		+ "`dob` date NOT NULL,"
		  		+ "`salary` double NOT NULL,"
		  		+ "`department_id` int(8) NOT NULL,"
		  		+ "PRIMARY KEY (`employee_id`),"
		  		+ "FOREIGN KEY (`department_id`) "
		  		+ "REFERENCES `departments` (`department_id`))";
		  
		  jdbcTemplate.update(query2);
		  
	  }
	  
	
	  public static  Department findDep(JdbcTemplate jdbcTemplate, Long id){
			String query = "select department_id, title from departments where department_id=?";
			
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
			
			return dep; 	
		  
	  }

	  
	  public static  Employee findEmpl(JdbcTemplate jdbcTemplate, Long id){
			String query = "select employee_id, firstname, secondname, surname, dob, salary, department_id " +
					"from employees where employee_id=?";
		
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
		
		return em; 	
		  
	  }

	  public static  Long saveDep(JdbcTemplate jdbcTemplate, final Department dep){
			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				   @Override
				   public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				      final PreparedStatement ps =
				    		  connection.prepareStatement("INSERT INTO departments(title) VALUES(?)",
				        Statement.RETURN_GENERATED_KEYS);
				        ps.setString(1, dep.getTitle());
				        return ps;
				     }
				};

				final KeyHolder holder = new GeneratedKeyHolder();

			    jdbcTemplate.update(psc, holder);

			    final long newId = holder.getKey().longValue();
			    return newId;
			    
	  }
	  
	  public static  Long saveEmpl(JdbcTemplate jdbcTemplate, final Employee em) {
			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				   @Override
				   public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				      final PreparedStatement ps =
				    		connection.prepareStatement(
				    		"INSERT INTO `employees`(firstname, secondname, surname, dob, salary, department_id) " +
				    		"VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				        ps.setString(1, em.getFirstName());
				        ps.setString(2, em.getSecondName());
				        ps.setString(3, em.getSurname());
				        ps.setDate(4, new java.sql.Date(em.getDob().toInstant().toEpochMilli()));
				        ps.setDouble(5, em.getSalary());
				        ps.setLong(6, em.getDepId());
				        return ps;
				     }
				};

				final KeyHolder holder = new GeneratedKeyHolder();

			    jdbcTemplate.update(psc, holder);

			    final long newId = holder.getKey().longValue();
			    return newId;
			    

				
		}



}
