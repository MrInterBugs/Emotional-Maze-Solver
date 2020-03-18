package uk.mrinterbugs.aedan;

import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * A behaviour to stop the program if the battery voltage is bellow a low threshold.
 * Plays a threaded low battery sound. Then calls System.exit(0).
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Jules James
 * 
 * @version 0.5
 * @since 2020-02-14
 */
public class LowBattery implements Behavior {
    private static final float LOW_LEVEL = 6.0f;
    private Navigator navi;
    private String lowSound =  "LowBatterySound.wav";

    /**
     * Constructor to allow the passing of Navigator.
     * 
     * @param navi the main Navigator.
     */
    public LowBattery(Navigator navi) {
        this.navi = navi;
    }

    /**
     * This behaviour will take control only when the battery voltage is bellow 6.0.
     */
    @Override
    public boolean takeControl() {
        return Battery.getVoltage() < LOW_LEVEL ;
    }

    /**
     * When the behaviour is triggered the robot will play the low battery sound, count down on screen, then exit.
     */
    @Override
    public void action() {
        navi.stop();
        LCD.drawString("Low Battery!", 2, 2);
        (new PlaySound(lowSound)).start();
        LCD.drawString("Shutdown: 3",2,3);
        Delay.msDelay(500);
        LCD.drawString("2",2,4);
        Delay.msDelay(500);
        LCD.drawString("1",2,5);
        Delay.msDelay(500);
        System.exit(0);
    }

    /**
     * We do not want want this method to suppress if the battery is low it must exit.
     */
    @Override
    public void suppress() {
    }
}
