package Amazon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AmazonQuestion {

	/**
	 * Intersection of 2 Integer lists
	 * 
	 * Method:
	 *   Build a hash set containing elements from first list. The set will contain only unique values in first list
	 *   For each entry in the second list, check if the entry is present in the first list. If so, add it to the final result and remove it from the set.
	 * 
	 * @param a First list of integers
	 * @param b Second list of integers
	 * @return intersection of two list containing only unique values. 
	 *         Empty list if two lists are disjoint or either one (or both) list is empty or null
	 */
	public static List<Integer> intersection (List<Integer> a, List<Integer> b) {

		List<Integer> result = new ArrayList<Integer>();

		/**
		 * If either list is empty, then return empty result
		 */
		if ( (null == a) || ( a.isEmpty()))
			return result;

		if ( (null == b) || ( b.isEmpty()))
			return result;

		// Build a Set containing the first list
		Set<Integer> s = new HashSet<Integer>();

		for (Integer i : a)
			s.add(i);

		// Now for each entry in list b, check in the set, 
		// If present in the set, add the entry to the final result list and remove the entry from the set
		for (Integer i : b)
		{
			if (s.contains(i))
			{
				result.add(i);
				s.remove(i);
			}
		}

		return result;
	}

}

