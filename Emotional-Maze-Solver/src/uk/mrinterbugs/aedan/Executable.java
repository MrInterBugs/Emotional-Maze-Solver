package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.hardware.Sound;
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

/**
 * Executable is the main class which is run from the LeJOS robot.
 * The robot is capable of solving a maze whilst displaying different "emotions".
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Joules James
 * 
 * @version 0.5
 * @since 2020-02-14
 */
public class Executable {
	
	/**
	 * @param WHEEL_DIAMETER The diameter (mm) of the wheels.
	 * @param AXLE_LENGTH The distance (mm) your two driven wheels.
	 * @param START_UP Contains the string file name of the start up sound.
	 */
    private static final double WHEEL_DIAMETER = 56;
    private static final double AXLE_LENGTH = 120;
    private static final String START_UP = "StartUpSound.wav";

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
        LCD.drawString("Joules James",2,5);
        LCD.drawString("Press Enter",2,6);
        Button.ENTER.waitForPressAndRelease();
        LCD.clear();
    }
    
    /**
     * This is the main body of the code.
     * It is only used to initialise sensors motors and arbitrators as well as call other methods.
     */
    public static void main(String[] args) {

        NXTSoundSensor ss = new NXTSoundSensor(SensorPort.S1);
        SampleProvider sound = ss.getDBAMode();
        EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S2);
        SampleProvider color = cs.getRedMode();

        BaseRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
        Wheel leftWheel = WheeledChassis.modelWheel(leftMotor, WHEEL_DIAMETER).offset(AXLE_LENGTH/2);
        BaseRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
        Wheel rightWheel = WheeledChassis.modelWheel(rightMotor, WHEEL_DIAMETER).offset(-AXLE_LENGTH/2);

        Chassis chassis = new WheeledChassis(new Wheel[]{rightWheel,leftWheel},WheeledChassis.TYPE_DIFFERENTIAL);
        MovePilot pilot = new MovePilot(chassis);
        pilot.setAngularAcceleration(100);
        pilot.setAngularSpeed(50);
        Navigator navi = new Navigator(pilot);
        
        Sound.setVolume(100);      

        BaseRegulatedMotor sensorMotor = new EV3MediumRegulatedMotor(MotorPort.D);
       
        firstDisplay();
  
        (new Remote()).start();

        float lightLevels[] = SensorCalibration.calibrateColorSensor(color);
        
        Behavior EscapeExit = new EscapeExit(navi);
        Behavior LowBattery = new LowBattery(navi);
        Behavior Remote = new RemoteBehaviour(pilot);
        Behavior LeftMaze = new LeftMaze(navi, color, lightLevels, sensorMotor);
        Behavior QRHandler = new QRHandler();
        
        Behavior[] behaviorArray = {LeftMaze, QRHandler, Remote, EscapeExit, LowBattery};
        
        (new PlaySound(START_UP)).start();

        Arbitrator arbitrator = new Arbitrator(behaviorArray);
        arbitrator.go();

        ss.close();
        cs.close();
        sensorMotor.close();
        System.exit(0);
    }
}
