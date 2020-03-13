package uk.mrinterbugs.aedan;

import java.io.File;

import lejos.hardware.Sound;

/**
 * 
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
	
	public PlaySound(String filename) {
		this.filename = filename;
	}
		
    public void run() {
    	playTune(); 
    }

	private void playTune() {
		int time = Sound.playSample(new File(this.filename));
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}