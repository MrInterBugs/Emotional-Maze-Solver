package uk.mrinterbugs.aedan;

import java.io.File;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class PlaySound extends Thread {
	private String filename;
	
	public PlaySound(String filename) {
		this.filename = filename;
	}
		
    public void run() {
    	Sound.playSample(new File(this.filename));
    }
}