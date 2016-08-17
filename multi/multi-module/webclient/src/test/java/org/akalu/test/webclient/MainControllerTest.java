package org.akalu.test.webclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.akalu.dataclient.DataClient;
import org.akalu.dataclient.ListOption;
import org.akalu.dataclient.ListOptions;
import org.akalu.model.Department;
import org.akalu.model.Employee;
import org.akalu.webclient.MainController;
import org.akalu.dataclient.PageDescriptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
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

/**
 * This class tests the functionality of MainController class.
 * 
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

@RunWith(MockitoJUnitRunner.class)
//@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class})
public class MainControllerTest {
	
	private static final Date curdate = new Date();
	
	List<Department> deplist;
	Department d1, d2;
	Employee e1,e2;
	List<Employee> listfromdep1, listfromdep2;
	ListOptions lopt;
	List<ListOption> lst;

	@InjectMocks
    private MainController mainController;

	
	@Mock
    private DataClient dataClient;
    
    private MockMvc mockMvc;

/**
 * Before any tests the lists containing a few departments and employees  are created.
 * 
 * 
 */
    
    @Before
    public void setUp() {

    	MockitoAnnotations.initMocks(this);
    	
    	deplist = new ArrayList<Department>();
    	d1 = new Department(1L,"test dep 1",13.0d);
    	d2 = new Department(2L,"test dep 2",23.0d);
    	deplist.add(d1);
    	deplist.add(d2);
    	
    	listfromdep1 = new ArrayList<Employee>();
    	listfromdep2 = new ArrayList<Employee>();
    	e1 = new Employee(1L,"a1","a2","a3",curdate,1L,13.0d);
    	e2 = new Employee(2L,"b1","b2","b3",curdate,2L,23.0d);
    	listfromdep1.add(e1);
    	listfromdep2.add(e2);
    	
    	

        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }
    
    /**
     * This method tests a situation when the dataserver is off-line.
     * 
     * 
     */
/*    @Test
    public void testServerOffline() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/");
    	this.mockMvc.perform(requestBuilder).
        andExpect(MockMvcResultMatchers.status().isOk()).
        andExpect(MockMvcResultMatchers.model().size(0)).
        andExpect(MockMvcResultMatchers.view().name("error"));

    	verify(dataClient, times(1)).getdepList();
    	verifyNoMoreInteractions(dataClient);


    }
*/
    /**
     * This method tests the normal, probably first, loading of the main page.
     * 
     * 
     */
    @Test
    public void testGet() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/");
  
    	ListOption l1= new ListOption(d1.getId(),d1.getTitle());
    	ListOption l2= new ListOption(d2.getId(),d2.getTitle());
    	lst = new ArrayList<ListOption>();
    	lst.add(l1);
    	lst.add(l2);
    	PageDescriptor depPageDescriptor = new PageDescriptor(1L, 0, 1); 
    	PageDescriptor emplPageDescriptor = new PageDescriptor(1L, 0, 1); 
    	
    	Mockito.when(dataClient.getdepList()).thenReturn(deplist);
    	Mockito.when(dataClient.getemplList(1L)).thenReturn(listfromdep1);
    	Mockito.when(dataClient.getemplList(2L)).thenReturn(listfromdep2);
    	Mockito.when(dataClient.getDepPageDescriptor()).thenReturn(depPageDescriptor);
    	Mockito.when(dataClient.getEmplPageDescriptor()).thenReturn(emplPageDescriptor);


    	Mockito.when(dataClient.getOptList()).thenReturn(lst);

    	this.mockMvc.perform(requestBuilder).
            andExpect(MockMvcResultMatchers.status().isOk()).
            andExpect(MockMvcResultMatchers.model().size(5)).
            andExpect(MockMvcResultMatchers.model().attribute("depList", deplist)).
            andExpect(MockMvcResultMatchers.model().attribute("emplList", listfromdep1)).
            andExpect(MockMvcResultMatchers.model().attributeExists("depOpt")).
            andExpect(MockMvcResultMatchers.model().attributeExists("depPage")).
            andExpect(MockMvcResultMatchers.model().attributeExists("emplPage")).
            andExpect(MockMvcResultMatchers.view().name("index"));
    
    verify(dataClient, times(1)).getdepList();
    verify(dataClient, times(1)).getemplList(1L);
    verify(dataClient, times(1)).getOptList();
    verify(dataClient, times(2)).getDepPageDescriptor();
    verify(dataClient, times(2)).getEmplPageDescriptor();
    
    verifyNoMoreInteractions(dataClient);
    }
    
    /**
     * This method tests a situation when the list of departments becomes empty
     * (f.e., due to continuous delete operations)
     * 
     * 
     */

    @Test
    public void testEmptyListGet() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/");

    	PageDescriptor depPageDescriptor = new PageDescriptor(1L, 0, 1); 
    	PageDescriptor emplPageDescriptor = new PageDescriptor(1L, 0, 1); 
  	
    	Mockito.when(dataClient.getdepList()).thenReturn(deplist);
    	Mockito.when(dataClient.getemplList(1L)).thenReturn(listfromdep1);
    	Mockito.when(dataClient.getemplList(2L)).thenReturn(listfromdep2);
    	Mockito.when(dataClient.getDepPageDescriptor()).thenReturn(depPageDescriptor);
    	Mockito.when(dataClient.getEmplPageDescriptor()).thenReturn(emplPageDescriptor);


    	lst = new ArrayList<ListOption>();
    	Mockito.when(dataClient.getOptList()).thenReturn(lst);
    	

    	
    	this.mockMvc.perform(requestBuilder).
            andExpect(MockMvcResultMatchers.status().isOk()).
            andExpect(MockMvcResultMatchers.model().size(5)).
            andExpect(MockMvcResultMatchers.model().attribute("depList", deplist)).
            andExpect(MockMvcResultMatchers.model().attribute("emplList", listfromdep1)).
            andExpect(MockMvcResultMatchers.model().attributeExists("depOpt")).
            andExpect(MockMvcResultMatchers.model().attributeExists("depPage")).
            andExpect(MockMvcResultMatchers.model().attributeExists("emplPage")).
           andExpect(MockMvcResultMatchers.view().name("index"));
    
    	verify(dataClient, times(1)).getdepList();
        verify(dataClient, times(1)).getemplList(1L);
        verify(dataClient, times(1)).getOptList();
        verify(dataClient, times(2)).getDepPageDescriptor();
        verify(dataClient, times(2)).getEmplPageDescriptor();

    	verifyNoMoreInteractions(dataClient);
    }

    
    
}