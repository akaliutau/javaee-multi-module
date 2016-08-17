package org.akalu.test.webclient;

import org.akalu.dataclient.DataClient;
import org.akalu.model.Department;
import org.akalu.webclient.DepController;
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
 * This class tests the functionality of DepController.
 * 
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

@RunWith(MockitoJUnitRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class})
public class DepControllerTest {
	
	Department dep = new Department(1L,"test",0d);

	@InjectMocks
    private DepController depController;

	
	@Mock
    private DataClient dataClient;
	
	@Mock
    private BindingResult bindingResult;
    
    private MockMvc mockMvc;

    @Before
    public void setUp() {

    	MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(depController).build();
        
    	Mockito.when(dataClient.getDepId(1)).thenReturn(1L);
    	Mockito.when(dataClient.getDepbyIdx(1)).thenReturn(dep);
        Mockito.when(dataClient.addDep(any(Department.class))).thenReturn(1L);
        Mockito.when(dataClient.updateDep(any(Department.class))).thenReturn(true);
        Mockito.when(dataClient.deleteDep(1L)).thenReturn(true);
    	Mockito.when(bindingResult.hasErrors()).thenReturn(false);
    	
 //   	Mockito.when(dataClient.setUpdateDep(true));


    }
 
    @Test
    public void testaddDepartmentPage1() throws Exception {
    	
    	RequestBuilder requestBuilder = 
    			MockMvcRequestBuilders.post(LocalURI.DEP_DIR+LocalURI.ADD_DEP);
    	
    	this.mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testaddDepartmentPage2() throws Exception {
    	

    	ModelMap model = new ModelMap();
    	
    	String mav = depController.addDepartmentPage(dep, bindingResult, model);
    	
    	assertEquals("adddep",mav);
    	verify(dataClient, times(1)).addDep(dep);
    	verify(dataClient, times(1)).setUpdateDep(true);
    	verifyNoMoreInteractions(dataClient);
    }
    
    @Test
    public void testshowAddDepartmentPage() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.get(LocalURI.DEP_DIR+LocalURI.ADD_NEW_DEP);
    	
    	this.mockMvc.perform(requestBuilder).
        andExpect(MockMvcResultMatchers.status().isOk()).
        andExpect(MockMvcResultMatchers.model().size(2)).
        andExpect(MockMvcResultMatchers.model().attributeExists("depmodelattr")).
        andExpect(MockMvcResultMatchers.model().attribute("message","")).
        andExpect(MockMvcResultMatchers.view().name("adddep"));

    }
  
    @Test
    public void testshowEditDepartmentPage() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.get(LocalURI.DEP_DIR+LocalURI.EDIT_DEP)
    			.param("id", "1");
  
    	this.mockMvc.perform(requestBuilder).
        andExpect(MockMvcResultMatchers.status().isOk()).
        andExpect(MockMvcResultMatchers.model().size(2)).
        andExpect(MockMvcResultMatchers.model().attribute("depmodelattr",dep)).
        andExpect(MockMvcResultMatchers.model().attribute("message","")).
        andExpect(MockMvcResultMatchers.view().name("editdep"));
    }
    
    @Test
    public void testshowEditDepartmentPage2() throws Exception {
    	
    	RequestBuilder requestBuilder = 
    			MockMvcRequestBuilders.post(LocalURI.DEP_DIR+LocalURI.UPDATE_DEP);
    	
    	this.mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    public void testshowEditDepartmentPage3() throws Exception {
    	
    	ModelMap model = new ModelMap();
    	
    	String mav = depController.showEditDepartmentPage(dep, bindingResult, model);
    	
    	assertEquals("redirect: ../", mav);
    	verify(dataClient, times(1)).updateDep(dep);
    	verify(dataClient, times(1)).setUpdateDep(true);
    	verifyNoMoreInteractions(dataClient);

    }
    
    @Test
    public void testdelDepartment() throws Exception {
    	ModelMap model = new ModelMap();
    	
    	String mav = depController.delDepartment(model,1);
    	
    	assertEquals("redirect: ../", mav);
    	verify(dataClient, times(1)).getDepId(1);
    	verify(dataClient, times(1)).deleteDep(1L);
    	verify(dataClient, times(1)).setUpdateDep(true);
    	verifyNoMoreInteractions(dataClient);
    	
    }

    
}
