package uk.mrinterbugs.aedan;

import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

/**
 * This class works very similarly to RemoteBehaviour however it uses QR codes instead of user supplied inputs.
 * @see RemoteBehaviour
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Joules James
 * 
 * @version 0.5
 * @since 2020-03-13
 */
public class QRHandler implements Behavior{
	private static String input;
	private static String current;

	/**
	 * This behaviour will only take control if the string starts with "QR: ". Else it will return false.
	 */
	@Override
	public boolean takeControl() {
		input = Remote.getInput();
		String[] inputArray = input.split("\\s+");
		LCD.drawString(inputArray[0], 0, 4);
		if(inputArray[0].equals("QR:")) {
			System.out.println(inputArray[1]);
			current = inputArray[1];
			return true;
		}
		return false;
	}

	/**
	 * Based on which QR is scanned different methods will me called from the Emotions class.
	 */
	@Override
	public void action() {
		switch (current) {
        case "END":
            break;
        case "HAPPY":
            break;
        case "SAD":    
            break;
        case "SNORLAX": 
            break;
        case "CLAP":
            break;                   
		}
		current = "";
		Remote.setInput();	
	}

	@Override
	public void suppress() {
		
	}
}
