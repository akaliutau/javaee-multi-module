package org.akalu.test.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.akalu.dao.DepartmentDAO;
import org.akalu.model.Department;
import org.akalu.service.DepartmentDataService;
import org.akalu.service.DepartmentDataServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.List;

import org.mockito.runners.MockitoJUnitRunner;

/**
 * This class tests service layer using mock objects for DAO classes.
 * 
 * <p>In this implementation service layer is mostly transitive, and that is why  
 * we are using here the most algorithmic of the techniques have been presented
 * so far in current package:
 * 
 * <p>1. Define value which mock have to return 
 * <p>2. Invoke method to be tested
 * <p>3. Verify assertions
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

@RunWith(MockitoJUnitRunner.class)
public class DepartmentDataServiceTest {
	
    // Expected object
		private final Department persistedDep = new Department(1L,"test1",1d);



		@Mock
	    private DepartmentDAO depDAO;

	    @InjectMocks
	    private DepartmentDataService departmentDataService 
	    			= new DepartmentDataServiceImpl();
	    
	    @Before
	    public void setup() {
	        MockitoAnnotations.initMocks(this);
	    } 

	    @Test
	    public void testGetDepbyId() {


			// Mockito expectations                            
			Mockito.when(depDAO.getDepbyId(1L)).thenReturn(persistedDep);

	        // Execute the method being tested     
			Department result = departmentDataService.getDepbyId(1L);

	        // Validation  
	        assertNotNull(result);
	        assertTrue(1 == result.getId());
	        assertEquals("test1", result.getTitle());

	        verify(depDAO).getDepbyId(1L);
	        verifyNoMoreInteractions(depDAO);
	    }
	    
	    @Test
		public void testlist(){
	    	List<Department> lst = new ArrayList<Department>();
	    	lst.add(persistedDep);
	    	Mockito.when(depDAO.list(0, 10)).thenReturn(lst);
	    	
	    	List<Department> result = departmentDataService.list(0, 10);
			
	        assertNotNull(result);
	        assertTrue(result.size() == 1);
	        assertEquals(lst.get(0).getId(), result.get(0).getId());

	        verify(depDAO).list(0,10);
	        verifyNoMoreInteractions(depDAO);
			
		}
		
	    @Test
		public void testAddnew(){
	    	Mockito.when(depDAO.addnew(persistedDep)).thenReturn(1L);
	    	
	    	Long result = departmentDataService.addnew(persistedDep);
	        assertTrue(result != null);

	        verify(depDAO).addnew(persistedDep);
	        verifyNoMoreInteractions(depDAO);
			
		}
		
	    @Test
		public void testUpdate(){
	    	Mockito.when(depDAO.update(persistedDep)).thenReturn(true);
	    	
	    	Boolean result = departmentDataService.update(persistedDep);
	        assertTrue(result);

	        verify(depDAO).update(persistedDep);
	        verifyNoMoreInteractions(depDAO);
			
		}
		
	    @Test
		public void testDelete(){
	    	Mockito.when(depDAO.delete(1L)).thenReturn(true);
	    	
	    	Boolean result = departmentDataService.delete(1L);
	        assertTrue(result);

	        verify(depDAO).delete(1L);
	        verifyNoMoreInteractions(depDAO);

			
		}
	    
	    @Test
		public void testDepSize(){
	    	Mockito.when(depDAO.size()).thenReturn(1L);
	    	
	    	Long result = departmentDataService.getDepSize();
	        assertTrue(result == 1);

	        verify(depDAO).size();
	        verifyNoMoreInteractions(depDAO);

			
		}


	}
