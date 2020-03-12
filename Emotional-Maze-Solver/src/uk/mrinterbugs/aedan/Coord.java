package uk.mrinterbugs.aedan;
import java.util.ArrayList;

public class Coord {
	private ArrayList<ArrayList<Integer>> list;
	
	public Coord() {
		this.list = new ArrayList<>();
	}
	
	public void add(int coord1,int coord2) {
		// add new elements to the main arraylist
		this.list.add(new ArrayList<Integer>());
		int curr = this.list.size()-1;
		
		this.list.get(curr).add(coord1);
		this.list.get(curr).add(coord2);
		this.list.get(curr).add(0);
	}
	
	public void markoff(int coord1,int coord2) {
		//test arraylist used to find the index of the coords in list arraylist
		ArrayList<Integer> test = compareArr(coord1,coord2,0);
		
		int index = list.indexOf(test);
		//sets the 3rd element of the arraylist to 1 showing it has been marked off
		this.list.get(index).set(2, 1);	
	}

	//method checks if coords have been visited once,twice(marked off) or never
	public int checkvisited(int coord1,int coord2) {
		ArrayList<Integer> once = compareArr(coord1,coord2,0);
		ArrayList<Integer> twice= compareArr(coord1,coord2,1);
		
		//return 0 if the coords have been visited once but not marked off
		if(list.indexOf(once)>=0) {return 0;}
		
		//return 1 if the coords have been marked off
		if(list.indexOf(twice)>=0) {return 1;}
	
		//returns -1 if the coords have not been visited
		return -1;
	}
	
	//prints out the coordinates that have only been visited once
	public void print() {
		for(int x=0;x<this.list.size();x++) {
			if(this.list.get(x).get(2)==0) {
				System.out.println(this.list.get(x).get(0) +" "+ this.list.get(x).get(1));
			}
		}
	}
	

	//method to create an arraylist to find index of coords in list (only to be used in methods above)
	private ArrayList<Integer> compareArr(int coord1,int coord2,int stat) {
		ArrayList<Integer> test = new ArrayList<>();
		test.add(coord1);
		test.add(coord2);
		test.add(stat);
		return test;
	}	
}
