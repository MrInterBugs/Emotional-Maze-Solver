package uk.mrinterbugs.aedan;

import java.io.File;

import lejos.hardware.Sound;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

/**
 * Creates a threaded instant to play a .wav file.
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Jules James
 * 
 * @version 0.5
 * @since 2020-02-17
 */
public class Snorlax extends Thread {

	private boolean suppressed = false;
	private SampleProvider sound;
	private float soundLevels;
	private String input;
	private String current;
	private boolean clapped = false;
	private LeftMaze leftmaze;
	private float level[];
	
	/**
	 * Constructor to pass through the name of the file to be played "JohnDoe.wav"
	 */
	public Snorlax(SampleProvider ss,float slevel,LeftMaze leftmaze) {
		this.sound = ss;
		this.soundLevels = slevel;
		this.leftmaze = leftmaze;
	}

	
	/**
	 * When the thread is called it will play the sound file then sleep untill the file has fully played.
	 */
    public void run() {
		String[] inputArray = input.split("\\s+");
		if(inputArray[0].equals("QR:")) {
			current = inputArray[1];}
		
		this.sound.fetchSample(level,0);
		while(level[0]<this.soundLevels) {
			this.leftmaze.suppress();
			this.sound.fetchSample(level,0);
		}
			
		this.leftmaze.action();
    }
}