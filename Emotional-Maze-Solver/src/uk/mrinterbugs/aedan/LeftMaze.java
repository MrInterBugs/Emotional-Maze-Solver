package uk.mrinterbugs.aedan;

import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

/**
 * This is our initial attempt at solving a physical maze. 
 * Using a light sensor the robot will follow a black line on a white background.
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Joules James
 * 
 * @version 0.5
 * @since 2020-03-01
 */
public class LeftMaze implements Behavior {
	private Navigator navi;
	private SampleProvider colorSampler;
	private final int whiteIndex = 0;
	private final int blackIndex = 1;
	private final float averageLight;
	private final int slowSpeed = 10;
	private final int mediumSpeed = slowSpeed * 2;
	private int adjustmentsLeft = 0;
	private int adjustmentsRight = 0;
	private final int rotationCorrectionDegree = 2;

	/**
	 * Constructor which sets average light level from given light and dark levels.
	 * Also sets the speed of the robot.
	 * 
	 * @param navi Allows control of the robots speed and travel.
	 * @param colorSampler Check reflected light levels to see if the line is present or not.
	 * @param lightLevels An array of average values so the colour sampler can be calibrated.
	 */
	public LeftMaze(Navigator navi, SampleProvider colorSampler, float[] lightLevels) {
		this.navi = navi;
		this.colorSampler = colorSampler;
		averageLight = (lightLevels[whiteIndex] + lightLevels[blackIndex]) / 2;
		navi.getMoveController().setLinearSpeed(mediumSpeed);
	}

	/**
	 * This is our main behaviour that should be always active unless suppressed or the maze has been completed.
	 */
	@Override
	public boolean takeControl() {
		return false;
	}

	/**
	 * When takeControl() is true action will call the followLeftWall method.
	 * 
	 * @see followLeftWall
	 */
	@Override
	public void action() {
		followLeftWall();
	}
	
	/**
	 * Moves forward a small amount and takes a reading from the colour sensor.
	 * If this reading is not the same surface as the last 3 readings then it will correct to be as close the edge of the line as possible.
	 * However if it is the forth reading it will assume it has come to a corner and call the findTurn method.
	 * 
	 * @see findTurn
	 */
	private void followLeftWall() {
		navi.getMoveController().travel(10, false);
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
	
	/**
	 * This method allows the robot to find the line at a corner.
	 */
	private void findTurn() {
		navi.getMoveController().travel(37, false);
		correctRotation();
		rotateLeft();
		if(!this.onDarkSurface()) {
			rotateClockwiseDegrees(180);
			if(!this.onDarkSurface()) {
				rotateRight();
			}
		}
	}
	
	/**
	 * Due to small rotations to stay on the line this makes sure the 90 degree turns are done from 0, 90, 180 or 270.
	 */
	private void correctRotation() {
		if(this.getAdjustmentsLeft() > 0) {
			rotateClockwiseDegrees(rotationCorrectionDegree * getAdjustmentsLeft());
		} else if(this.getAdjustmentsRight() > 0) {
			rotateClockwiseDegrees(rotationCorrectionDegree * getAdjustmentsRight());
		}
		this.setAdjustmentsLeft(0);
		this.setAdjustmentsRight(0);
	}
	/**
	 * Makes the robot turn left 90 degrees.
	 */
	private void rotateLeft() {
		rotateClockwiseDegrees(-90);
	}
	
	/**
	 * Makes the robot turn right 90 degrees.
	 */
	private void rotateRight() {
		rotateClockwiseDegrees(90);
	}

	/**
	 * Checks the stored samples and compares the current one to see if it is on a dark surface.
	 * 
	 * @return true if on dark, false otherwise.
	 */
	private boolean onDarkSurface() {
		System.out.println(getLightSample());
		System.out.println(getAverageLight());
		return getLightSample() <= getAverageLight();
	}

	/**
	 * Gets the current reading from the colour sensor.
	 * 
	 * @return a float of the current light value from the colour sensor.
	 */
	private float getLightSample() {
		float lightSample[] = new float[1];
		this.getColorSampler().fetchSample(lightSample, 0);
		return lightSample[0];
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

	private SampleProvider getColorSampler() {
		return colorSampler;
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
