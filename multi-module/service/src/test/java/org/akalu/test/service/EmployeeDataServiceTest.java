package org.akalu.test.service;

import org.akalu.dao.EmployeeDAO;
import org.akalu.model.Employee;
import org.akalu.service.EmployeeDataService;
import org.akalu.service.EmployeeDataServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.runners.MockitoJUnitRunner;

/**
 * This class tests service layer using mock objects for DAO classes.
 * 
 * <p>
 * In this implementation service layer is mostly transitive, and that is why we
 * are using here the most algorithmic of the techniques have been presented so
 * far in current package:
 * 
 * <p>
 * 1. Define value which mock have to return
 * <p>
 * 2. Invoke method to be tested
 * <p>
 * 3. Verify assertions
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

@RunWith(MockitoJUnitRunner.class)
public class EmployeeDataServiceTest {

	// Expected object
	private final Employee persistedEmpl = new Employee(1L, "name1", "name2", "name3", new Date(), 1L, 1d);

	@Mock
	private EmployeeDAO emplDAO;

	@InjectMocks
	private EmployeeDataService employeeDataService = new EmployeeDataServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetEmplbyId() {

		// Mockito expectations
		Mockito.when(emplDAO.getEmplbyId(1L)).thenReturn(persistedEmpl);

		// Execute the method being tested
		Employee result = employeeDataService.getEmplbyId(1L);

		// Validation
		assertNotNull(result);
		assertTrue(1 == result.getId());

		verify(emplDAO).getEmplbyId(1L);
		verifyNoMoreInteractions(emplDAO);
	}

	@Test
	public void testlist() {
		List<Employee> lst = new ArrayList<Employee>();
		lst.add(persistedEmpl);
		Mockito.when(emplDAO.list(0, 10, 1L)).thenReturn(lst);

		List<Employee> result = employeeDataService.list(0, 10, 1L);

		assertNotNull(result);
		assertTrue(result.size() == 1);
		assertEquals(lst.get(0).getId(), result.get(0).getId());

		verify(emplDAO).list(0, 10, 1L);
		verifyNoMoreInteractions(emplDAO);

	}

	@Test
	public void testAddnew() {
		Mockito.when(emplDAO.addnew(persistedEmpl)).thenReturn(1L);

		Long result = employeeDataService.addnew(persistedEmpl);
		assertTrue(result != null);

		verify(emplDAO).addnew(persistedEmpl);
		verifyNoMoreInteractions(emplDAO);

	}

	@Test
	public void testUpdate() {
		Mockito.when(emplDAO.update(persistedEmpl)).thenReturn(true);

		Boolean result = employeeDataService.update(persistedEmpl);
		assertTrue(result);

		verify(emplDAO).update(persistedEmpl);
		verifyNoMoreInteractions(emplDAO);

	}

	@Test
	public void testDelete() {
		Mockito.when(emplDAO.delete(1L)).thenReturn(true);

		Boolean result = employeeDataService.delete(1L);
		assertTrue(result);

		verify(emplDAO).delete(1L);
		verifyNoMoreInteractions(emplDAO);

	}

	@Test
	public void testEmplSize() {
		Mockito.when(emplDAO.size(1L)).thenReturn(1L);

		Long result = employeeDataService.getEmplSize(1L);
		assertTrue(result == 1);

		verify(emplDAO).size(1L);
		verifyNoMoreInteractions(emplDAO);

	}

	@Test
	public void testSearch1() {
		List<Employee> lst = new ArrayList<Employee>();
		lst.add(persistedEmpl);
		Date dob1 = persistedEmpl.getDob();
		Mockito.when(emplDAO.search1(dob1)).thenReturn(lst);

		List<Employee> result = employeeDataService.search1(dob1);

		assertNotNull(result);
		assertTrue(result.size() == 1);
		assertEquals(lst.get(0).getId(), result.get(0).getId());

		verify(emplDAO).search1(dob1);
		verifyNoMoreInteractions(emplDAO);

	}

}
