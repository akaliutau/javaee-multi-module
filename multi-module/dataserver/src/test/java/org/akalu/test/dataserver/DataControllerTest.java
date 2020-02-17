package org.akalu.test.dataserver;

import org.akalu.dataserver.DataController;
import org.akalu.uri.DataURI;
import org.akalu.model.Department;
import org.akalu.model.DobParamsSearch;
import org.akalu.model.Employee;
import org.akalu.service.DepartmentDataService;
import org.akalu.service.EmployeeDataService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Matchers.any;
import org.mockito.runners.MockitoJUnitRunner;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class tests all methods of DataController class using mock object
 * technique. MockMvc object plays role of MVC infrastructure with established
 * behavior. The main class to test - DataController - injected as mock
 * {@code dataController}.
 * 
 * <p>
 * All tests mostly functional by nature to the exclusion of the methods
 * test404() and test400() which are testing anomalies.
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */
@RunWith(MockitoJUnitRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class DataControllerTest {

	private final Date curDate = new Date();

	private MockMvc mockMvc;

	@InjectMocks
	private DataController dataController;

	@Mock
	private DepartmentDataService depDataService;

	@Mock
	private EmployeeDataService emplDataService;

	/**
	 * In set-up the behavior of mocked classes is defined
	 * 
	 * 
	 */

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Department d = new Department(1L, "test", 0d);
		List<Department> lst = new ArrayList<Department>();
		lst.add(d);
		Employee e = new Employee(0L, "name1", "name2", "name3", curDate, 1L, 1d);
		List<Employee> lst2 = new ArrayList<Employee>();
		lst2.add(e);

		Mockito.when(depDataService.getDepbyId(1L)).thenReturn(d);
		Mockito.when(depDataService.list(1, 1)).thenReturn(lst);
		Mockito.when(depDataService.delete(1L)).thenReturn(true);
		Mockito.when(depDataService.update(any(Department.class))).thenReturn(true);
		Mockito.when(depDataService.addnew(any(Department.class))).thenReturn(1L);
		Mockito.when(depDataService.getDepSize()).thenReturn(1L);
		Mockito.when(emplDataService.getEmplbyId(0L)).thenReturn(e);
		Mockito.when(emplDataService.list(0, 1, 1L)).thenReturn(lst2);
		Mockito.when(emplDataService.delete(0L)).thenReturn(true);
		Mockito.when(emplDataService.update(any(Employee.class))).thenReturn(true);
		Mockito.when(emplDataService.addnew(any(Employee.class))).thenReturn(0L);
		Mockito.when(emplDataService.search1(curDate)).thenReturn(lst2);
		Mockito.when(emplDataService.getEmplSize(1L)).thenReturn(1L);

		mockMvc = MockMvcBuilders.standaloneSetup(dataController).build();
	}

	@Test
	public void test404() throws Exception {

		mockMvc.perform(get("/nonExistedPage")).andExpect(status().isNotFound());
	}

	/**
	 * 
	 * Tests request with wrong parameter
	 * 
	 * @throws Exception
	 */
	@Test
	public void test400() throws Exception {
		mockMvc.perform(get(DataURI.DEPARTMENT).param("id", "_s@d41~%")).andExpect(status().isBadRequest());
	}

	/**
	 * 
	 * Tests request with omitted mandatory parameter
	 * 
	 * @throws Exception
	 */

	@Test
	public void test400v2() throws Exception {
		mockMvc.perform(delete(DataURI.DEPARTMENT)).andExpect(status().isBadRequest());
	}

	/*
	 * This section includes tests of the department - related requests.
	 * 
	 */

	@Test
	public void testGetDepartmentDetail() throws Exception {

		mockMvc.perform(get(DataURI.DEPARTMENT).param("id", "1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$").isNotEmpty()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.title").value("test"));

		verify(depDataService, times(1)).getDepbyId(1L);
		verifyNoMoreInteractions(depDataService);

	}

	@Test
	public void testGetDepartmentsList() throws Exception {
		mockMvc.perform(get(DataURI.DEPARTMENTS).param("f", "1").param("n", "1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[0].title").value("test"));

		verify(depDataService, times(1)).list(1, 1);
		verifyNoMoreInteractions(depDataService);

	}

	/**
	 * 
	 * In this method some classes from jackson-databind package are used. In
	 * particularly, ObjectMapper - for creating mock JSON request from objects.
	 * 
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetSearchList() throws Exception {
		DobParamsSearch dob = new DobParamsSearch();
		dob.setDob1(curDate);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJSON = ow.writeValueAsString(dob);

		mockMvc.perform(put(DataURI.SEARCH).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$").isNotEmpty()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id").value(0));

		verify(emplDataService, times(1)).search1(dob.getDob1());
		verifyNoMoreInteractions(emplDataService);

	}

	@Test
	public void testDeleteDepartment() throws Exception {
		mockMvc.perform(delete(DataURI.DEPARTMENT).param("id", "1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$").isBoolean())
				.andExpect(jsonPath("$").value(true));

		verify(depDataService, times(1)).delete(1L);
		verifyNoMoreInteractions(depDataService);

	}

	@Test
	public void testAddDepartment() throws Exception {

		Department dep = new Department(1L, "to_update", 0d);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJSON = ow.writeValueAsString(dep);
		// System.out.println(requestJSON);

		mockMvc.perform(post(DataURI.DEPARTMENT).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$").isNumber()).andExpect(jsonPath("$").value(1));

		verify(depDataService, times(1)).addnew(any(Department.class));
		verifyNoMoreInteractions(depDataService);

	}

	@Test
	public void testUpdateDepartment() throws Exception {

		Department dep = new Department(1L, "to_update", 0d);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJSON = ow.writeValueAsString(dep);
		// System.out.println(requestJSON);

		mockMvc.perform(put(DataURI.DEPARTMENT).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$").isNumber()).andExpect(jsonPath("$").value(1));

		verify(depDataService, times(1)).update(any(Department.class));
		verifyNoMoreInteractions(depDataService);

	}

	/*
	 * This section includes tests of the employee - related requests.
	 * 
	 */

	@Test
	public void testGetEmployeesList() throws Exception {
		mockMvc.perform(get(DataURI.EMPLOYEES).param("f", "0").param("n", "1").param("id", "1"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$").isNotEmpty()).andExpect(jsonPath("$[0].id").value(0));

		verify(emplDataService, times(1)).list(0, 1, 1L);
		verifyNoMoreInteractions(emplDataService);

	}

	@Test
	public void testDeleteEmployee() throws Exception {
		mockMvc.perform(delete(DataURI.EMPLOYEE).param("id", "0")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$").isBoolean())
				.andExpect(jsonPath("$").value(true));

		verify(emplDataService, times(1)).delete(0L);
		verifyNoMoreInteractions(emplDataService);

	}

	@Test
	public void testgetEmployeeDetail() throws Exception {
		mockMvc.perform(get(DataURI.EMPLOYEE).param("id", "0")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$").isNotEmpty()).andExpect(jsonPath("$.id").value(0));

		verify(emplDataService, times(1)).getEmplbyId(0L);
		verifyNoMoreInteractions(emplDataService);

	}

	@Test
	public void testAddEmployee() throws Exception {
		Employee e = new Employee(0L, "name1", "name2", "name3", curDate, 1L, 1d);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJSON = ow.writeValueAsString(e);

		mockMvc.perform(post(DataURI.EMPLOYEE).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$").isNumber()).andExpect(jsonPath("$").value(0));

		verify(emplDataService, times(1)).addnew(any(Employee.class));
		verifyNoMoreInteractions(depDataService);

	}

	@Test
	public void testUpdateEmployee() throws Exception {
		Employee e = new Employee(0L, "name1", "name2", "name3", curDate, 1L, 1d);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJSON = ow.writeValueAsString(e);

		mockMvc.perform(put(DataURI.EMPLOYEE).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$").isNumber()).andExpect(jsonPath("$").value(0));

		verify(emplDataService, times(1)).update(any(Employee.class));
		verifyNoMoreInteractions(depDataService);

	}

	/*
	 * This section includes tests of the information requests.
	 * 
	 */

	@Test
	public void testGetDepartmentListSize() throws Exception {
		mockMvc.perform(get(DataURI.DEPARTMENTS_INFO)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$").isNumber())
				.andExpect(jsonPath("$").value(1));

		verify(depDataService, times(1)).getDepSize();
		verifyNoMoreInteractions(depDataService);

	}

	@Test
	public void testGetEmployeeListSize() throws Exception {
		mockMvc.perform(get(DataURI.EMPLOYEES_INFO).param("id", "1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$").isNumber())
				.andExpect(jsonPath("$").value(1));

		verify(emplDataService, times(1)).getEmplSize(1L);
		verifyNoMoreInteractions(emplDataService);

	}

}
