package uk.mrinterbugs.aedan;

import lejos.hardware.Sound;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;

public class Emotions {
	private Navigator navi;
	private MovePilot pilot;
	private SampleProvider touch;
	private SampleProvider sound;
	
	public Emotions (MovePilot pilot, Navigator navi, SampleProvider touch, SampleProvider sound) {
		this.navi = navi;
		this.pilot = pilot;
		this.touch = touch;
		this.sound = sound;
	}
	public static void END() {
		Executable.close();
	}
	
	public static void HAPPY() {
		
	}
	
	public static void SAD() {
		Sound.beep();
	}
	
	public static void SNORLAX() {
		
	}

}
