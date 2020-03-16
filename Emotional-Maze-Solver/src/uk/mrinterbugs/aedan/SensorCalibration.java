package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;

/**
 * This class is used to calibrate the sensors which w are using by getting average values.
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Jules James
 * 
 * @version 0.5
 * @since 2020-03-10
 */
public class SensorCalibration {
	
	/**
	 * Gets average values for white and black surfaces so we can tell the difference between the lines of the maze.
	 * 
	 * @param colorSampler the sampler used to get values.
	 * @return a float array including the white and black average value.
	 */
    public static float[] calibrateColorSensor(SampleProvider colorSampler) {
    	String[] calibrations = new String[]{"white", "black"};
    	float[] lightLevels = new float[calibrations.length];
    	
    	int index = 0;
    	for(String calibration: calibrations) {
    		LCD.drawString("Place sensor on", 2, 2);
    		LCD.drawString(calibration + " surface", 2, 3);
    		
    		LCD.drawString("Press enter", 2, 4);
    		LCD.drawString("to continue", 2, 5);
    		Button.ENTER.waitForPressAndRelease();
    		LCD.clear();
    		LCD.drawString("Calibrating", 2, 2);
    		lightLevels[index] = averageReadings(colorSampler);
    		index++;
    		LCD.clear();
    	}
    	return lightLevels;
    }
    
    /**
     * Used to calibrate the sound sensor to block out the noise floor.
     * 
     * @param soundSampler the sampler used to get values.
	 * @return a float of the background noise plus a little extra.
     */
    public static float calibrateSoundSensor(SampleProvider soundSampler) {
    	float[] backgroundNoiseLevel = new float[1];
    	LCD.drawString("Press enter to", 2, 2);
    	LCD.drawString("calibrate sound", 2, 3);
    	
    	backgroundNoiseLevel[0] = averageReadings(soundSampler);
    	backgroundNoiseLevel[0] += 0.1;
    	return backgroundNoiseLevel[0];
    }
    
    /**
     * This is the method used to get 100 readings from what ever sampleProvider is passed to it.
     * 
     * @param sampleProvider Can be any sampleProvider.
     * @return An average of the readings it took.
     */
    private static float averageReadings(SampleProvider sampleProvider) {
    	float average = 0;
    	int readings = 0;
    	float[] reading = new float[1];
    	for(int i=0; i<100; i++) {
    		sampleProvider.fetchSample(reading, 0);
    		readings++;
    		average = average + (reading[0] - average) / readings;
    	}
    	return average;
    }
}
