package uk.mrinterbugs.aedan;

import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

public class DontTouch implements Behavior {
	private String DONT_TOUCH = "DontTouch.wav";
	private SampleProvider touch;

	public DontTouch(SampleProvider touch) {
		this.touch = touch;
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void action() {
		(new PlaySound(DONT_TOUCH)).start();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
