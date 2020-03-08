package uk.mrinterbugs.aedan;

public class testcoord {

	public static void main(String[] args) {
		Coord coordinates = new Coord();
		coordinates.add(5,2);
		coordinates.add(7, 4);
		coordinates.markoff(7,4);

		coordinates.checkvisited(7, 3);
		coordinates.checkvisited(7, 4);
		coordinates.checkvisited(5,2);
		
		

	}

}
