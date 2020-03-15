package uk.mrinterbugs.aedan;

import java.io.File;

import lejos.hardware.Sound;

/**
 * Creates a threaded instant to play a .wav file.
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Joules James
 * 
 * @version 0.5
 * @since 2020-02-17
 */
public class PlaySound extends Thread {
	private String filename;
	
	/**
	 * Constructor to pass through the name of the file to be played "JohnDoe.wav"
	 */
	public PlaySound(String filename) {
		this.filename = filename;
	}
	
	/**
	 * When the thread is called it will play the sound file then sleep untill the file has fully played.
	 */
    public void run() {
    	int time = Sound.playSample(new File(this.filename));
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}