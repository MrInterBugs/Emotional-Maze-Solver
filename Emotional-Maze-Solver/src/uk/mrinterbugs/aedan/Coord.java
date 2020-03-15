package uk.mrinterbugs.aedan;

import java.util.ArrayList;
/**
 * Used to create a list of coordinates and keep track of how much they have been visited for the pose provider.
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Joules James
 * 
 * @version 0.5
 * @since 2020-03-04
 */
public class Coord {
	private ArrayList<ArrayList<Integer>> list = new ArrayList<>();;

	/**
	 * Method to add two new coordinates to the Array.
	 * 
	 * @param coord1 The x axis cord to add to the array.
	 * @param coord2 The y axis cord to add to the array.
	 */
	public void add(int coord1, int coord2) {
		this.list.add(new ArrayList<Integer>());
		int curr = this.list.size()-1;
		
		this.list.get(curr).add(coord1);
		this.list.get(curr).add(coord2);
		this.list.get(curr).add(0);
	}
	
	/**
	 * Sets the 3rd element of the array list to 1 showing it has been marked off
	 * 
	 * @param coord1 The x axis cord to add to the array.
	 * @param coord2 The y axis cord to add to the array.
	 */
	public void markoff(int coord1, int coord2) {
		ArrayList<Integer> test = compareArr(coord1,coord2,0);
		int index = list.indexOf(test);
		this.list.get(index).set(2, 1);	
	}

	/**
	 * method checks if coordinates have been visited once,twice(marked off) or never.
	 * 
	 * @param coord1 The x axis cord to add to the array.
	 * @param coord2 The y axis cord to add to the array.
	 * @return 0 if visited but not marked off, 1 if it has been marked off and -1 if it has not been visited at all.
	 */
	public int checkvisited(int coord1, int coord2) {
		ArrayList<Integer> once = compareArr(coord1,coord2,0);
		ArrayList<Integer> twice= compareArr(coord1,coord2,1);
		
		if(list.indexOf(once)>=0) {return 0;}
		if(list.indexOf(twice)>=0) {return 1;}
		return -1;
	}

	/**
	 * method to create an array list to find index of coordinates in list (only to be used in methods above).
	 * 
	 * @param coord1 The x axis cord to add to the array.
	 * @param coord2 The y axis cord to add to the array.
	 * @param stat The amount of times these coordinates have been visited.
	 * @return The array of the three inputs.
	 */
	private ArrayList<Integer> compareArr(int coord1, int coord2, int stat) {
		ArrayList<Integer> test = new ArrayList<>();
		test.add(coord1);
		test.add(coord2);
		test.add(stat);
		return test;
	}	
}
