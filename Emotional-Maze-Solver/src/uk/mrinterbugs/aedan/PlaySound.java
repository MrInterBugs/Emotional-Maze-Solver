package uk.mrinterbugs.aedan;

import java.io.File;

import lejos.hardware.Sound;

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