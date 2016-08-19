package org.akalu.test.dataclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.akalu.dataclient.ListOption;
import org.akalu.dataclient.ListOptions;
import org.junit.Test;

/**
 * This class contains a simple test for ListOptions class.
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

public class ListOptionsTest {

	@Test
	public void testListOptions() {
		List<ListOption> lo = new ArrayList<ListOption>();
		ListOptions los = new ListOptions(lo, 0L);

		assertNotNull(los);
		assertNotNull(los.getList());
		assertTrue(los.getSelected() == 0);

	}

}
