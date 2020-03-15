package uk.mrinterbugs.aedan;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

/**
 * This class allows for the robot to be controlled from the android app remotely it gets the string from the Remote class.
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Joules James
 * 
 * @version 0.5
 * @since 2020-02-27
 */
public class RemoteBehaviour implements Behavior{
	private static String input;
	private static String current;
	private MovePilot pilot;

	/**
     * Constructor to allow the passing of MovePilot.
     * 
     * @param pilot the main MovePilot.
     */
	public RemoteBehaviour(MovePilot pilot) {
        this.pilot = pilot;
    }

	/**
	 * Take control will return true if the input equals up, down, left, right, stop. Else it will return false.
	 */
	@Override
	public boolean takeControl() {
		input = Remote.getInput();
		if(input.equals("up") || input.equals("stop") || input.equals("right") || input.equals("left") || input.equals("down")) {
			current = input;
			return true;
		}
		return false;
	}

	/**
	 * The action will change based on the input, once the action is run it will clear the input from the Remote class so the action can only be run once.
	 */
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
