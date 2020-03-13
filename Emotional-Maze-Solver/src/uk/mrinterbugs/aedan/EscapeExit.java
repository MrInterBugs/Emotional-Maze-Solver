package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * 
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Joules James
 * 
 * @version 0.5
 * @since 2020-02-14
 */
public class EscapeExit implements Behavior {
    private Navigator navi;
    private String shutSound = "ShutDownSound.wav";

    public EscapeExit(Navigator navi) {
        this.navi = navi;
    }

    @Override
    public boolean takeControl() {
        return Button.ESCAPE.isDown();
    }

    @Override
    public void action() {
        navi.stop();
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

    @Override
    public void suppress() {
        //We do not want want this method to suppress if the escape key is pressed it must exit.
    }
}
