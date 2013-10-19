package Amazon;

import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import Amazon.KClosestPointsToOrigin.Point;


public class FinalScoreQuestion {

	/**
	 * 
	 * Method: Build a map whose key is student id and value is min-heap containing top 5 scores 
	 *         for each student id seen so far.
	 * On each new test result entry, 
	 *   when the number of elements in the queue is less than 5,
	 *      add the score to the queue
	 *   otherwise,
	 *      the minheap's root element is compared with the score. 
	 *      if the root is less, then replace the root with the new score
	 *      otherwise, skip to the next entry
	 * After all scores are processed, iterate
	 * @param results : List of test scores
	 * @return map containing studentId to Average test score.
	 */
	public Map <Integer, Double> calculateFinalScores (List<TestResult> results) 
	{	 
		if ( (results == null) || results.isEmpty())
			return null;

		// Data structure for Min-Heap map
		Map<Integer, PriorityQueue<Integer>> top5ScoreMap = new HashMap<Integer, PriorityQueue<Integer>>();

		for (TestResult t : results)
		{
			if ( top5ScoreMap.containsKey(t.studentId))
			{
				PriorityQueue<Integer> top5Scores = top5ScoreMap.get(t.studentId);

				if ( top5Scores.size() < 5) // if we have not seen first 5 scores of the student, just add
				{
					top5Scores.add(t.testScore);
				} else {
					// 5 scores already present, compare root(head) with the new score. if root is less, then replace
					if ( top5Scores.peek() < t.testScore )
					{
						top5Scores.poll();
						top5Scores.add(t.testScore);
					}
				}
			} else {
				PriorityQueue<Integer> top5Scores = new PriorityQueue<Integer>();
				top5Scores.add(t.studentId);
				top5ScoreMap.put(t.studentId, top5Scores);
			}
		}

		Map <Integer, Double> result = new HashMap<Integer, Double>();

		for (Entry<Integer, PriorityQueue<Integer>> e : top5ScoreMap.entrySet())
		{
			result.put(e.getKey(),getAverage(e.getValue()));
		}
		return result;
	}

	/**
	 * Calculates Average of the elements in the list or 0 if list is empty
	 * 
	 * @param scoreList : List of scores
	 * @return
	 */
	private double getAverage(AbstractCollection<Integer> scoreList )
	{
		int sum = 0;

		if ( null == scoreList)
			return 0;

		for (Integer s : scoreList)
			sum += s;

		int size = scoreList.size();
		return (size > 0 ) ? sum/size : 0;
	}

}

