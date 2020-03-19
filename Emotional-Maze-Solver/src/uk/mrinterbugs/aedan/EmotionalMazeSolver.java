package uk.mrinterbugs.aedan;

import lejos.robotics.subsumption.Behavior;

public class EmotionalMazeSolver implements Behavior{
	private LeftMaze leftmaze;
	private QRHandler qrhandler;
	private LowBattery lowbattery;
	private EscapeExit escapeexit;
	
	public EmotionalMazeSolver(LeftMaze leftmaze, EscapeExit escapeexit, LowBattery lowbattery, QRHandler qrhandler) {
		this.leftmaze = leftmaze;
		this.escapeexit = escapeexit;
		this.qrhandler = qrhandler;
		this.lowbattery = lowbattery;
		leftmaze.start();
		escapeexit.start();
		lowbattery.start();
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		leftmaze.action();
		escapeexit.action();
		qrhandler.action();
		lowbattery.action();
	}

	@Override
	public void suppress() {
		leftmaze.suppress();
		escapeexit.suppress();
		qrhandler.suppress();
		lowbattery.suppress();
	}

}
