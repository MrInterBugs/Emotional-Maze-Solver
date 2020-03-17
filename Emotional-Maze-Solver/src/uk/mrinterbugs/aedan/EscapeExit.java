package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * Allows the user to press and hold the top left button (Escape) on the EV3 to stop the program.
 * Plays a threaded shutdown sound. Then calls System.exit(0).
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Jules James
 * 
 * @version 0.5
 * @since 2020-02-14
 */
public class EscapeExit implements Behavior {
    private Navigator navi;
    private String shutSound = "ShutDownSound.wav";

    /**
     * Constructor to allow the passing of Navigator.
     * 
     * @param navi the main Navigator.
     */
    public EscapeExit(Navigator navi) {
        this.navi = navi;
    }

    /**
     * This behaviour will take control only when the escape button is pushed down.
     */
    @Override
    public boolean takeControl() {
        return Button.ESCAPE.isDown();
    }

    /**
     * When the escape button is pushed down the robot will play the shutdown sound, count down on screen, then exit.
     */
    @Override
    public void action() {
    	Executable.close();
        LCD.clear();
        (new PlaySound(shutSound)).start();
        LCD.drawString("Escape key: 3",2,2);
        Delay.msDelay(500);
        LCD.drawString("2",2,3);
        Delay.msDelay(500);
        LCD.drawString("1",2,4);
        Delay.msDelay(500);
        System.exit(0);
    }

    /**
     * We do not want want this method to suppress if the escape key is pressed it must exit.
     */
    @Override
    public void suppress() {
    }
}
