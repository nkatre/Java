package Amazon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FindMaxOccurenceNumber {
	
	public static void main(String[] args)
	{
		int[] arr = { 1, 2, 2, 3, 3, 2, 5, 0, 1, 5, 2, 3, 3, 5, 5, 3};
		
		// There can be more than on most frequent number. What should we do ?
		// Assuming we need to print all of them.
		
		Map<Integer, Integer> countMap = new HashMap<Integer,Integer>();
		
		for ( int i = 0 ; i < arr.length; i++ )
		{
			if (countMap.containsKey(arr[i]))
			{
				int c = countMap.get(arr[i]);
				c++;
				countMap.put(arr[i], c);
			} else {
				countMap.put(arr[i], 1);
			}
		}
		
		Iterator<Entry<Integer,Integer>> itr = countMap.entrySet().iterator();
		List<Integer> currMaxKeys = new ArrayList<Integer>();
		int currMax = Integer.MIN_VALUE;
		while ( itr.hasNext() )
		{
			Entry<Integer,Integer> pair = itr.next();
			
			if ( pair.getValue() > currMax)
			{
				currMaxKeys.clear();
				currMaxKeys.add(pair.getKey()); // Store the key
				currMax = pair.getValue();
			} else if ( pair.getValue() == currMax) {
				currMaxKeys.add(pair.getKey());
			}
		}
		
		System.out.println("Result is : " + currMaxKeys + " with occurence : " + currMax);
	}
}
