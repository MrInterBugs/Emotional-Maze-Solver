package uk.mrinterbugs.aedan;

import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

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
            navi.goTo(100, 0);
        }
    }

    @Override
    public void suppress() {
    }
}
