package uk.mrinterbugs.aedan;

import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class LowBattery implements Behavior {
    private static final float LOW_LEVEL = 6.0f;
    private Navigator navi;
    private String lowSound =  "LowBatterySound.wav";

    public LowBattery(Navigator navi) {
        this.navi = navi;
    }

    @Override
    public boolean takeControl() {
        return Battery.getVoltage() < LOW_LEVEL ;
    }

    @Override
    public void action() {
        navi.stop();
        LCD.drawString("Low Battery!", 2, 2);
        (new PlaySound(lowSound)).start();
        LCD.drawString("Shutdown: 3",2,2);
        Delay.msDelay(500);
        LCD.drawString("2",2,3);
        Delay.msDelay(500);
        LCD.drawString("1",2,4);
        Delay.msDelay(500);
        System.exit(-1);
    }

    @Override
    public void suppress() {
        //We do not want want this method to suppress if the battery is low it must exit.
    }
}
