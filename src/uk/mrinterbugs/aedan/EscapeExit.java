package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class EscapeExit implements Behavior {
    private Navigator navi;

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
        (new ShutDownSound()).start();
        LCD.drawString("3",2,2);
        Delay.msDelay(500);
        LCD.drawString("2",2,3);
        Delay.msDelay(500);
        LCD.drawString("1",2,4);
        Delay.msDelay(500);
        System.exit(-1);
    }

    @Override
    public void suppress() {
        //We do not want want this method to suppress if the escape key is pressed it must exit.
    }
}
