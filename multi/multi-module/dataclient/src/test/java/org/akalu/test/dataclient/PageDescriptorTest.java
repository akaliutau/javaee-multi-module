package org.akalu.test.dataclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.akalu.dataclient.PageDescriptor;
import org.junit.Test;

/** 
 *  This class contains the simple tests for  PageDescriptor class.
 *  The standard JUnit testing techniques are used.
 *  <p>Almost all tests presented here  test the edge constraints.
 *  
 * @see org.akalu.dataclient.PageDescriptor
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */


public class PageDescriptorTest {
	
	PageDescriptor pd;
	
	@Test
	public void testConstructor(){
		pd = new PageDescriptor(1L, 0, 1);
		
		assertNotNull(pd);
		assertTrue(pd.getTotalSize() == 1L);
		assertTrue(pd.getFirst() == 0);
		assertTrue(pd.getPageSize() == 1);
		assertTrue(!pd.getIsNext());
		assertTrue(!pd.getIsPrevious());

	}
	
	@Test
	public void testGoNextFunctional(){
		pd = new PageDescriptor(5L, 0, 3);
		Boolean b = pd.goNext();
		
		assertNotNull(pd);
		assertTrue(b);
		assertTrue(pd.getFirst() == 3);
		assertTrue(!pd.getIsNext());
		assertTrue(pd.getIsPrevious());
		
	}
	
	/**
	 * Testing constraints for goNext() method
	 * (next page do not exists, on the current there are only 2 (=5-3) records)
	 * 
	 */

	@Test
	public void testGoNextEdge(){
		pd = new PageDescriptor(5L, 3, 3);
		Boolean b = pd.goNext();
		
		assertNotNull(pd);
		assertTrue(!b);
		assertTrue(pd.getFirst() == 3);
		assertTrue(!pd.getIsNext());
		assertTrue(pd.getIsPrevious());
		
	}
	
	/**
	 * Testing constraints for goPrevious() method
	 * (previous page do exists with only 1 record, on the current there are 3 records)
	 * Method must set <first> field to 0. 
	 * 
	 */
	@Test
	public void testGoPrevious(){
		pd = new PageDescriptor(5L, 1, 3);
		Boolean b = pd.goPrevious();
		

		assertNotNull(pd);
		assertTrue(b);
		assertTrue(pd.getFirst() == 0);
		assertTrue(pd.getIsNext());
		assertTrue(!pd.getIsPrevious());
		
	}
	
	@Test
	public void testIncSize(){
		pd = new PageDescriptor(3L, 0, 3);
		
		assertNotNull(pd);
		assertTrue(!pd.getIsNext());

		pd.incSize();

		assertTrue(pd.getIsNext());
		assertTrue(pd.getTotalSize() == 4L);
		
		
	}
	
	@Test
	public void testDecSize(){
		pd = new PageDescriptor(5L, 0, 3);
		pd.decSize();

		assertNotNull(pd);
		assertTrue(pd.getTotalSize() == 4L);
		
		
	}

	@Test
	public void testDecSizeV2(){
		pd = new PageDescriptor(4L, 0, 3);

		assertNotNull(pd);
		assertTrue(!pd.getIsPrevious());
		
		pd.setFirst(4);
		
		assertTrue(pd.getIsPrevious());
		
		pd.decSize();

		assertTrue(!pd.getIsPrevious());
		assertTrue(pd.getFirst() == 1);
		assertTrue(pd.getTotalSize() == 3L);
		
		
	}

	
	@Test
	public void testIsVisiblePositive(){
		pd = new PageDescriptor(5L, 0, 3);
		Boolean b = pd.goNext();

		assertNotNull(pd);
		assertTrue(b);
		assertTrue(pd.getIsVisible());
		
	}

	@Test
	public void testIsVisibleNegative(){
		pd = new PageDescriptor(2L, 0, 3);
		Boolean b = pd.goNext();

		assertNotNull(pd);
		assertTrue(!b);
		assertTrue(!pd.getIsVisible());
		
	}

}
