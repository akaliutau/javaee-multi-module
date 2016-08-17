package org.akalu.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.akalu.dao.EmployeeDAO;
import org.akalu.model.Department;
import org.akalu.model.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * This class tests EmployeeDAO class  with the help of the embedded database H2.
 * In test-cases the following methodology is used.
 * 
 * <p>Firstly, all the necessary records are created, both in memory and in database.
 * <p>Next, the method being tested is executed.
 * <p>Finally, the result of method's execution are compared with that retrieved using
 * alternative access to database. 
 * 
 * <p>Class TestContext is used to programmatically configure invoking environment.
 * @See org.akalu.dataserver.test.TestContext
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDAOContext.class})
@Transactional(transactionManager="transactionManager") 
public class EmployeeDAOTest {
		
	  private final Date curDate = new Date();
	  private final Date pastDate = Date.from(Instant.now().minusSeconds(86500));
	  private final Date pastDate2 = Date.from(Instant.now().minusSeconds(173000));
	
	  @Autowired
	  private EmployeeDAO employeeDAO;
		
	  @Autowired
      private JdbcTemplate jdbcTemplate;
	  
	  @Before
	  public void init(){
		  DAOHelper.dropTables(jdbcTemplate);
		  DAOHelper.createTables(jdbcTemplate);
		  //One department record in DB is needed in order to avoid referential integrity constraints violation error
          Department newDep = new Department(1L,"Test Department",1d);
          DAOHelper.saveDep(jdbcTemplate, newDep);

	  }
	 
	  @Test
      @Rollback(true)
	  @Transactional 
	  public void testAddNew() {   
	         Employee newEmpl = new Employee(1L,"name1","name2", "name3", curDate, 1L, 1d);

	         // Execute the method being tested
	         Long id = employeeDAO.addnew(newEmpl); 

	         Employee e = DAOHelper.findEmpl(jdbcTemplate, id);
			 
	         // Validation
	         assertNotNull(e);
	         assertNotNull(e.getId());
	         assertEquals(id, e.getId());

	    }
		
	  @Test
      @Rollback(true)
	  @Transactional 
		public void testlist(){
		      
		     
	         Employee newEmpl = new Employee(1L,"name1","name2", "name3", curDate, 1L, 1d);
	         Long id = employeeDAO.addnew(newEmpl); 

		     List<Employee> dList = employeeDAO.list(0, 1, 1L);
		     
		     // Validation
	         assertNotNull(dList);
	         assertTrue(dList.size() == 1);
	         assertEquals(dList.get(0).getId() , id);

		}
	  
	  /**
	   * This method tests list method with wrong arguments (amount = -1).
	   * Expecting: returns empty  List<Employee>.
	   * 
	   * 
	   */

	  @Test
      @Rollback(true)
	  @Transactional 
		public void testlistWrongParams(){
		      
		     
	         Employee newEmpl = new Employee(1L,"name1","name2", "name3", curDate, 1L, 1d);
	         Long id = employeeDAO.addnew(newEmpl); 

		     List<Employee> dList = employeeDAO.list(0, -1, 1L);
		     
		     // Validation
	         assertNotNull(dList);
	         assertTrue(dList.size() == 0);
		}
		
	  @Test
      @Rollback(true)
	  @Transactional 
		public void testGetEmplbyId(){
		      

	         Employee newEmpl = new Employee(1L,"name1","name2", "name3", curDate, 1L, 1d);
	         Long id = DAOHelper.saveEmpl(jdbcTemplate, newEmpl);

	         Employee d = employeeDAO.getEmplbyId(id);

	         assertNotNull(d);
	         assertEquals(id, d.getId());
		}
		
	  @Test
      @Rollback(true)
	  @Transactional 
		public void  testUpdate(){
		      
	         Employee newEmpl = new Employee(1L,"name1","name2", "name3", curDate, 1L, 1d);
	         Long id = DAOHelper.saveEmpl(jdbcTemplate, newEmpl);
	         
	         newEmpl.setFirstName("Updated");
	         employeeDAO.update(newEmpl);

	         Employee e = DAOHelper.findEmpl(jdbcTemplate, id);

	         assertNotNull(e);
	         assertNotNull(e.getId());
	         assertEquals(id, e.getId());
	         assertEquals(newEmpl.getFirstName(), e.getFirstName());
		}
		
	  @Test
      @Rollback(true)
	  @Transactional 
		public void testDeletePositive(){
		      
		     
	         Employee newEmpl = new Employee(1L,"name1","name2", "name3", curDate, 1L, 1d);
	         Long id = DAOHelper.saveEmpl(jdbcTemplate, newEmpl);
	         
	         Boolean b = employeeDAO.delete(id);
	         
	         assertTrue(b);
			
		}

	  @Test
      @Rollback(true)
	  @Transactional 
		public void testSearch1Negative(){
		      
		     
	         Employee newEmpl = new Employee(1L,"name1","name2", "name3", curDate, 1L, 1d);
	         Long id = DAOHelper.saveEmpl(jdbcTemplate, newEmpl);
	         
	         assertNotNull(id);
	         
	         List<Employee> dList = employeeDAO.search1(pastDate);
	         
	         assertNotNull(dList);
	         assertTrue(dList.size() == 0);
		}

	  @Test
      @Rollback(true)
	  @Transactional 
		public void testSearch1Positive(){
		      
	         Employee newEmpl = new Employee(1L,"name1","name2", "name3", curDate, 1L, 1d);
	         Long id = DAOHelper.saveEmpl(jdbcTemplate, newEmpl);
	         
	         assertNotNull(id);
	         
	         List<Employee> dList = employeeDAO.search1(curDate);
	         
	         assertNotNull(dList);
	         assertTrue(dList.size() == 1);
	         assertEquals(dList.get(0).getId(), id);
			
		}

	  @Test
      @Rollback(true)
	  @Transactional 
		public void testSearch2Negative(){
		      
	         Employee newEmpl = new Employee(1L,"name1","name2", "name3",  curDate, 1L, 1d);
	         DAOHelper.saveEmpl(jdbcTemplate, newEmpl);
	         
	         List<Employee> dList = employeeDAO.search2(pastDate2, pastDate);
	         
	         assertNotNull(dList);
	         assertTrue(dList.size() == 0);
		}

	  @Test
      @Rollback(true)
	  @Transactional 
		public void testSearch2Positive(){
		      
	         Employee newEmpl = new Employee(1L,"name1","name2", "name3", pastDate, 1L, 1d);
	         Long id = DAOHelper.saveEmpl(jdbcTemplate, newEmpl);
	         
	         List<Employee> dList = employeeDAO.search2(pastDate2, curDate);
	         
	         assertNotNull(dList);
	         assertTrue(dList.size() == 1);
	         assertEquals(dList.get(0).getId(), id);

		}
	  
	  @Test
      @Rollback(true)
	  @Transactional 
		public void testSize(){
		      
	         Employee newEmpl = new Employee(1L,"name1","name2", "name3", pastDate, 1L, 1d);
	         DAOHelper.saveEmpl(jdbcTemplate, newEmpl);


	         Long sz = employeeDAO.size(1L);

	         assertNotNull(sz);
	         assertTrue(sz == 1L);
		}

	  /**
	   * Test for empty department (with id=2 in this case)
	   * 
	   * 
	   */
	  @Test
      @Rollback(true)
	  @Transactional 
		public void testSizeZero(){

	         Long sz = employeeDAO.size(2L);

	         assertNotNull(sz);
	         assertTrue(sz == 0L);
		}

	
}


