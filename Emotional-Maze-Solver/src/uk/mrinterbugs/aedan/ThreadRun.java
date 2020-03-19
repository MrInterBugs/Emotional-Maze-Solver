package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class ThreadRun {
    private static final double WHEEL_DIAMETER = 56;
    private static final double AXLE_LENGTH = 120;
	private static EV3ColorSensor cs;
	private static NXTSoundSensor ss;
    private static BaseRegulatedMotor sensorMotor;
    private static BaseRegulatedMotor leftMotor;
    private static BaseRegulatedMotor rightMotor;
    private static float lightLevels[];
    private static Navigator navi;
    private static SampleProvider color;
    private static SampleProvider sound;
    private static AndroidConnection ac = new AndroidConnection();
    private static MovePilot pilot;
    private static String START_UP = "StartUpSound.wav";
    private static float soundLevels;
    
    /**
     * Displays the program and version information until a button is pressed.
     * Also shows group members names.
     */
    public static void firstDisplay() {
    	LCD.drawString("Emotional Maze Solver",2,2);
        LCD.drawString("Version 0.5",2,3);
        LCD.drawString("Press Enter",2,5);
        Button.ENTER.waitForPressAndRelease();
        LCD.clear();
        LCD.drawString("Aedan Lawrence",2,2);
        LCD.drawString("Bruce Lay",2,3);
        LCD.drawString("Edmund Chee",2,4);
        LCD.drawString("Jules James",2,5);
        LCD.drawString("Press Enter",2,6);
        Button.ENTER.waitForPressAndRelease();
        LCD.clear();
    }
    
	private static void startUp() {
		firstDisplay();
		
		cs = new EV3ColorSensor(SensorPort.S2);
        color = cs.getRedMode();
        
		ss = new NXTSoundSensor(SensorPort.S1);
		sound = ss.getDBAMode();

        leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
        Wheel leftWheel = WheeledChassis.modelWheel(leftMotor, WHEEL_DIAMETER).offset(AXLE_LENGTH/2);
        rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
        Wheel rightWheel = WheeledChassis.modelWheel(rightMotor, WHEEL_DIAMETER).offset(-AXLE_LENGTH/2);
        sensorMotor = new EV3MediumRegulatedMotor(MotorPort.D);

        Chassis chassis = new WheeledChassis(new Wheel[]{rightWheel,leftWheel},WheeledChassis.TYPE_DIFFERENTIAL);
        pilot = new MovePilot(chassis);
        pilot.setAngularAcceleration(100);
        pilot.setAngularSpeed(50);
        navi = new Navigator(pilot); 
        
        ac.start();
        //(new PlaySound(START_UP)).run();

        lightLevels = SensorCalibration.calibrateColorSensor(color);
        
        soundLevels = SensorCalibration.calibrateSoundSensor(sound);
	}
	
	public static void main(String[] args) {
		ThreadRun.startUp();
		LeftMaze leftmaze = new LeftMaze(navi, color, lightLevels, sensorMotor);
	    QRHandler qrhandler = new QRHandler(navi, ac, leftmaze, sound, soundLevels);
	    LowBattery lowbattery = new LowBattery();
	    EscapeExit escapeexit = new EscapeExit();
	    Behavior EmotionalMazeSolver = new EmotionalMazeSolver(leftmaze, escapeexit, lowbattery, qrhandler);
	        
	    Behavior[] behaviorArray = {EmotionalMazeSolver};
	      
	    Arbitrator arbitrator = new Arbitrator(behaviorArray);
	    arbitrator.go();
	}
}
