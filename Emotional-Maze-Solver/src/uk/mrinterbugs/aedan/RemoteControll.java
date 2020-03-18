package uk.mrinterbugs.aedan;

import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class RemoteControll implements Behavior{
	private String input;
	private String current;
	private AndroidConnection ac;
	private MovePilot pilot;
	private LeftMaze leftmaze;

	public RemoteControll(MovePilot pilot, AndroidConnection ac, LeftMaze leftmaze) {
		this.pilot = pilot;
		this.ac =  ac;
		this.leftmaze = leftmaze;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public synchronized void action() {
		leftmaze.suppress();
		notifyAll();
		while (true) {
			try {
				try {
					input = ac.getInput();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LCD.drawString(input, 0, 2);
				current = input;
				try {
					if(!current.equals("")) {
						LCD.drawString(current, 0, 3);
					}
					switch (current) {
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
				} catch(NullPointerException ingored) {
				}
				current = "";
				leftmaze.action();
			} catch (NullPointerException ingored) {
			}
		}
		
	}

	@Override
	public synchronized void suppress() {
		notifyAll();
	}
}

