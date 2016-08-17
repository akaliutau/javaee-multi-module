package org.akalu.test.webclient;

import org.akalu.dataclient.DataClient;
import org.akalu.model.Employee;
import org.akalu.webclient.EmplController;
import org.akalu.webclient.LocalURI;


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
import java.util.Date;

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
 * This class tests the functionality of EmployeeController.
 * 
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

@RunWith(MockitoJUnitRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class})
public class EmployeeControllerTest {
	
	Employee empl = new Employee(1L,"name1","name2", "name3", new Date(), 1L, 0d);

	@InjectMocks
    private EmplController emplController;

	
	@Mock
    private DataClient dataClient;
	
	@Mock
    private BindingResult bindingResult;
    
    private MockMvc mockMvc;

    @Before
    public void setUp() {

    	MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(emplController).build();
        
    	Mockito.when(dataClient.getEmplId(1)).thenReturn(1L);
    	Mockito.when(dataClient.getEmplbyIdx(1)).thenReturn(empl);
        Mockito.when(dataClient.addEmpl(any(Employee.class))).thenReturn(1L);
        Mockito.when(dataClient.updateEmpl(any(Employee.class))).thenReturn(true);
        Mockito.when(dataClient.deleteEmpl(1L)).thenReturn(true);
    	Mockito.when(bindingResult.hasErrors()).thenReturn(false);
    	
 //   	Mockito.when(dataClient.setUpdateEmpl(true));


    }
 
    @Test
    public void test404() throws Exception {
    	
    	RequestBuilder requestBuilder = 
    			MockMvcRequestBuilders.post(LocalURI.EMPL_DIR);
    	
    	this.mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    
    @Test
    public void testaddEmployeePage1() throws Exception {
    	
    	RequestBuilder requestBuilder = 
    			MockMvcRequestBuilders.post(LocalURI.EMPL_DIR+LocalURI.ADD_EMPL);
    	
    	this.mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testaddEmployeePage2() throws Exception {
    	

    	ModelMap model = new ModelMap();
    	
    	String mav = emplController.addEmployeePage(empl, bindingResult, model);
    	
    	assertEquals("redirect: ../",mav);
    	verify(dataClient, times(1)).addEmpl(empl);
    	verify(dataClient, times(1)).setUpdateEmpl(true);
    	verify(dataClient, times(1)).setUpdateDep(true);
    	verifyNoMoreInteractions(dataClient);
    }
    
    @Test
    public void testshowAddEmployeePage() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.get(LocalURI.EMPL_DIR+LocalURI.ADD_NEW_EMPL);
    	
    	this.mockMvc.perform(requestBuilder).
        andExpect(MockMvcResultMatchers.status().isOk()).
        andExpect(MockMvcResultMatchers.model().size(3)).
        andExpect(MockMvcResultMatchers.model().attributeExists("emplmodelattr")).
        andExpect(MockMvcResultMatchers.model().attribute("message","")).
        andExpect(MockMvcResultMatchers.model().attributeExists("depOpt")).
        andExpect(MockMvcResultMatchers.view().name("addempl"));

    }
  
    @Test
    public void testshowEditEmployeePage() throws Exception {
    	RequestBuilder requestBuilder = 
    			MockMvcRequestBuilders.get(LocalURI.EMPL_DIR+LocalURI.EDIT_EMPL)
    			.param("id", "1");
  
    	this.mockMvc.perform(requestBuilder).
        andExpect(MockMvcResultMatchers.status().isOk()).
        andExpect(MockMvcResultMatchers.model().size(3)).
        andExpect(MockMvcResultMatchers.model().attribute("emplmodelattr",empl)).
        andExpect(MockMvcResultMatchers.model().attribute("message","")).
        andExpect(MockMvcResultMatchers.model().attributeExists("depOpt")).
        andExpect(MockMvcResultMatchers.view().name("editempl"));

    	verify(dataClient, times(1)).getEmplbyIdx(1);
    	verify(dataClient, times(1)).getOptList();
    	verifyNoMoreInteractions(dataClient);

    }
    
    @Test
    public void testshowEditEmployeePage2() throws Exception {
    	
    	RequestBuilder requestBuilder = 
    			MockMvcRequestBuilders.post(LocalURI.EMPL_DIR+LocalURI.UPDATE_EMPL);
    	
    	this.mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    public void testshowEditEmployeePage3() throws Exception {
    	
    	ModelMap model = new ModelMap();
    	
    	String mav = emplController.showEditEmployeePage(empl, bindingResult, model);
    	
    	assertEquals("redirect: ../", mav);
    	verify(dataClient, times(1)).updateEmpl(empl);
    	verify(dataClient, times(1)).setUpdateEmpl(true);
    	verify(dataClient, times(1)).setUpdateDep(true);
    	verifyNoMoreInteractions(dataClient);

    }
    
    @Test
    public void testdelEmployee() throws Exception {
    	ModelMap model = new ModelMap();
    	
    	String mav = emplController.delEmployee(model,1);
    	
    	assertEquals("redirect: ../", mav);
    	verify(dataClient, times(1)).getEmplId(1);
    	verify(dataClient, times(1)).deleteEmpl(1L);
    	verify(dataClient, times(1)).setUpdateEmpl(true);
    	verify(dataClient, times(1)).setUpdateDep(true);
    	verifyNoMoreInteractions(dataClient);
    	
    }

    
}
