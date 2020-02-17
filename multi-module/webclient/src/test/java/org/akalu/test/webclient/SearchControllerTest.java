package org.akalu.test.webclient;

import org.akalu.dataclient.DataClient;
import org.akalu.model.Dob;
import org.akalu.model.Dob2;
import org.akalu.model.Employee;
import org.akalu.webclient.LocalURI;
import org.akalu.webclient.SearchController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

/**
 * This class tests the functionality of SearchController.
 * 
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

@RunWith(MockitoJUnitRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class SearchControllerTest {

	List<Employee> lst;

	@InjectMocks
	private SearchController sController;

	@Mock
	private DataClient dataClient;

	@Mock
	private BindingResult bindingResult;

	private MockMvc mockMvc;

	@Before
	public void setUp() {

		Employee empl = new Employee(1L, "name1", "name2", "name3", new Date(), 1L, 0d);
		lst = new ArrayList<Employee>();
		lst.add(empl);

		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(sController).build();

		Mockito.when(dataClient.getFoundList(any(Date.class))).thenReturn(lst);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);

	}

	/**
	 * 
	 * Test for first loading of the search page
	 * 
	 * @throws Exception
	 */

	@Test
	public void testShowSearchPage() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(LocalURI.NEW_SEARCH);

		this.mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().size(4))
				.andExpect(MockMvcResultMatchers.model().attributeExists("searchmodelattr"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("searchmodelattr2"))
				.andExpect(MockMvcResultMatchers.model().attribute("resultstitle", ""))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("foundList"))
				.andExpect(MockMvcResultMatchers.view().name("search"));

	}

	@Test
	public void testshowResultPage1() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(LocalURI.SEARCH1);

		this.mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void testshowResultPage1_2() throws Exception {

		ModelMap model = new ModelMap();

		String mav = sController.showResultPage1(new Dob(), bindingResult, new Dob2(), model);

		assertEquals("search", mav);
		verify(dataClient, times(1)).getFoundList(any(Date.class));
		verifyNoMoreInteractions(dataClient);

	}

	@Test
	public void testshowResultPage2() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(LocalURI.SEARCH2);

		this.mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void testshowResultPage2_2() throws Exception {

		ModelMap model = new ModelMap();

		String mav = sController.showResultPage2(new Dob(), new Dob2(), bindingResult, model);

		assertEquals("search", mav);
		verify(dataClient, times(1)).getFoundList(any(Date.class), any(Date.class));
		verifyNoMoreInteractions(dataClient);

	}

}