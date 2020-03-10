package uk.mrinterbugs.aedan;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class MoveForward implements Behavior {
    private Navigator navi;
    private BaseRegulatedMotor sensorMotor;
    private float[] samples;
    private SampleProvider color;
    private boolean suppressed;
    private Coord coord;
    
    private final int LEFT = 0;
    private final int FRONT = 1;
    private final int RIGHT = 2;
    
    private final int NOTVISITED = -1;
    private final int BEENVISITED = 0;
    private final int MARKEDOFF = 1;
    
    private final int DISTANCEMOVE = 10;
    private final float DARK = 0.2f;

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
            scanForCoord();
            int[] variables = checkForJunction();
            goToJunction(variables);
        }
    }

    @Override
    public void suppress() {
    }
    
    private void scanForCoord() {
	    while(true) {
	    	color.fetchSample(samples, LEFT);
		    Delay.msDelay(100);
	    	
		    sensorMotor.rotateTo(-90, false);
		    sensorMotor.waitComplete();
		    color.fetchSample(samples, FRONT);
		    Delay.msDelay(100);
		    
		    sensorMotor.rotateTo(-180, false);
		    sensorMotor.waitComplete();
		    color.fetchSample(samples, RIGHT);
		    Delay.msDelay(100);
		    
		    sensorMotor.rotateTo(0, false);
		    sensorMotor.waitComplete();
		    break;
	    }
    }
    
    private int[] checkForJunction() {
    	int paths = 0;
    	int available = 0;
    	    	
    	int haveLeft = 0;
    	int haveFront = 0;
    	int haveRight = 0;
    	
    	
    	float tempHeading = Float.parseFloat(String.valueOf(this.navi.getPoseProvider().getPose().getHeading()));
    	int tempX = (int)(Float.parseFloat(String.valueOf(this.navi.getPoseProvider().getPose().getX())));
    	int tempY = (int)(Float.parseFloat(String.valueOf(this.navi.getPoseProvider().getPose().getY())));
    	
    	while (true) {
        	int lastDigitX = Math.abs(tempX%10);
        	int lastDigitY = Math.abs(tempY%10);
        	
        	//make x coord ends with value 5
            if ((int)(lastDigitX/5) == 0) {
                if (tempX < 0) {
                    tempX -= (5 - lastDigitX);
                } else {
                    tempX += (5 - lastDigitX);
                }
            } else {
                if (tempX < 0) {
                    tempX += ((lastDigitX + 5) % 5); 

                } else {
                    tempX -= ((lastDigitX + 5) % 5);
                }
            }

            //make y coord ends with value 5
            if ((int)(lastDigitY/5) == 0) {
                if (tempY < 0) {
                    tempY -= (5 - lastDigitY); 
                } else {
                    tempY += (5 - lastDigitY);
                }
            } else {
                if (tempY < 0) {
                    tempY += ((lastDigitY + 5) % 5);
                } else {
                    tempY -= ((lastDigitY + 5) % 5);
                }
            }
        	break;
    	}
    	
    	int currentHeading = 0;
    	int currentX = tempX;
    	int currentY = tempY;
    	
    	if (tempHeading < 5 && tempHeading > -5) {
    		currentHeading = 0;
        	if (samples[this.LEFT] <= DARK) {
        		haveLeft = 1;
        		if (coord.checkvisited(currentX, currentY-10)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
        	if (samples[this.FRONT] <= DARK) {
        		haveFront = 1;
        		if (coord.checkvisited(currentX+10, currentY)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
        	if (samples[this.RIGHT] <= DARK) {
        		haveRight = 1;
        		if (coord.checkvisited(currentX, currentY+10)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
    	}
    	
    	if (tempHeading < 95 && tempHeading > 85) {
    		currentHeading = 90;
        	if (samples[this.LEFT] <= DARK) {
        		haveLeft = 1;
        		if (coord.checkvisited(currentX+10, currentY)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
        	if (samples[this.FRONT] <= DARK) {
        		haveFront = 1;
        		if (coord.checkvisited(currentX, currentY+10)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
        	if (samples[this.RIGHT] <= DARK) {
        		haveRight = 1;
        		if (coord.checkvisited(currentX-10, currentY)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
    	}
    	
    	if (tempHeading < -85 && tempHeading > -95) {
    		currentHeading = -90;
        	if (samples[this.LEFT] <= DARK) {
        		haveLeft = 1;
        		if (coord.checkvisited(currentX-10, currentY)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
        	if (samples[this.FRONT] <= DARK) {
        		haveFront = 1;
        		if (coord.checkvisited(currentX, currentY-10)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
        	if (samples[this.RIGHT] <= DARK) {
        		haveRight = 1;
        		if (coord.checkvisited(currentX+10, currentY)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
    	}
    	
    	if (tempHeading > 175 || tempHeading < -175) {
    		currentHeading = 180;
        	if (samples[this.LEFT] <= DARK) {
        		haveLeft = 1;
        		if (coord.checkvisited(currentX, currentY+10)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
        	if (samples[this.FRONT] <= DARK) {
        		haveFront = 1;
        		if (coord.checkvisited(currentX-10, currentY)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
        	if (samples[this.RIGHT] <= DARK) {
        		haveRight = 1;
        		if (coord.checkvisited(currentX, currentY-10)==NOTVISITED) {
        			available++;
        		}
        		paths++;
        	}
    	}
    	
    	return new int[] {currentX, currentY, currentHeading, paths, available, haveLeft, haveFront, haveRight};
    	
    }
    
    private void goToJunction(int[] variables) {
    	int currentX = variables[0];
    	int currentY = variables[1];
    	int currentHeading = variables[2];
    	int paths = variables[3];
    	int available = variables[4];
    	boolean haveLeft = (variables[5] == 1) ? true:false;
    	boolean haveFront = (variables[6] == 1) ? true:false;
    	boolean haveRight = (variables[7] == 1) ? true:false;
    	
    	
    	int random = 0;
    	
    	if (currentHeading == 0) {
        	switch (paths) {
        	case 3:
        		if (available == 3) { //choose one from the 3 paths
        			random = (int)(Math.random()*3);
        			if (random == LEFT) { //left or
        				coord.add(currentX, currentY-10);
        				navi.goTo(new Waypoint(currentX, currentY-10)); //check if adjustment is needed before that
        			} else if (random == FRONT) { //front or
        				coord.add(currentX+10, currentY);
        				navi.goTo(new Waypoint(currentX+10, currentY)); //for now just put it in like that
        			} else { //right
        				coord.add(currentX, currentY+10);
        				navi.goTo(new Waypoint(currentX, currentY+10));
        			}
        			coord.add(currentX-10, currentY); //add previous
        		} else if (available == 2) { //choose one not visited
        			random = (int)(Math.random()*3);
        			if (random == LEFT) { //left if available or
        				if (coord.checkvisited(currentX, currentY-10) == NOTVISITED) {
        					coord.add(currentX, currentY-10);
        					navi.goTo(new Waypoint(currentX, currentY-10));
        				} else if (coord.checkvisited(currentX+10, currentY) ==NOTVISITED) { //check front
        					coord.add(currentX+10, currentY);
        				    navi.goTo(new Waypoint(currentX+10, currentY));
        				} else { //then it should be right
        					coord.add(currentX, currentY+10); 
            				navi.goTo(new Waypoint(currentX, currentY+10));
        				}
        			}
        			if (random == FRONT) { //front if available or
        				if (coord.checkvisited(currentX+10, currentY) == NOTVISITED) {
        					coord.add(currentX+10, currentY);
        				    navi.goTo(new Waypoint(currentX+10, currentY));
        				} else if (coord.checkvisited(currentX, currentY-10) == NOTVISITED) {//check right
        					coord.add(currentX, currentY-10);
        					navi.goTo(new Waypoint(currentX, currentY-10));
        				} else { //then should be right
        					coord.add(currentX, currentY+10);
        				    navi.goTo(new Waypoint(currentX, currentY+10));
        				}
        			} 
        			if (random == RIGHT) { //right
        				if (coord.checkvisited(currentX, currentY+10) == NOTVISITED) {
        					coord.add(currentX, currentY+10);
        				    navi.goTo(new Waypoint(currentX, currentY+10));
        				} else if (coord.checkvisited(currentX, currentY-10) == NOTVISITED) { //check left
            				coord.add(currentX, currentY-10);
            				navi.goTo(new Waypoint(currentX, currentY-10));
        				} else { //then should be front
        					coord.add(currentX+10, currentY);
        				    navi.goTo(new Waypoint(currentX+10, currentY));
        				}
        			}
        			coord.markoff(currentX-10, currentY); //mark previous twice
        		} else if (available == 1) {  //go back
        			coord.add(currentX-10, currentY); //add previous
        			coord.markoff(currentX-10, currentY); //mark previous twice
        			navi.goTo(new Waypoint(currentX-10, currentY)); 
        		} else { //if none available go back
        			coord.add(currentX-10, currentY); //add previous
        			coord.markoff(currentX-10, currentY); //mark previous twice
        			navi.goTo(new Waypoint(currentX-10, currentY));
        		}
        		break;
        	case 2:
        		if (available == 2) {
        			//check where are the paths first
        			if (haveLeft) { //if there is left
        				if (haveFront) { //either front
        					random = (int)(Math.random()*2);
        					if (random == LEFT) { //either choose left
            					coord.add(currentX, currentY-10);
            				    navi.goTo(new Waypoint(currentX, currentY-10));
        					} else { //or else choose front
            					coord.add(currentX+10, currentY);
            				    navi.goTo(new Waypoint(currentX+10, currentY));
        					}
        				} else { //or else right as the second path
        					random = (int)(Math.random()*2);
        					if (random == LEFT) { //either choose left
            					coord.add(currentX, currentY-10);
            				    navi.goTo(new Waypoint(currentX, currentY-10));
        					} else { //or else choose right
            					coord.add(currentX, currentY+10);
            				    navi.goTo(new Waypoint(currentX, currentY+10));
        					}        					
        				}
        			
        			}
        			
        			if (haveFront) { //or else it's only just front and right as paths
        				random = 1 + (int)(Math.random()*2);
        				if (random == FRONT) {
        					coord.add(currentX+10, currentY);
        				    navi.goTo(new Waypoint(currentX+10, currentY));
        				} else { //then right
        					coord.add(currentX, currentY+10);
        				    navi.goTo(new Waypoint(currentX, currentY+10));
        					
        				}
        			}
        			coord.add(currentX-10, currentY);
        			
        		} else if (available == 1) {
        			if (haveLeft && coord.checkvisited(currentX, currentY-10)==NOTVISITED) { //left is valid path and available
        				coord.add(currentX, currentY-10);
        				navi.goTo(new Waypoint(currentX, currentY-10));
        			} else if (haveFront && coord.checkvisited(currentX+10, currentY)==NOTVISITED) { //front is valid path and available
        				coord.add(currentX+10, currentY);
        				navi.goTo(new Waypoint(currentX+10, currentY));
        			} else { // else just right valid and available
        				coord.add(currentX, currentY+10);
        				navi.goTo(new Waypoint(currentX, currentY+10));
        			}
        			coord.markoff(currentX-10, currentY);
        		} else { //no availability
        			if (haveLeft && coord.checkvisited(currentX, currentY-10)==BEENVISITED) { //left is valid path and visited once
        				coord.markoff(currentX, currentY-10);
        				navi.goTo(new Waypoint(currentX, currentY-10));
        			} else if (haveFront && coord.checkvisited(currentX+10, currentY)==BEENVISITED) { //front is valid path and visited once
        				coord.markoff(currentX+10, currentY);
        				navi.goTo(new Waypoint(currentX+10, currentY));
        			} else { // else just right valid and visited once
        				coord.markoff(currentX, currentY+10);
        				navi.goTo(new Waypoint(currentX, currentY+10));
        			}
        			coord.add(currentX-10, currentY);
        			coord.markoff(currentX-10, currentY);
        		}
        	case 1:
        		if (available == 1) {
        			if (haveFront) {
        				navi.goTo(new Waypoint(currentX+10, currentY));
        			} else if (haveLeft) {
        				coord.add(currentX, currentY-10);
        				navi.goTo(new Waypoint(currentX, currentY-10));
        				coord.add(currentX-10, currentY);
        			} else { //else have Right as path
        				coord.add(currentX, currentY+10);
        				navi.goTo(new Waypoint(currentX, currentY+10));
        				coord.add(currentX-10, currentY);
        			}
        		} else {
        			if (haveFront) { //if front is the path
        				navi.goTo(new Waypoint(currentX+10, currentY));
        			}
        			if (haveLeft) { //either left is the path
        				coord.markoff(currentX, currentY-10);
        				navi.goTo(new Waypoint(currentX, currentY-10));
        				coord.markoff(currentX-10, currentY);
        			} else { //else it's right as path!
        				coord.markoff(currentX, currentY+10);
        				navi.goTo(new Waypoint(currentX, currentY+10));
        				coord.markoff(currentX-10, currentY);
        			}
        		}
        		break;
        	default:
        		navi.goTo(new Waypoint(currentX-10, currentY));
        		break;	
        		
        	}     	
        }
    	
    	if (currentHeading == 90) {
        	switch (paths) {
        	case 3:
        		if (available == 3) {
        			
        			
        		} else if (available == 2) {
        			
        		} else if (available == 1) {
        		 	
        		} else {
        			
        		}
        		break;
        	case 2:
        		if (available == 2) {
        			
        		} else if (available == 1) {
        			
        		}
        	case 1:
        		if (available == 1) {
        			
        		} else {
        			
        		}
        	default:
        		
        		
        	}
    	}
    	
    	if (currentHeading == 180) {
        	switch (paths) {
        	case 3:
        		if (available == 3) {
        			
        			
        		} else if (available == 2) {
        			
        		} else if (available == 1) {
        			
        		} else {
        			
        		}
        		break;
        	case 2:
        		if (available == 2) {
        			
        		} else if (available == 1) {
        			
        		}
        	case 1:
        		if (available == 1) {
        			
        		} else {
        			
        		}
        		
        	}
    	}
    	
    	if (currentHeading == -90) {
        	switch (paths) {
        	case 3:
        		if (available == 3) {
        			
        			
        		} else if (available == 2) {
        			
        		} else if (available == 1) {
        			
        		} else {
        			
        		}
        		break;
        	case 2:
        		if (available == 2) {
        			
        		} else if (available == 1) {
        			
        		}
        	case 1:
        		if (available == 1) {
        			
        		} else {
        			
        		}
        		
        	}
    	}
    }
}
