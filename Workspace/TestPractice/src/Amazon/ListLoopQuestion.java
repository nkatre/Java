package Amazon;

public class ListLoopQuestion {
	public static class ListNode {

	     public int value;

	     public ListNode next;
	  }
	 
	  /**
	   *  My approach is to use 2 pointers (slow and fast).
	   *  The slow pointer is traversed one node at a time.
	   *  The fast pointer is traversed 2 nodes at a time.
	   *  If there is a cycle, at one time during iteration, 
	   *   the slow and fast pointers would point to the same node
	   *  otherwise, the fast pointer would hit the tail (node.next == null)
	   *  
	   *  Assumption: 
	   *      The List is expected NOT to be modified concurrently
	   **/
	  public static boolean hasLoops( ListNode myList ) {
	          
	      ListNode slowItr = myList;
	      ListNode fastItr = myList;

		  if ( null == slowItr) 
			return false;   // No cycle in empty linked-list
			
			while ( true)
			{
				slowItr = slowItr.next;
				fastItr = fastItr.next;
				
				if ( null == fastItr)
					return false; // Seen the tail and no cycle, return false
				
				// No need to check for null for slow iterator as fast iterator
				// is ahead and linked-list is expected to be static
				fastItr = fastItr.next;
				
				if ( null == fastItr)
					return false;
				else if ( fastItr == slowItr) // reference check alone is enough. No need for equals()
					return true;
			}	      
	  }

}
