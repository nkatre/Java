package Amazon;

import junit.framework.Assert;

import org.junit.Test;

public class TestLinkedListCycle {
	
	@Test
	public void testInsertion()
	{
		LinkedList<Integer> list = new LinkedList<Integer>();
		
		for ( int i = 0; i < 10; i++)
		{
			list.addToBegin(i);
		}
		
		// Write a good assertEquals here
		System.out.println("List is :" + list);
	}

	@Test
	public void testInsertion2()
	{
		LinkedList<Integer> list = new LinkedList<Integer>();
		
		for ( int i = 0; i < 10; i++)
		{
			list.addToEnd(i);
		}
		
		// Write a good assertEquals here
		System.out.println("List is :" + list);
	}
	
	@Test
	public void testCycle()
	{
		LinkedList<Integer> list = new LinkedList<Integer>();
		
		for ( int i = 0; i < 6; i++)
		{
			list.addToEnd(i);
		}
		
		Assert.assertFalse("No cycle should be present", list.isCyclePresent());
		list.insertCycle();
		Assert.assertTrue("Cycle should be present", list.isCyclePresent());

	}
}
