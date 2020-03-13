package uk.mrinterbugs.aedan;

import java.util.HashMap;
import java.util.Map;

public enum Direction {
	LEFT (90),
	FRONT (0),
	RIGHT (270),
	BACK(180);
	
	private final int degree;
	
	Direction(int degree) {
		this.degree = degree;
	}
	
	public static Direction valueOfDegree(int degree) {
		for(Direction direction: values()) {
			if(direction.getDegree() == degree) {
				return direction;
			}
		}
		return null;
	}

	public int getDegree() {
		return this.degree;
	}
	
	public boolean withinTenDegrees(int degree) {
		return(degree > getDegree() - 5 && degree < getDegree() + 5);
	}
}