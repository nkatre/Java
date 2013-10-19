package Amazon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class KClosestPointsToOrigin {

	public static class Point
	{
		private int xCoord;
		private int yCoord;
		
		public int getxCoord() {
			return xCoord;
		}
		public void setxCoord(int xCoord) {
			this.xCoord = xCoord;
		}
		public int getyCoord() {
			return yCoord;
		}
		public void setyCoord(int yCoord) {
			this.yCoord = yCoord;
		}
		
		public Point(int xCoord, int yCoord) {
			super();
			this.xCoord = xCoord;
			this.yCoord = yCoord;
		}
		
		@Override
		public String toString() {
			return "Point [xCoord=" + xCoord + ", yCoord=" + yCoord + "]";
		}
		
		public double getDistanceFromOrigin()
		{
			return Math.sqrt((xCoord * xCoord) + (yCoord * yCoord));
		}
		
	}

	public static class ClosestPointComparator
	  implements Comparator<Point>
	{

		@Override
		/**
		 * if dist(p1) < dist(p2), return (> 0)
		 * else if dist(p1) == dist(p2) , return 0
		 * else return ( < 0)
		 */
		public int compare(Point p1, Point p2) {
			Double dist1 = p1.getDistanceFromOrigin();
			Double dist2 = p2.getDistanceFromOrigin();
			
			return dist2.compareTo(dist1); // Need to create MaxHeap, so reversing here
		}
		
	}
	
	public static List<Point> getKClosestPoints(List<Point> points,
			                             int k)
	{
		if ( null == points)
			return null;
			
		if ( (points.isEmpty()) || (k <= 0))
			return new ArrayList<Point>(); // Return Empty result
		
		ClosestPointComparator cmp = new ClosestPointComparator();
		PriorityQueue<Point> heapQueue = 
				new PriorityQueue<KClosestPointsToOrigin.Point>(k, cmp);
		
		int size = points.size();
		
		for ( int i = 0; i < Math.min(size, k); i++)
			heapQueue.add(points.get(i)); 
		
		for ( int i = k ; i < size ; i++)
		{
			if (cmp.compare(heapQueue.peek(), points.get(i)) < 0)
			{
				// remove head and insert new element
				heapQueue.poll();
				heapQueue.add(points.get(i));
			}
		}
		
		int resultSize = heapQueue.size();
		LinkedList<Point> result = new LinkedList<Point>();
		for ( int i = 0; i < resultSize ; i++)
		{
			result.addFirst(heapQueue.poll());
		}
		return result;
	}
	
	
	public static void main(String[] args)
	{
		List<Point> points = new ArrayList<Point>();
		for ( int i = 10; i >= 0; i--)
		{
			points.add(new Point(i,i));
		}
		
		System.out.println("Result 5 : " + getKClosestPoints(points, 5));
	}
}
