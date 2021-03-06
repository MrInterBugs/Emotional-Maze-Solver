package uk.mrinterbugs.aedan;

import lejos.hardware.Battery;
import lejos.robotics.subsumption.Behavior;

/**
 * Allows the user to press and hold the top left button (Escape) on the EV3 to stop the program.
 * Plays a threaded shutdown sound. Then calls System.exit(0).
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Jules James
 * 
 * @version 0.5
 * @since 2020-02-14
 */
public class LowBattery extends Thread implements Behavior {
	private boolean suppressed = false;
    /**
     * This behaviour will take control only when the escape button is pushed down.
     */
    @Override
    public boolean takeControl() {
        return true;
    }

    /**
     * When the escape button is pushed down the robot will play the shutdown sound, count down on screen, then exit.
     */
    @Override
    public synchronized void action() {
    	suppressed = false;
		notifyAll();
    }

    /**
     * We do not want to suppress this method to suppress if the escape key is pressed it must exit.
     */
    @Override
    public synchronized void suppress() {
    	suppressed = true;
		notifyAll();
    }
    
    public void run() {
		while(true) {
			if (!suppressed) {
				if(Battery.getVoltage() < 6.0f) {
					System.exit(0);
				}
			}
		}
	}
}
