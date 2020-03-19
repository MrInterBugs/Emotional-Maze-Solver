package uk.mrinterbugs.aedan;

import javax.net.ssl.SSLContext;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.SampleProvider;
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
	private AndroidConnection ac;
	private Navigator navi;
	private SampleProvider sound;
	private float soundlevel;
	private LeftMaze leftmaze;
	private float[] level;

	public QRHandler(Navigator navi, AndroidConnection ac, LeftMaze leftmaze, SampleProvider sound, float slevel) {
		this.navi = navi;
		this.ac =  ac;
		this.leftmaze = leftmaze;
		this.sound = sound;
		this.soundlevel = slevel;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public synchronized void action() {
		notifyAll();
		while (true) {
			try {
				try {
					input = ac.getInput();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String[] inputArray = input.split("\\s+");
				if(inputArray[0].equals("QR:")) {
					current = inputArray[1];
				}
				try {
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
			        	navi.getMoveController().setLinearSpeed(34);
			        	Sound.beep();
			        	break;
			        case "SNORLAX":
			        	(new Snorlax(this.sound,this.soundlevel,this.leftmaze)).run();
			        	break;
			        case "CLAP":
			        	navi.getMoveController().setLinearSpeed(17);
			        	Sound.beep();
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
		notifyAll();
	}
}
