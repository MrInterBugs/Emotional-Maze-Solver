package uk.mrinterbugs.aedan;

import lejos.hardware.Battery;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

/**
 * This class works very similarly to RemoteBehaviour however it uses QR codes instead of user supplied inputs.
 * @see RemoteBehaviour
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Jules James
 * 
 * @version 0.5
 * @since 2020-03-13
 */
public class QRHandler implements Behavior{
	private String input;
	private String current;
	private AndroidConnection ac = new AndroidConnection();
	private Navigator navi;
	private boolean suppressed = false;

	public QRHandler(Navigator navi) {
		this.navi = navi;
	}

	@Override
	public boolean takeControl() {
		suppressed = false;
		return true;
	}

	@Override
	public synchronized void action() {
		suppressed = false;
		notifyAll();
		ac.start();
		System.out.println(Battery.getVoltage());
		while (true) {
			try {
				try {
					input = ac.getInput();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LCD.drawString(input, 0, 2);
				String[] inputArray = input.split("\\s+");
				if(inputArray[0].equals("QR:")) {
					current = inputArray[1];
				}
				try {
					if(!current.equals("")) {
						LCD.drawString(current, 0, 3);
					}
					switch (current) {
			        case "END":
			        	LCD.drawString("END", 0, 4);
			        	Sound.beep();
			        	System.exit(0);
			            break;
			        case "HAPPY":
			        	LCD.clear();
			            break;
			        case "SAD": 
			        	LCD.clear();
			            break;
			        case "SNORLAX":
			        	navi.getMoveController().setLinearSpeed(34);
			            break;
			        case "CLAP":
			        	navi.getMoveController().setLinearSpeed(17);
			        	break;
					}
				} catch(NullPointerException ingored) {
				}
				current = "";
			} catch (NullPointerException ingored) {
			}
		}
		
	}

	@Override
	public synchronized void suppress() {
		suppressed = true;
		notifyAll();
	}
}
