package org.akalu.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.akalu.dao.DepartmentDAO;
import org.akalu.dao.DepartmentDAOImpl;
import org.akalu.model.Department;
import org.akalu.model.Employee;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * This class tests DepartmentDAO class with the help of the embedded database
 * H2. In tests the following methodology is used.
 * 
 * <p>
 * Firstly, all the necessary records are created, both in memory and in
 * database.
 * <p>
 * Next, the method being tested is executed.
 * <p>
 * Finally, the result of method's execution are compared with that retrieved
 * using alternative access to database (alternative methods are collected in
 * DAOHelper class).
 * 
 * <p>
 * Class TestContext is used to programmatically configure invoking environment.
 * 
 * @See org.akalu.dataserver.test.TestContext
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestDAOContext.class })
@Transactional(transactionManager = "transactionManager")
public class DepartmentDAOTest {

	@Autowired
	private DepartmentDAO departmentDAO;

	@Autowired
	private NamedParameterJdbcTemplate npJdbcTemplate;

	@Autowired
	private SimpleJdbcInsert insertDepartment;

	@Autowired
	private SimpleJdbcInsert insertEmployee;

	@Before
	public void init() {
		DAOHelper.dropTables(npJdbcTemplate);
		DAOHelper.createTables(npJdbcTemplate);
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testAddNew() {

		Department newDep = new Department(1L, "Test Department", 1d);

		// Execute the method being tested
		Long id = departmentDAO.addnew(newDep);

		Department d = DAOHelper.findDep(npJdbcTemplate, id);

		// Validation
		assertNotNull(d);
		assertNotNull(d.getId());
		assertEquals(id, d.getId());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testGetDepbyId() {

		Department newDep = new Department(2L, "Test Department", 1d);
		Long id = DAOHelper.saveDep(insertDepartment, newDep);

		Department d = departmentDAO.getDepbyId(id);

		assertNotNull(d);
		assertEquals(id, d.getId());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testUpdate() {

		Department newDep = new Department(1L, "Test Department", 1d);
		Long id = DAOHelper.saveDep(insertDepartment, newDep);

		newDep.setTitle("Updated");
		departmentDAO.update(newDep);

		Department d = DAOHelper.findDep(npJdbcTemplate, id);

		assertNotNull(d);
		assertNotNull(d.getId());
		assertEquals(id, d.getId());
		assertEquals(newDep.getTitle(), d.getTitle());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testDeletePositive() {

		Department newDep = new Department(3L, "Test Department", 1d);
		Long id = DAOHelper.saveDep(insertDepartment, newDep);

		Boolean b = departmentDAO.delete(id);

		assertTrue(b);

	}

	@Test
	@Rollback(true)
	@Transactional
	public void testDeleteNegative() {

		Department newDep = new Department(1L, "Test Department", 1d);
		Long id = DAOHelper.saveDep(insertDepartment, newDep);
		Employee e1 = new Employee(1L, "name1", "name2", "name3", new Date(), id, 10d);
		DAOHelper.saveEmpl(insertEmployee, e1);

		departmentDAO.delete(id);

		Department d = DAOHelper.findDep(npJdbcTemplate, id);

		assertNotNull(d);

	}

	@Test
	@Rollback(true)
	@Transactional
	public void testSize() {

		Department newDep = new Department(2L, "Test Department", 2d);
		DAOHelper.saveDep(insertDepartment, newDep);

		Long sz = departmentDAO.size();

		assertNotNull(sz);
		assertTrue(sz > 0L);
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testdepSizeNull() {

		DepartmentDAO departmentDAO = new DepartmentDAOImpl(npJdbcTemplate);

		try {
			departmentDAO.size();
			assertTrue(true);
		} catch (Exception e) {
			fail("Must not throw an exception");
		}

	}

	@Test
	@Rollback(true)
	@Transactional
	public void testListNonEmpty() {

		Department newDep = new Department(1L, "Test Department", 1d);
		Long id = DAOHelper.saveDep(insertDepartment, newDep);
		Employee e1 = new Employee(10L, "name1", "name2", "name3", new Date(), id, 10d);
		Employee e2 = new Employee(20L, "name1", "name2", "name3", new Date(), id, 20d);
		DAOHelper.saveEmpl(insertEmployee, e1);
		DAOHelper.saveEmpl(insertEmployee, e2);

		List<Department> dList = departmentDAO.list(0, 1);

		// Validation
		assertNotNull(dList);
		assertTrue(dList.size() == 1);
		assertTrue(dList.get(0).getAvrSalary() == 15d);
		assertEquals(id, dList.get(0).getId());

	}

	/**
	 * This method tests list method with wrong arguments (amount = -1).
	 * Expecting: returns empty List<Department>.
	 * 
	 * 
	 */

	@Test
	@Rollback(true)
	@Transactional
	public void testListWrongParams() {

		Department newDep = new Department(1L, "Test Department", 1d);
		Long id = DAOHelper.saveDep(insertDepartment, newDep);
		Employee e1 = new Employee(10L, "name1", "name2", "name3", new Date(), id, 10d);
		Employee e2 = new Employee(20L, "name1", "name2", "name3", new Date(), id, 20d);
		DAOHelper.saveEmpl(insertEmployee, e1);
		DAOHelper.saveEmpl(insertEmployee, e2);

		List<Department> dList = departmentDAO.list(0, -1);

		// Validation
		assertNotNull(dList);
		assertTrue(dList.size() == 0);
	}

}
