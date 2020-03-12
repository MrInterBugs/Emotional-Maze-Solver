package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class MoveForward implements Behavior {
	private Navigator navi;

    public MoveForward(Navigator navi) {
    	this.navi = navi;
    }

    @Override
    public boolean takeControl() {
        return true;
    }

    @Override
    public void action() {
        if(!navi.isMoving()) {    
        	navi.goTo(9999,9999);
        }
    }

    @Override
    public void suppress() {
    }
    
}
   
