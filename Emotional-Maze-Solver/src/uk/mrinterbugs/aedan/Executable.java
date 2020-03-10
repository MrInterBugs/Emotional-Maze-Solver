package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Executable {
    private static final double WHEEL_DIAMETER = 56; // The diameter (mm) of the wheels
    private static final double AXLE_LENGTH = 120; // The distance (mm) your two driven wheels
    private static final String START_UP = "StartUpSound.wav";
    /**
     * Displays the program and version information until a button is pressed.
     * Also shows group members names.
     */
    public static void firstDisplay() {
    	(new PlaySound(START_UP)).start();
        LCD.drawString("Emotional Maze Solver",2,2);
        LCD.drawString("Version 0.1",2,3);
        LCD.drawString("Press Enter",2,5);
        Button.ENTER.waitForPressAndRelease();
        LCD.clear();
        LCD.drawString("Aedan Lawrence",2,2);
        LCD.drawString("Bruce Lay",2,3);
        LCD.drawString("Edmund Chee",2,4);
        LCD.drawString("Joules James",2,5);
        LCD.drawString("Press Enter",2,6);
        Button.ENTER.waitForPressAndRelease();
        LCD.clear();
    }
    
    private static float[] calibrateColorSensor(SampleProvider colorSampler) {
    	String[] calibrations = new String[]{"white", "black"};
    	float[] lightLevels = new float[calibrations.length];
    	
    	int index = 0;
    	for(String calibration: calibrations) {
    		LCD.drawString("Place sensor on", 2, 2);
    		LCD.drawString(calibration + " surface", 2, 3);
    		
    		LCD.drawString("Press enter to continue", 2, 4);
    		Button.ENTER.waitForPressAndRelease();
    		LCD.clear();
    		LCD.drawString("Calibrating", 2, 2);
    		lightLevels[index] = averageReadings(colorSampler);
    		LCD.clear();
    	}
    	
    	return lightLevels;
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

    public static void main(String[] args) {

        NXTSoundSensor ss = new NXTSoundSensor(SensorPort.S1);
        SampleProvider sound = ss.getDBAMode();
        //EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S2);
        //SampleProvider distance = us.getDistanceMode();
        
        EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
        SampleProvider color = cs.getRedMode();
        float lightLevels[] = calibrateColorSensor(color);
        
        EV3TouchSensor ts = new EV3TouchSensor(SensorPort.S4);
        SampleProvider touch = ts.getTouchMode();

        BaseRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
        Wheel leftWheel = WheeledChassis.modelWheel(leftMotor, WHEEL_DIAMETER).offset(AXLE_LENGTH/2);
        BaseRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
        Wheel rightWheel = WheeledChassis.modelWheel(rightMotor, WHEEL_DIAMETER).offset(-AXLE_LENGTH/2);

        Chassis chassis = new WheeledChassis(new Wheel[]{rightWheel,leftWheel},WheeledChassis.TYPE_DIFFERENTIAL);
        MovePilot pilot = new MovePilot(chassis);
        Navigator navi = new Navigator(pilot);

        firstDisplay();

        Behavior MoveForward = new MoveForward(navi);
        Behavior EscapeExit = new EscapeExit(navi);
        Behavior LowBattery = new LowBattery(navi);
        Behavior LineFollower = new LineFollower(navi, color, lightLevels);
        System.out.println(navi.getPoseProvider().getPose().getHeading());

        Behavior[] behaviorArray = {LineFollower, EscapeExit, LowBattery};

        Arbitrator arbitrator = new Arbitrator(behaviorArray);
        arbitrator.go();

        ss.close();
        //us.close();
        cs.close();
        ts.close();
        System.exit(0);
    }
}
