package uk.mrinterbugs.aedan;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class RemoteBehaviour implements Behavior{
	private static String input;
	private MovePilot pilot;

	public RemoteBehaviour(MovePilot pilot) {
        this.pilot = pilot;
    }

	@Override
	public boolean takeControl() {
		input = Remote.getInput();
		switch (input) {
        case "up":
            return true;
        case "stop":
        	return true;
        case "right":
        	return true;
        case "left":
        	return true;
        case "down":
        	return true;                 
		}
		return false;
	}

	@Override
	public void action() {
		switch (input) {
        case "up":
            if (!pilot.isMoving()) {
                pilot.forward();
            }
            input = "";
            break;
        case "stop":
            pilot.stop();
            input = "";
            break;
        case "right":
            pilot.rotate(-90);
            input = "";
            break;
        case "left":
            pilot.rotate(90);
            input = "";
            break;
        case "down":
            if (!pilot.isMoving()) {
                pilot.backward();
            }
            input = "";
            break;                   
		}	
	}

	@Override
	public void suppress() {
	}

}
