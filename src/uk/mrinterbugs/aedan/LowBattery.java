package uk.mrinterbugs.aedan;

import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class LowBattery implements Behavior {
    private static final float LOW_LEVEL = 6.0f;
    @Override
    public boolean takeControl() {
        return Battery.getVoltage() < LOW_LEVEL ;
    }

    @Override
    public void action() {

    }

    @Override
    public void suppress() {
        LCD.drawString("Low Battery!", 2, 2);
        (new LowBatterySound()).start();
        LCD.drawString("Shutdown: 3",2,2);
        Delay.msDelay(500);
        LCD.drawString("2",2,3);
        Delay.msDelay(500);
        LCD.drawString("1",2,4);
        Delay.msDelay(500);
        System.exit(-1);
    }
}
