package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;

/**
 * Allows the user to press and hold the top left button (Escape) on the EV3 to stop the program.
 * Calls System.exit(0).
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Jules James
 * 
 * @version 0.5
 * @since 2020-02-14
 */
public class EscapeExit extends Thread implements Behavior {
	private boolean suppressed = false;
  
    @Override
    public boolean takeControl() {
        return true;
    }

    @Override
    public synchronized void action() {
    	suppressed = false;
		notifyAll();
    }

    @Override
    public synchronized void suppress() {
    	suppressed = true;
		notifyAll();
    }
    
    public void run() {
		while(true) {
			if (!suppressed) {
				if(Button.ESCAPE.isDown()) {
					System.exit(0);
				}
			}
		}
	}
}
