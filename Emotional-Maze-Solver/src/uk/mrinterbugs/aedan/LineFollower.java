package uk.mrinterbugs.aedan;

import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public class LineFollower implements Behavior {
	private Navigator navi;
	private SampleProvider colorSampler;
	private final float[] lightLevels;
	private final int whiteIndex = 0;
	private final int blackIndex = 1;
	private final float averageLight;
	private final int slowSpeed = 10;
	private final int mediumSpeed = slowSpeed * 2;
	private int adjustmentsLeft = 0;
	private int adjustmentsRight = 0;
	private final int rotationCorrectionDegree = 2;

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
		getNavigatorMoveController().travel(10, false);
		if(getConsecutiveAdjustments() < 4) {
			if(this.onDarkSurface()) {
				System.out.println("dark");
				rotateClockwiseDegrees(rotationCorrectionDegree);
				incrementAdjustmentsLeft();
				setAdjustmentsRight(0);
				LCD.clear();
			} else {	
				System.out.println("light");
				rotateClockwiseDegrees(-rotationCorrectionDegree);
				incrementAdjustmentsRight();
				setAdjustmentsLeft(0);
				LCD.clear();
			}
		} else {
			findTurn();
		}
	}

	public void suppress() {

	}
	
	private void findTurn() {
		getNavigatorMoveController().travel(37, false);
		correctRotation();
		rotateLeft();
		if(!this.onDarkSurface()) {
			rotateClockwiseDegrees(180);
			if(!this.onDarkSurface()) {
				rotateLeft();
			}
		}
	}
	
	private void correctRotation() {
		if(this.getAdjustmentsLeft() > 0) {
			rotateClockwiseDegrees(rotationCorrectionDegree * getAdjustmentsLeft());
		} else if(this.getAdjustmentsRight() > 0) {
			rotateClockwiseDegrees(rotationCorrectionDegree * getAdjustmentsRight());
		}
		this.setAdjustmentsLeft(0);
		this.setAdjustmentsRight(0);
	}
	
	private void rotateLeft() {
		rotateClockwiseDegrees(-90);
	}
	
	private void rotateRight() {
		rotateClockwiseDegrees(90);
	}

	private boolean onDarkSurface() {
		System.out.println(getLightSample());
		System.out.println(getAverageLight());
		return getLightSample() <= getAverageLight();
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
	
	private void rotateClockwiseDegrees(int degrees) {
		getNavi().rotateTo(getCurrentHeading() + degrees);
	}

	private float getAverageLight() {
		return this.averageLight;
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

	private int getWhiteIndex() {
		return whiteIndex;
	}

	private int getBlackIndex() {
		return blackIndex;
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
	
	private int getConsecutiveAdjustments() {
		int rightAdjustments = getAdjustmentsRight();
		int leftAdjustments = getAdjustmentsLeft();
		if(rightAdjustments > leftAdjustments) {
			return rightAdjustments;
		} else {
			return leftAdjustments;
		}
	}
}
