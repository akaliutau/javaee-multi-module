
package org.akalu.test.dataclient;

import org.akalu.dataclient.DataClient;
import org.akalu.dataclient.DataClientImpl;
import org.akalu.uri.DataURI;
import org.akalu.model.Department;
import org.akalu.model.Employee;
import org.akalu.dataclient.ListOption;
import org.akalu.dataclient.PageDescriptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This class tests Rest client using mock objects technique. Class
 * MockRestServiceServer from Spring-test library plays a role of the mock
 * server, which generates established responses to send back.
 * 
 * <p>
 * During class initializing an instance of {@code DataClient} is created using
 * a fictitious URI
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContext.class })
public class DataClientTest {

	private static final String SERVER_URI = "http://localhost:8080/DataServer";

	private final DataClient dataClient = new DataClientImpl(SERVER_URI);
	private final Date curDate = new Date();

	private static RestTemplate restTemplate;

	private static MockRestServiceServer mockServer;

	/**
	 * Create a {@code MockRestServiceServer} and set up the variable
	 * restTemplate with the same value, as in object to be tested.
	 * 
	 */

	@Before
	public void init() {
		restTemplate = dataClient.getRestTemplate();
		PageDescriptor depPageDescriptor = new PageDescriptor(1L, 0, 1);
		PageDescriptor emplPageDescriptor = new PageDescriptor(1L, 0, 1);
		dataClient.setDepPageDescriptor(depPageDescriptor);
		dataClient.setEmplPageDescriptor(emplPageDescriptor);

		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	/**
	 * A few negative tests are testing situation when the dataserver is
	 * off-line
	 * 
	 * 
	 */

	@Test
	public void testGetDepListNegative() {
		mockServer.expect(requestTo(SERVER_URI + DataURI.DEPARTMENTS + "?n=1&f=0")).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));
		try {
			dataClient.getdepList();
			fail("no exception throws");
		} catch (HttpClientErrorException e) {
			assertTrue(true);
			mockServer.verify();
		}

	}

	@Test
	public void testGetDepNegative() {
		mockServer.expect(requestTo(SERVER_URI + DataURI.DEPARTMENT + "?id=" + 1)).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));
		try {
			dataClient.getdep(1L);
			fail("no exception throws");
		} catch (HttpClientErrorException e) {
			assertTrue(true);
			mockServer.verify();
		}
	}

	@Test
	public void testDeleteDepNegative() {

		mockServer.expect(requestTo(SERVER_URI + DataURI.DEPARTMENT + "?id=1")).andExpect(method(HttpMethod.DELETE))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));
		try {
			dataClient.deleteDep(1L);
			fail("no exception throws");
		} catch (HttpClientErrorException e) {
			assertTrue(true);
			mockServer.verify();
		}
	}

	@Test
	public void testGetEmplListNegative() {
		mockServer.expect(requestTo(SERVER_URI + DataURI.EMPLOYEES + "?id=1&n=1&f=0")).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));
		dataClient.setUpdateEmpl(true);
		try {
			dataClient.getemplList(1L);
			fail("no exception throws");
		} catch (HttpClientErrorException e) {
			assertTrue(true);
			mockServer.verify();
		}

	}

	@Test
	public void testGetEmplNegative() {
		mockServer.expect(requestTo(SERVER_URI + DataURI.EMPLOYEE + "?id=" + 1)).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));
		try {
			dataClient.getEmpl(1L);
			fail("no exception throws");
		} catch (HttpClientErrorException e) {
			assertTrue(true);
			mockServer.verify();
		}
	}

	@Test
	public void testDeleteEmplNegative() {

		mockServer.expect(requestTo(SERVER_URI + DataURI.EMPLOYEE + "?id=1")).andExpect(method(HttpMethod.DELETE))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));
		try {
			dataClient.deleteEmpl(1L);
			fail("no exception throws");
		} catch (HttpClientErrorException e) {
			assertTrue(true);
			mockServer.verify();
		}
	}

	/**
	 * Hereafter the functional tests.
	 * 
	 * 
	 */

	@Test
	public void testGetDep() {
		Department d = new Department(1L, "test", 1d);

		mockServer.expect(requestTo(SERVER_URI + DataURI.DEPARTMENT + "?id=" + 1)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess("{ \"id\" : \"1\", \"title\" : \"test\", \"avrSalary\" : \"1.0\"}",
						MediaType.APPLICATION_JSON));

		Department dActual = dataClient.getdep(1L);

		assertEquals(d.getId(), dActual.getId());
		assertEquals(d.getTitle(), dActual.getTitle());
		assertEquals(d.getAvrSalary(), dActual.getAvrSalary());
		mockServer.verify();

	}

	/**
	 * 
	 * This method uses an object of {@code ObjectMapper} class in order to
	 * create a mock response in JSON format.
	 * 
	 */

	@Test
	public void testGetDepList() throws JsonProcessingException {
		Department d = new Department(1L, "test", 1d);
		List<Department> lst = new ArrayList<Department>();
		lst.add(d);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String mockResponse = ow.writeValueAsString(lst);
		// System.out.println(mockResponse);
		dataClient.setUpdateDep(true);

		mockServer.expect(requestTo(SERVER_URI + DataURI.DEPARTMENTS + "?n=1&f=0")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

		List<Department> dActual = dataClient.getdepList();

		assertNotNull(dActual);
		assertTrue(dActual.size() == 1);
		assertEquals(dActual.get(0).getId(), d.getId());
		mockServer.verify();

	}

	@Test
	public void testAddDep() {
		Department d = new Department(1L, "test", 1d);

		mockServer.expect(requestTo(SERVER_URI + DataURI.DEPARTMENT)).andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess(d.getId().toString(), MediaType.APPLICATION_JSON));

		Long dresult = dataClient.addDep(d);

		assertTrue(dresult == d.getId());
		mockServer.verify();

	}

	@Test
	public void testUpdateDep() {
		Department d = new Department(1L, "test", 1d);

		mockServer.expect(requestTo(SERVER_URI + DataURI.DEPARTMENT)).andExpect(method(HttpMethod.PUT))
				.andRespond(withSuccess("true", MediaType.APPLICATION_JSON));

		Boolean dresult = dataClient.updateDep(d);

		assertTrue(dresult);
		mockServer.verify();

	}

	@Test
	public void testDeleteDep() {

		mockServer.expect(requestTo(SERVER_URI + DataURI.DEPARTMENT + "?id=1")).andExpect(method(HttpMethod.DELETE))
				.andRespond(withSuccess("true", MediaType.APPLICATION_JSON));

		Boolean eresult = dataClient.deleteDep(1L);

		assertTrue(eresult);
		mockServer.verify();

	}

	@Test
	public void testGetEmpl() throws JsonProcessingException {
		Employee e = new Employee(1L, "name1", "name2", "name3", curDate, 1L, 1d);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String mockResponse = ow.writeValueAsString(e);
		// System.out.println(mockResponse);

		mockServer.expect(requestTo(SERVER_URI + DataURI.EMPLOYEE + "?id=" + 1)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

		Employee eActual = dataClient.getEmpl(1L);

		assertEquals(e.getId(), eActual.getId());
		assertEquals(e.getFirstName(), eActual.getFirstName());
		assertEquals(e.getSecondName(), eActual.getSecondName());
		assertEquals(e.getSurname(), eActual.getSurname());
		assertEquals(e.getDepId(), eActual.getDepId());
		assertEquals(e.getSalary(), eActual.getSalary());
		assertEquals(e.getDob(), eActual.getDob());
		mockServer.verify();

	}

	@Test
	public void testGetEmplList() throws JsonProcessingException {
		Employee e = new Employee(1L, "name1", "name2", "name3", curDate, 1L, 1d);
		List<Employee> lst = new ArrayList<Employee>();
		lst.add(e);
		dataClient.setUpdateEmpl(true);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String mockResponse = ow.writeValueAsString(lst);
		// System.out.println(mockResponse);

		mockServer.expect(requestTo(SERVER_URI + DataURI.EMPLOYEES + "?id=1&n=1&f=0")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

		List<Employee> eActual = dataClient.getemplList(1L);

		assertNotNull(eActual);
		assertTrue(eActual.size() == 1);
		assertEquals(eActual.get(0).getId(), e.getId());
		mockServer.verify();

	}

	@Test
	public void testAddEmpl() {
		Employee e = new Employee(1L, "name1", "name2", "name3", curDate, 1L, 1d);

		mockServer.expect(requestTo(SERVER_URI + DataURI.EMPLOYEE)).andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess(e.getId().toString(), MediaType.APPLICATION_JSON));

		Long eresult = dataClient.addEmpl(e);

		assertTrue(eresult == e.getId());
		mockServer.verify();

	}

	@Test
	public void testUpdateEmpl() {
		Employee e = new Employee(1L, "name1", "name2", "name3", curDate, 1L, 1d);

		mockServer.expect(requestTo(SERVER_URI + DataURI.EMPLOYEE)).andExpect(method(HttpMethod.PUT))
				.andRespond(withSuccess("true", MediaType.APPLICATION_JSON));

		Boolean eresult = dataClient.updateEmpl(e);

		assertTrue(eresult);
		mockServer.verify();

	}

	@Test
	public void testDeleteEmpl() {

		mockServer.expect(requestTo(SERVER_URI + DataURI.EMPLOYEE + "?id=1")).andExpect(method(HttpMethod.DELETE))
				.andRespond(withSuccess("true", MediaType.APPLICATION_JSON));

		Boolean eresult = dataClient.deleteEmpl(1L);

		assertTrue(eresult);
		mockServer.verify();

	}

	@Test
	public void testGetFoundList() throws JsonProcessingException {

		Employee e = new Employee(1L, "name1", "name2", "name3", curDate, 1L, 1d);
		List<Employee> lst = new ArrayList<Employee>();
		lst.add(e);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String mockResponse = ow.writeValueAsString(lst);
		// System.out.println(mockResponse);

		mockServer.expect(requestTo(SERVER_URI + DataURI.SEARCH)).andExpect(method(HttpMethod.PUT))
				.andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

		List<Employee> eActual = dataClient.getFoundList(curDate);

		assertNotNull(eActual);
		assertTrue(eActual.size() == 1);
		assertEquals(eActual.get(0).getId(), e.getId());
		mockServer.verify();
	}

	/**
	 * Finally, one test for getOptList() method Test creates a list of
	 * department (consisted from 1 department record actually), then tries to
	 * get a list of available departments to choose.
	 * 
	 * @throws JsonProcessingException
	 * 
	 * 
	 */

	@Test
	public void testGetOptList() throws JsonProcessingException {
		Department d = new Department(1L, "test", 1d);
		List<Department> lst = new ArrayList<Department>();
		lst.add(d);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String mockResponse = ow.writeValueAsString(lst);
		// System.out.println(mockResponse);

		mockServer.expect(requestTo(SERVER_URI + DataURI.DEPARTMENTS + "?n=100&f=0")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

		List<ListOption> lst1 = dataClient.getOptList();

		assertNotNull(lst1);
		assertTrue(lst1.size() == 1);
		assertTrue(lst1.get(0).getId() == 1);

	}

}
