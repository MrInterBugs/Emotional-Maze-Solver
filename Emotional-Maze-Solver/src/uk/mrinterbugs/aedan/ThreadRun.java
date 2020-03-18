package uk.mrinterbugs.aedan;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
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
    private static BaseRegulatedMotor sensorMotor;
    private static BaseRegulatedMotor leftMotor;
    private static BaseRegulatedMotor rightMotor;
    private static float lightLevels[];
    private static Navigator navi;
    private static SampleProvider color;
	
	private static void startUp() {
		cs = new EV3ColorSensor(SensorPort.S2);
        color = cs.getRedMode();

        leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
        Wheel leftWheel = WheeledChassis.modelWheel(leftMotor, WHEEL_DIAMETER).offset(AXLE_LENGTH/2);
        rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
        Wheel rightWheel = WheeledChassis.modelWheel(rightMotor, WHEEL_DIAMETER).offset(-AXLE_LENGTH/2);
        sensorMotor = new EV3MediumRegulatedMotor(MotorPort.D);

        Chassis chassis = new WheeledChassis(new Wheel[]{rightWheel,leftWheel},WheeledChassis.TYPE_DIFFERENTIAL);
        MovePilot pilot = new MovePilot(chassis);
        pilot.setAngularAcceleration(100);
        pilot.setAngularSpeed(50);
        navi = new Navigator(pilot); 
        
        lightLevels = SensorCalibration.calibrateColorSensor(color);
	}
	
	public static void main(String[] args) {
		ThreadRun.startUp();
		LeftMaze leftmaze = new LeftMaze(navi, color, lightLevels, sensorMotor);
	    QRHandler qrhandler = new QRHandler(navi);
	    LowBattery lowbattery = new LowBattery();
	    EscapeExit escapeexit = new EscapeExit();
	    Behavior EmotionalMazeSolver = new EmotionalMazeSolver(leftmaze, escapeexit, lowbattery, qrhandler);
	        
	    Behavior[] behaviorArray = {EmotionalMazeSolver};
	      
	    Arbitrator arbitrator = new Arbitrator(behaviorArray);
	    arbitrator.go();
	}
}
