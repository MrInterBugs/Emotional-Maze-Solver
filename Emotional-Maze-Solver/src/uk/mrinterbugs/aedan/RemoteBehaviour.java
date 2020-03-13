package uk.mrinterbugs.aedan;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class RemoteBehaviour implements Behavior{
	private static String input;
	private static String current;
	private MovePilot pilot;

	public RemoteBehaviour(MovePilot pilot) {
        this.pilot = pilot;
    }

	@Override
	public boolean takeControl() {
		input = Remote.getInput();
		if(input != "") {
			current = input;
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		switch (current) {
        case "up":
            if (!pilot.isMoving()) {
                pilot.forward();
            }
            break;
        case "stop":
            pilot.stop();
            break;
        case "right":
            pilot.rotate(-90);
            break;
        case "left":
            pilot.rotate(90);
            break;
        case "down":
            if (!pilot.isMoving()) {
                pilot.backward();
            }
            break;                   
		}
		current = "";
		Remote.setInput();
	}

	@Override
	public void suppress() {
	}

}
