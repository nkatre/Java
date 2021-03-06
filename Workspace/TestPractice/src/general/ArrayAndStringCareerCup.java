package general;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

public class ArrayAndStringCareerCup<T> {
	
	/*
	 * Implement an algorithm to determine if a string has all unique characters 
	 * What if you can not use additional data structures?
	 */
	// O(n2)
	public boolean allCharactersUnique(String s) {		
		if((null==s) ||(s.isEmpty())) {
			return false;
		}		
		char[] sArray = s.toCharArray();
		
		for(int i=0; i<s.length(); i++) {
			for(int j=(i+1); j<s.length(); j++) {
				if(sArray[i]==sArray[j]) {
					return false;
				}
			}
		}		
		return true;
	}
	
	/*
	 * Write code to reverse a C-Style String 
	 * (C-String means that �abcd� is represented as five characters, including the null character )
	 */
	//O(n2)
	public String reverse(String s) {
		if(null == s)
			return null;
		
		if(s.isEmpty())
			return s;
		
		int size = s.length();
		
		StringBuffer sb = new StringBuffer();
		for(int i=size-1; i>=0; i--) {
			sb.append(s.charAt(i));
		}

		return sb.toString();
	}
	
	
	/*
	 * Design an algorithm and write code to remove the duplicate characters in a string without using any additional buffer 
	 * NOTE: One or two additional variables are fine 
	 * An extra copy of the array is not 
	 * FOLLOW UP
	 * Write the test cases for this method 
	 */
	// VB : The below 2 testcases do not work     ssfsassssss
	public String removeDuplicateCharacters(String s) {
		if(null ==s)
			return null;
		
		if(s.isEmpty())
			return s;

		for(int i=0; i< s.length(); i++) {
			for (int j = (i+1); j <s.length(); j++) {
				if(s.charAt(i)==s.charAt(j)) {
					System.out.println(s.substring(0, (j))+ " + " +   s.substring(j+1, s.length())   );
					s = s.substring(0,j) + s.substring(j+1, s.length());		
					j--;
				}
			}
		}
		
		return s;
	}
	
	/*
	 * Write a method to decide if two strings are anagrams or not 
	 */
	
	public boolean areAnagrams(String s1, String s2) {
		
		if((null==s1)||(null==s2)){
			return false;
		}
		
		if((s1.isEmpty())||(s2.isEmpty())){
			return false;
		}
		
		if (s1.length() != s2.length())
			return false;
		
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		char[] c1 = s1.toCharArray();
		char[] c2 = s2.toCharArray();
		
		for(int i=0; i<c1.length; i++) {
			if(map.containsKey(c1[i])) {
				map.put(c1[i], (Integer) map.get(c1[i]) + 1);
			} else {
				map.put(c1[i], 1);
			}
		}
		
		for(int i=0; i<c2.length; i++) {
			if(map.containsKey(c2[i])) {
				int count = map.get(c2[i]);
				if(count>1) {
					map.put(c2[i], count-1);
				} else {
					map.remove(c2[i]);
				}
			} else {
				return false;
			}
			//FIXED - //VB : What if map does not contain the character. Shouldnt you return false? 
		}
		
		if (map.isEmpty()) {
			return true;
		} else {
			return false;
		}
		
	}
	
	/*
	 * Write a method to replace all spaces in a string with �%20�
	 */
	public String fillSpaces(String s) {
		
		if(null==s || s.isEmpty())
			return s;

		// return s.replaceAll(" ", "%20");     // -- method 1
		
		for(int i=0; i<s.length(); i++) {
			if(s.charAt(i) == ' ') {
				s = s.substring(0, i) + "%20" + s.substring(i+1, s.length());
			}
		}
		
		return s;
	}
	
	public String fillSpaces1(String s) { // using arrays
		
		if(null==s || s.isEmpty())
			return s;

		char[] c = s.toCharArray();
		
		int x = 0;
		for(int i=0; i<c.length; i++) {
			if(c[i] == ' ') {
				x++;
			}
		}
		if(x>0) {
			char[] cNew = new char[c.length + (x*3)];
			int cNewPointer = cNew.length-1;
			for(int i= (c.length-1); i>=0; i-- ) {
				if(c[i] == ' ') {
					cNew[cNewPointer] = '0';
					cNewPointer--;
					cNew[cNewPointer] = '2';
					cNewPointer--;
					cNew[cNewPointer] = '%';
					cNewPointer--;
				} else {
					cNew[cNewPointer] = c[i];
					cNewPointer--;
				}
			}
			StringBuilder sb = new StringBuilder();
			for(int i= 0; i<cNew.length; i++) {
				sb.append(cNew[i]);
			}
			s = sb.toString();
		}
		return s;
	}
	
	/*
	 * Given an image represented by an NxN matrix, where each pixel in the image is 4 bytes, 
	 * write a method to rotate the image by 90 degrees Can you do this in place?
	 */
	
	// TODO this is wrong answer
	//VB : Verify if there is a correct answer 
	public Integer[][] rotateBy90Degree(Integer[][] data, int matrixSize) {
		if(null==data)
			return null;
		
		for(int i=0; i<(matrixSize/2); i++ ) {
			
			for(int j=(matrixSize/2 + 1); j>0; j-- ) {
				
				Integer temp = data[i][j];
				
				data[i][j] = data[j][i];
				data[j][i] = temp;
			}
		}
		
		return data;
	}
	
	private void toStringMatrix(T[][] data, int size) {
		StringBuilder sb;
		for(int i=0; i<size; i++) {
			sb = new StringBuilder();
			for(int j=0; j<size; j++) {
				sb.append(data[i][j] + " ");
			}
			System.out.println(sb);
		}
		
	}
	
	/*
	 * Assume you have a method isSubstring which checks if one word is a substring of another 
	 * Given two strings, s1 and s2, write code to check if s2 is a rotation of s1 
	 * using only one call to isSubstring (i e , �waterbottle� is a rotation of �erbottlewat�) 
	 */
	
	public boolean isRotation(String s1, String s2) {
		
		if((null == s1) || (null == s2))
			return false;
		
		if(s1.length() != s2.length())
			return false;
		
		if(areAnagrams(s1, s2)) {      //VB You should not use areAnagrams for this question
			
			// VB : this will fail or abcad, adabc
			int start = s2.indexOf(s1.charAt(0));
			String sub1 = s2.substring(start);
			String sub2 = s2.substring(0, start);
			String s3 = sub1 + sub2;
			if(s3.equals(s1)) {
				return true;
			}
			
		} else {
			return false;
		}
		
		return false;
	}
	
	public boolean isRotation1(String s1, String s2) {
		
		if((null == s1) || (null == s2))
			return false;
		
		if(s1.length() != s2.length())
			return false;
		
		int i = s2.indexOf(s1.charAt(0));
		
		System.out.println(s2.substring(0,i) + ":" + s2.substring(i));

		//("waterbottle","erbottlewat")
		if((isSubstring(s2.substring(i), s1) ) && (isSubstring(s2.substring(0,i), s1) )) {
			return true;
		}
		
		return false;
	}
	
	
	private boolean isSubstring(String subString, String givenString) {
		
		if((null == subString) || (null == givenString))
			return false;
		
		if(subString.length() > givenString.length())
			return false;
		
		if(subString.equals(givenString))
			return true;
		
		int startPointer = -1;
		
		for(int i=0; i<givenString.length(); i++) {
			
			if(givenString.charAt(i) == subString.charAt(0)) {
				startPointer = i;
				
				if ((startPointer + subString.length()) > givenString.length()) {
					return false;
				}
				
				int x = 0;
				boolean flag = true;
				
				while (x<subString.length()) {
					if(givenString.charAt(i+x) != subString.charAt(x)) {
						flag = false;
						break;
					}
					x++;
				}
				if(flag) {
					return true;
				}
				
			}
		}		
		
		return false;
	}
	
	public static void main(String[] args) {
		ArrayAndStringCareerCup a = new ArrayAndStringCareerCup();
		System.out.println(a.isRotation1("waterbotlle","erbottlewat"));
	}
	
	public static void main1(String[] args) {
		
		ArrayAndStringCareerCup a = new ArrayAndStringCareerCup();
		
		Assert.assertTrue(a.allCharactersUnique("string"));
		Assert.assertFalse(a.allCharactersUnique("strings"));
		
		Assert.assertEquals("sgnirts", a.reverse("strings"));
		Assert.assertEquals("", a.reverse(""));
		Assert.assertNull(a.reverse(null));
		
		Assert.assertEquals("string", a.removeDuplicateCharacters("sttriings"));
		Assert.assertNull(a.removeDuplicateCharacters(null));
		Assert.assertEquals("", a.removeDuplicateCharacters(""));
		
		System.out.println("..." + a.removeDuplicateCharacters("sssssss"));
		// VB : The below 2 testcases do not work
		Assert.assertEquals("s", a.removeDuplicateCharacters("sssssss"));
		Assert.assertEquals("string", a.removeDuplicateCharacters("sttriingssssss"));

		Assert.assertTrue(a.areAnagrams("dormitory", "dirtyroom"));	
		Assert.assertFalse(a.areAnagrams("dorm", "dirtyroom"));
		
		//Assert.assertTrue(a.fillSpaces("this is a string with spaces"));
		
		Assert.assertEquals("this%20is%20a%20string%20with%20spaces", a.fillSpaces("this is a string with spaces"));
		Assert.assertNotSame("this is a string with spaces", a.fillSpaces("this is a string with spaces"));
		
		/*
		
		Integer[][] data = new Integer[4][4];
		int count = 0;
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				count++;
				data[i][j] = count;
			}
		}
		a.toStringMatrix(data, 4);
		System.out.println();
		a.toStringMatrix(a.rotateBy90Degree(data, 4), 4);
		*/
		
		Assert.assertTrue(a.isSubstring("substring", "substring"));
		
		Assert.assertTrue(a.isRotation("waterblttoe","erbottlewat"));
		
	}

}
