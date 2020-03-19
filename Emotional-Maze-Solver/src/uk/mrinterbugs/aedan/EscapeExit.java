package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
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
	private EV3TouchSensor ts = new EV3TouchSensor(SensorPort.S3);
    private SampleProvider touch = ts.getTouchMode();

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
				float[] sample = new float[1];
				touch.fetchSample(sample, 0);
				if(Button.ESCAPE.isDown()) {
					System.exit(0);
				}
			}
		}
	}
}
