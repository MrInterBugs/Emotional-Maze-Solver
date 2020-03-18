package uk.mrinterbugs.aedan;

import lejos.robotics.subsumption.Behavior;

public class EmotionalMazeSolver implements Behavior{
	private LeftMaze leftmaze;
	private QRHandler qrhandler;
	private LowBattery lowbattery;
	private EscapeExit escapeexit;
	private RemoteControll remotecontroll;
	
	public EmotionalMazeSolver(LeftMaze leftmaze, EscapeExit escapeexit, LowBattery lowbattery, QRHandler qrhandler, RemoteControll remotecontroll) {
		this.leftmaze = leftmaze;
		this.escapeexit = escapeexit;
		this.qrhandler = qrhandler;
		this.remotecontroll = remotecontroll;
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
		lowbattery.action();
		remotecontroll.action();
		qrhandler.action();
	}

	@Override
	public void suppress() {
		leftmaze.suppress();
		escapeexit.suppress();
		lowbattery.suppress();
		remotecontroll.suppress();
		qrhandler.suppress();
	}

}
