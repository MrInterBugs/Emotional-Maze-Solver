package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;

public class SensorCalibration {
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
    
    public static float calibrateSoundSensor(SampleProvider soundSampler) {
    	float[] backgroundNoiseLevel = new float[1];
    	LCD.drawString("Press enter to", 2, 2);
    	LCD.drawString("calibrate sound", 2, 3);
    	
    	backgroundNoiseLevel[0] = averageReadings(soundSampler);
    	backgroundNoiseLevel[0] += 0.1;
    	return backgroundNoiseLevel[0];
    }
    
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
