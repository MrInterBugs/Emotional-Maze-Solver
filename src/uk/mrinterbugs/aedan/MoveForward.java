package uk.mrinterbugs.aedan;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class MoveForward implements Behavior {
    private MovePilot pilot;

    public MoveForward(MovePilot pilot) {
        this.pilot = pilot;
    }

    @Override
    public boolean takeControl() {
        return true;
    }

    @Override
    public void action() {
        if(!pilot.isMoving()) {
            pilot.forward();
        }
    }

    @Override
    public void suppress() {
    }
}
