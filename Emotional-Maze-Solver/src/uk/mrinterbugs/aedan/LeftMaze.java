package uk.mrinterbugs.aedan;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
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
	private boolean hasFoundLine = true;
	private BaseRegulatedMotor sensorMotor;
	private final int incrementalTravelDistance = 12; // About 1 centimetre on paper
	private final int alignWheelTravelDistance = 82; // Distance to move forward before making turn
	private final int whiteIndex = 0;
	private final int blackIndex = 1;
	private final int maxConsecutiveAdjustments = 4;
	private final float averageLight;
	private final int slowSpeed = 17;
	private final int mediumSpeed = slowSpeed * 2;
	private int adjustmentsLeft = 0;
	private int adjustmentsRight = 0;
	private final int rotationCorrectionDegree = 3;

	/**
	 * Constructor which sets average light level from given light and dark levels.
	 * Also sets the speed of the robot.
	 * 
	 * @param navi Allows control of the robots speed and travel.
	 * @param colorSampler Check reflected light levels to see if the line is present or not.
	 * @param lightLevels An array of average values so the colour sampler can be calibrated.
	 */
	public LeftMaze(Navigator navi, SampleProvider colorSampler, float[] lightLevels, BaseRegulatedMotor sensorMotor) {
		this.navi = navi;
		this.colorSampler = colorSampler;
		this.sensorMotor = sensorMotor;
		averageLight = (lightLevels[whiteIndex] + lightLevels[blackIndex]) / 2;
		navi.getMoveController().setLinearSpeed(mediumSpeed);
	}

	/**
	 * This is our main behaviour that should be always active unless suppressed or the maze has been completed.
	 */
	@Override
	public boolean takeControl() {
		return true;
	}

	/**
	 * When takeControl() is true action will call the followLeftWall method.
	 * 
	 * @see followLeftWall
	 */
	@Override
	public void action() {
		if(this.hasFoundLine()) {
			followLeftWall();
		} else {
			findLine();
		}
	}
	
	private void findLine() {
		this.getSensorMotor().rotateTo(0);
		boolean initiallyDark = this.onDarkSurface();
		int rotationsToMake = 3;
		for(int rotations = 0; rotations <= rotationsToMake; rotations++) {
			if(initiallyDark && this.onDarkSurface()) {
				this.rotateClockwiseDegrees(rotationCorrectionDegree);
			} else if(!initiallyDark && !this.onDarkSurface()) {
				this.rotateClockwiseDegrees(-rotationCorrectionDegree);
			} else {
				this.setHasFoundLine(true);
				break;
			}
		}
	}
	
	private boolean hasFoundLine() {
		return this.hasFoundLine;
	}
	
	private void setHasFoundLine(boolean hasFoundLine) {
		this.hasFoundLine = hasFoundLine;
	}
	
	/**
	 * 
	 * @return -1, 0, 1, or 2 for a left, forward, right, or no path respectively with priority in that order.
	 */
	private int detectNextMovePath() {
		int leftRotationAngle = -45;
		int rightRotationAngle = -90;
		boolean forwardPossible = this.getConsecutiveAdjustments() < this.getMaxConsecutiveAdjustments();
		boolean  rightPossible = false;
		
		
		this.getSensorMotor().rotateTo(-leftRotationAngle);
		if(this.onDarkSurface()) {
			return -1;
		}
		
		this.getSensorMotor().rotateTo(rightRotationAngle);
		if(this.onDarkSurface()) {
			rightPossible = true;
		}
		
		if(forwardPossible) {
			return 0;
		} else if(rightPossible) {
			return 1;
		} else {
			return 2;
		}
	}
	
	public void travelForwardShortStep() {
		this.getNavi().getMoveController().travel(this.getIncrementalTravelDistancee());
	}
	
	public void travelForwardAlignWheels() {
		this.getNavi().getMoveController().travel(this.getAlignWheelTravelDistance());
	}
	
	/**
	 * Moves forward a small amount and takes a reading from the colour sensor.
	 * If this reading is not the same surface as the last 3 readings then it will correct to be as close the edge of the line as possible.
	 * However if it is the forth reading it will assume it has come to a corner and call the findTurn method.
	 * 
	 * @see findTurn
	 */
	private void followLeftWall() {
		int nextPath = detectNextMovePath();
		
		final int left = -1;
		final int forward = 0;
		final int right = 1;
		
		if(nextPath == left || nextPath == right) {
			this.travelForwardAlignWheels();
			correctRotation();
		}
		
		switch(nextPath) {
		case left: // Left turn
			rotateLeft();
			this.setHasFoundLine(false);
			break;
		case forward:
			this.getSensorMotor().rotateTo(0);
			this.travelForwardShortStep();
			if(this.onDarkSurface()) {
				rotateClockwiseDegrees(rotationCorrectionDegree);
				setAdjustmentsRight(0);
				incrementAdjustmentsLeft();
			} else {
				rotateClockwiseDegrees(-rotationCorrectionDegree);
				setAdjustmentsLeft(0);
				incrementAdjustmentsRight();
			}
			break;
		case right:
			rotateRight();
			this.setHasFoundLine(false);
			break;
		default: // Turn around
			rotateClockwiseDegrees(195);
			setAdjustmentsLeft(0);
			setAdjustmentsRight(0);
			this.setHasFoundLine(false);
			break;
		}
	}

	public void suppress() {

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
		rotateClockwiseDegrees(90);
	}
	
	/**
	 * Makes the robot turn right 90 degrees.
	 */
	private void rotateRight() {
		rotateClockwiseDegrees(-90);
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

	private int getMaxConsecutiveAdjustments() {
		return maxConsecutiveAdjustments;
	}

	private int getAlignWheelTravelDistance() {
		return this.alignWheelTravelDistance;
	}
	
	private int getIncrementalTravelDistancee() {
		return incrementalTravelDistance;
	}

	private BaseRegulatedMotor getSensorMotor() {
		return sensorMotor;
	}
}
