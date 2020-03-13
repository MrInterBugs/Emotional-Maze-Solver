package uk.mrinterbugs.aedan;

import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

/**
 * 
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

	@Override
	public boolean takeControl() {
		input = Remote.getInput();
		String[] inputArray = input.split("\\s+");
		System.out.println(inputArray[0]);
		if(inputArray[0].equals("QR:")) {
			System.out.println(inputArray[1]);
			current = inputArray[1];
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		switch (current) {
        case "END":
        	System.out.println("Fuck Yeah");
        	Sound.beep();
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
		// TODO Auto-generated method stub
		
	}

}
