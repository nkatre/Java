package general;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ModerateCareerCup {
	
	/*
	 * Write a function to swap a number in place without temporary variables
	 */
	public void swap(int x, int y) {
		
		System.out.println("x: " + x + " y: " + y);
		
		int z = x + y;
		x = z - x;
		y = z - y;
		
		System.out.println("x: " + x + " y: " + y);
	}
	
	/*
	 * Write a method which finds the maximum of two numbers 
	 * You should not use if-else or any other comparison operator 
	 */
	//TODO
	public void findMax(int a, int b) {
		
	}
	
	/*
	 * The Game of Master Mind is played as follows: 
		The computer has four slots containing balls that are red (R), yellow (Y), green (G) or 
		blue (B) For example, the computer might have RGGB (e g , Slot #1 is red, Slots #2 and 
		#3 are green, Slot #4 is blue) 
		You, the user, are trying to guess the solution You might, for example, guess YRGB 
		When you guess the correct color for the correct slot, you get a �hit� If you guess 
		a color that exists but is in the wrong slot, you get a �pseudo-hit� For example, the 
		guess YRGB has 2 hits and one pseudo hit 
		For each guess, you are told the number of hits and pseudo-hits 
		Write a method that, given a guess and a solution, returns the number of hits and 
		pseudo hits 
	 */
	public void gameOfMasterMind(char[] solution, char[] guess) {
		
		int hit = 0;
		int pseudoHit = 0;
		for(int i=0; i<solution.length; i++) {
			if(solution[i] == guess[i]) {
				hit++;
				solution[i] = '#';
			}
		}
		for(int i=0; i<solution.length; i++) {
			if(solution[i] == '#') {
				continue;
			}
			for(int j=0; j<guess.length; j++) {
				if(solution[i] == guess[j]) {
					pseudoHit++;
					solution[i] = '#';
				}
			}
		}
		
		System.out.println("hit: " + hit + " pseudoHit:" + pseudoHit);
	}
	
	/*
	 * Given an integer between 0 and 999,999, print an English phrase that describes the 
		integer (eg, �One Thousand, Two Hundred and Thirty Four�) 
	 */
	public void printNumberInWords(int number) {
		if(number<0 || number >999999) {
			System.out.println("integer should be between 0 and 999,999");
			return;
		}
		
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(0, "zero");
		map.put(1, "one");
		map.put(2, "two");
		map.put(3, "three");
		map.put(4, "four");
		map.put(5, "five");
		map.put(6, "six");
		map.put(7, "seven");
		map.put(8, "eight");
		map.put(9, "nine");
		
		Map<Integer, String> tenMap = new HashMap<Integer, String>();
		tenMap.put(1, "-teen ");
		tenMap.put(2, "twenty ");
		tenMap.put(3, "thirty ");
		tenMap.put(4, "forty ");
		tenMap.put(5, "fifty ");
		tenMap.put(6, "sixty ");
		tenMap.put(7, "seventy ");
		tenMap.put(8, "eighty ");
		tenMap.put(9, "ninty ");
		
		Map<Integer, String> teenMap = new HashMap<Integer, String>();
		teenMap.put(0, "ten");
		teenMap.put(1, "eleven");
		teenMap.put(2, "twelve ");
		teenMap.put(3, "thirteen ");
		teenMap.put(4, "fourteen ");
		teenMap.put(5, "fifteen ");
		teenMap.put(6, "sixteen ");
		teenMap.put(7, "seventeen ");
		teenMap.put(8, "eighteen");
		teenMap.put(9, "ninteen ");
		
		// 0-ones 1-tens 2-hundreds 3-thousands 4-ten thousands 5-hundred thousands
		int[] pcount = new int[6];
		
		int i=0;
		int x = number;
		int c;
		
		while(i<6) {
			c = x%10;
			x = x/10;
			pcount[i] = c;
			i++;
		}
		
		StringBuilder sb = new StringBuilder();
		boolean andFlag = false;
		boolean tenFlag = false;
		if(pcount[5] !=0) {
			sb.append(map.get(pcount[5]) + " hundred ");
			andFlag = true;
		}
		if(pcount[4] !=0) {
			if(andFlag)
				sb.append("and ");
			
			if (pcount[4] == 1){
				tenFlag = true;
			} else {
				sb.append(tenMap.get(pcount[4]) );
			}
			andFlag = false;
		}
		if(pcount[3] !=0) {
			if(andFlag)
				sb.append("and ");
			if(tenFlag) {
				sb.append(teenMap.get(pcount[3]) + " ");
			} else {
				sb.append(map.get(pcount[3]) + " ");
			}
		}
		//if(!sb.equals("")) { 
		if(sb.length()!=0) {
			sb.append("thousand");
		}
		andFlag = false;
		tenFlag = false;
		if(pcount[2] !=0) {
			sb.append(map.get(pcount[2]) + " hundred ");
			andFlag = true;
		}
		if(pcount[1] !=0) {
			if(andFlag)
				sb.append("and ");
			if (pcount[1] == 1){
				tenFlag = true;
			} else {
				sb.append(tenMap.get(pcount[1]));
			}
			andFlag = false;
		}
		if(pcount[0] !=0) {
			if(andFlag)
				sb.append("and ");
			if(tenFlag) {
				sb.append(teenMap.get(pcount[0]));
			} else {
				sb.append(map.get(pcount[0]));
			}
		}
		if((sb.length()==0) && (pcount[0] ==0)) {
			sb.append("zero ");	
		}
		System.out.println(sb.toString());
	}
	
	/*
	 * You are given an array of integers (both positive and negative) Find the continuous 
		sequence with the largest sum Return the sum 
		EXAMPLE
		Input: {2, -8, 3, -2, 4, -10}
		Output: 5 (i e , {3, -2, 4} )
	 */
	
	//VB : THere is a solution to do better than O(n^2). 
	//VB : Please google for Maximum Sub-sequence sum. THis is very common question
	public void findLargestSumInSubSequence(int[] a) {
		int max = Integer.MIN_VALUE;
		int sum = 0;
		
		for(int i=0; i<a.length; i++) {
			
			for(int j=(a.length-1); j>i; j--) {
				
				for(int z = i ; z <= j ; z++) {
					sum = sum + a[z];
				}
				if (sum > max) {
					max = sum;
				}
				sum = 0;
			}
		}
		System.out.println("Max sum in the subsequence: " + max);
	}
	
	/*
	 * Design a method to find the frequency of occurrences of any given word in a book 
	 */
	public int frequency(String book, String word) {
		//VB : I you want to fin frequency of only one word, Why build a map
		//VB : If you have to do this for many words, you can implement a class
		//VB : that first builds the map and has an API to return frequency in O(1) time
		String[] words = book.split(" ");
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(int i=0; i<words.length; i++) {
			if(map.get(words[i]) != null) {
				map.put(words[i], (map.get(words[i])+1));
			} else {
				map.put(words[i], 1);
			}
		}
		
		return map.get(word);
	}
	
	/*
	 * Write a method to generate a random number between 1 and 7, given a method 
		that generates a random number between 1 and 5 (i e , implement rand7() using 
		rand5()) 
	 */
	public int rand7() {
		
		int x = rand5();
		int y = rand5();
		
		if((x+y) <=7)
			return x+y;
		
		//VB :This solution will not give equal probability for all numbers betwen  1-7. Please google
		if((Math.abs(x-y) > 0) && (Math.abs(x-y) <=7)) {
			return Math.abs(x-y);
		}

		return x;
	}
	
	//VB :This solution will not give equal probability for all numbers betwen  1-5. Please google
	private int rand5() {
		int r = (int) Math.round(Math.random()*10);
		if(r == 0) {
			return 1;
		}
		if(r > 5) {
			return 5;
		}
		
		return r;
	}
	
	/*
	 * Design an algorithm to find all pairs of integers within an array which sum to a specified value 
	 */
	public void findPairsThatSumToGivenNumber(int[] a, int n) {
		
		Map<Integer, Integer> map = new ConcurrentHashMap<Integer, Integer>(); //VB : Why concurrent Hash Map ?
		
		for(int i=0; i<a.length; i++) {			
			if(map.get(a[i]) != null) {
				map.put(a[i], (map.get(a[i]) + 1));
			} else {
				map.put(a[i], 1);
			}
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(Entry<Integer, Integer> e : map.entrySet()) {
			int key = e.getKey();
			int count = e.getValue();
			
			int diff = n - key;
			if(map.get(diff) != null) {
				sb.append("(" + key + "," + diff + ") ");
				if(map.get(diff) > 1) {
					map.put(diff, (map.get(diff)-1));
				} else {
					map.remove(diff);
				}
				if(map.get(key) > 1) {
					map.put(key, (map.get(key)-1));
				} else {
					map.remove(key);
				}
			}
			
		}
		System.out.println(sb.toString());
	}
	
	
	public static void main(String[] args) {
		ModerateCareerCup m = new ModerateCareerCup();
		/*
		m.swap(3, 5);
		
		char[] solution = {'R','G','G','B'};
		char[] guess = {'R','Y','G','B'};
		m.gameOfMasterMind(solution, guess);
		
		System.out.println("0");
		m.printNumberInWords(0);
		
		int[] a = {2, -8, 3, -2, 4, -10};
		m.findLargestSumInSubSequence(a);
		
		
		String book = "this is a very good book and a ery nice book and a famous novel";
		System.out.println(m.frequency(book, "book"));
		
		System.out.println(m.rand7());
		System.out.println(m.rand7());
		System.out.println(m.rand7());
		System.out.println(m.rand7());
		System.out.println(m.rand7());
		System.out.println(m.rand7());
		System.out.println(m.rand7());
		
		*/
		
		int[] a = {2, 0, 3, 1, 4, 5, 6, -1};
		m.findPairsThatSumToGivenNumber(a, 5);

	}
	

}
