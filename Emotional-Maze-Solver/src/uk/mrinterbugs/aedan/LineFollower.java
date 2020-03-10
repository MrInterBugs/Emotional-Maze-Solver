package uk.mrinterbugs.aedan;

import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public class LineFollower implements Behavior {
	private Navigator navi;
	private SampleProvider colorSampler;
	private float[] lightLevels;
	private int whiteIndex = 0;
	private int blackIndex = 1;
	private float averageLight;
	private int slowSpeed = 10;
	private int mediumSpeed = slowSpeed * 2;
	private int adjustmentsLeft = 0;
	private int adjustmentsRight = 0;

	public LineFollower(Navigator navi, SampleProvider colorSampler, float[] lightLevels) {
		this.navi = navi;
		this.colorSampler = colorSampler;
		this.lightLevels = lightLevels;
		this.averageLight = (lightLevels[whiteIndex] + lightLevels[blackIndex]) / 2;
		this.getNavigatorMoveController().setLinearSpeed(mediumSpeed);
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		followLeftWall();
	}
	
	private void followLeftWall() {
		float currentHeading = this.getCurrentHeading();
		getNavigatorMoveController().travel(25, false);
		if(this.onDarkSurface()) {
			System.out.println("dark");
			getNavi().rotateTo(currentHeading + 2);
			incrementAdjustmentsLeft();
			setAdjustmentsRight(0);
			LCD.clear();
		} else {	
			System.out.println("light");
			getNavi().rotateTo(currentHeading - 2);
			incrementAdjustmentsRight();
			setAdjustmentsLeft(0);
			LCD.clear();
		}
	}

	public void suppress() {

	}
	
	private void rotateLeft() {
		getNavi().rotateTo(getCurrentHeading() - 90);
	}

	private boolean onDarkSurface() {
		return getLightSample() < getAverageLight();
	}

	private float getLightSample() {
		float lightSample[] = new float[1];
		this.getColorSampler().fetchSample(lightSample, 0);
		return lightSample[0];
	}
	
	private MoveController getNavigatorMoveController() {
		return this.navi.getMoveController();
	}
	
	private float getCurrentHeading() {
		return getNavi().getPoseProvider().getPose().getHeading();
	}

	private float getAverageLight() {
		return this.averageLight;
	}

	private void setAverageLight(float averageLight) {
		this.averageLight = averageLight;
	}

	private Navigator getNavi() {
		return navi;
	}

	private void setNavi(Navigator navi) {
		this.navi = navi;
	}

	private SampleProvider getColorSampler() {
		return colorSampler;
	}

	private void setColorSampler(SampleProvider colorSampler) {
		this.colorSampler = colorSampler;
	}

	private float[] getLightLevels() {
		return lightLevels;
	}

	private void setLightLevels(float[] lightLevels) {
		this.lightLevels = lightLevels;
	}

	private int getWhiteIndex() {
		return whiteIndex;
	}

	private void setWhiteIndex(int whiteIndex) {
		this.whiteIndex = whiteIndex;
	}

	private int getBlackIndex() {
		return blackIndex;
	}

	private void setBlackIndex(int blackIndex) {
		this.blackIndex = blackIndex;
	}
	
	private void incrementAdjustmentsLeft() {
		this.setAdjustmentsLeft(getAdjustmentsLeft() + 1);
	}

	private int getAdjustmentsLeft() {
		return adjustmentsLeft;
	}

	private void setAdjustmentsLeft(int adjustmentsLeft) {
		this.adjustmentsLeft = adjustmentsLeft;
	}
	
	private void incrementAdjustmentsRight() {
		this.setAdjustmentsRight(getAdjustmentsRight() + 1);
	}

	private int getAdjustmentsRight() {
		return adjustmentsRight;
	}

	private void setAdjustmentsRight(int adjustmentsRight) {
		this.adjustmentsRight = adjustmentsRight;
	}
}
