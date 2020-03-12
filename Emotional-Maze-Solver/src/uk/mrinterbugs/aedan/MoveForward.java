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
    private Coord coord;
    
    private static final int LEFT = 0;
    private static final int FRONT = 1;
    private static final int RIGHT = 2;
    
    private static final int NOTVISITED = -1;
    private static final int BEENVISITED = 0;
    private static final int MARKEDOFF = 1;
    
    private static final int MOVE = 10;
    private static final float DARK = 0.2f;

    public MoveForward(Navigator navi, BaseRegulatedMotor sensorMotor, SampleProvider color) {
        this.navi = navi;
        this.sensorMotor = sensorMotor;
        this.samples = new float[3];
        this.color = color;
        this.coord = new Coord();    
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
    	
    	int leftAvailable = 0;
    	int frontAvailable = 0;
    	int rightAvailable = 0;
    	
    	int currentHeading = 0;
    	int currentX = 0;
    	int currentY = 0;
    	
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
    	
    	currentX = tempX;
    	currentY = tempY;
    	
    	if (tempHeading < 5 && tempHeading > -5) {
    		currentHeading = 0;
        	if (samples[this.LEFT] <= DARK) {
        		haveLeft = 1;
        		if (coord.checkvisited(currentX, currentY-MOVE)==NOTVISITED) {
        			available++;
        			leftAvailable++;
        		}
        		paths++;
        	}
        	if (samples[this.FRONT] <= DARK) {
        		haveFront = 1;
        		if (coord.checkvisited(currentX+MOVE, currentY)==NOTVISITED) {
        			available++;
        			frontAvailable++;
        		}
        		paths++;
        	}
        	if (samples[this.RIGHT] <= DARK) {
        		haveRight = 1;
        		if (coord.checkvisited(currentX, currentY+MOVE)==NOTVISITED) {
        			available++;
        			rightAvailable++;
        		}
        		paths++;
        	}
    	}
    	
    	if (tempHeading < 95 && tempHeading > 85) {
    		currentHeading = 90;
        	if (samples[this.LEFT] <= DARK) {
        		haveLeft = 1;
        		if (coord.checkvisited(currentX+MOVE, currentY)==NOTVISITED) {
        			available++;
        			leftAvailable++;
        		}
        		paths++;
        	}
        	if (samples[this.FRONT] <= DARK) {
        		haveFront = 1;
        		if (coord.checkvisited(currentX, currentY+MOVE)==NOTVISITED) {
        			available++;
        			frontAvailable++;
        		}
        		paths++;
        	}
        	if (samples[this.RIGHT] <= DARK) {
        		haveRight = 1;
        		if (coord.checkvisited(currentX-MOVE, currentY)==NOTVISITED) {
        			available++;
        			rightAvailable++;
        		}
        		paths++;
        	}
    	}
    	
    	if (tempHeading < -85 && tempHeading > -95) {
    		currentHeading = -90;
        	if (samples[this.LEFT] <= DARK) {
        		haveLeft = 1;
        		if (coord.checkvisited(currentX-MOVE, currentY)==NOTVISITED) {
        			available++;
        			leftAvailable++;
        		}
        		paths++;
        	}
        	if (samples[this.FRONT] <= DARK) {
        		haveFront = 1;
        		if (coord.checkvisited(currentX, currentY-MOVE)==NOTVISITED) {
        			available++;
        			frontAvailable++;
        		}
        		paths++;
        	}
        	if (samples[this.RIGHT] <= DARK) {
        		haveRight = 1;
        		if (coord.checkvisited(currentX+MOVE, currentY)==NOTVISITED) {
        			available++;
        			rightAvailable++;
        		}
        		paths++;
        	}
    	}
    	
    	if (tempHeading > 175 || tempHeading < -175) {
    		currentHeading = 180;
        	if (samples[this.LEFT] <= DARK) {
        		haveLeft = 1;
        		if (coord.checkvisited(currentX, currentY+MOVE)==NOTVISITED) {
        			available++;
        			leftAvailable++;
        		}
        		paths++;
        	}
        	if (samples[this.FRONT] <= DARK) {
        		haveFront = 1;
        		if (coord.checkvisited(currentX-MOVE, currentY)==NOTVISITED) {
        			available++;
        			frontAvailable++;
        		}
        		paths++;
        	}
        	if (samples[this.RIGHT] <= DARK) {
        		haveRight = 1;
        		if (coord.checkvisited(currentX, currentY-MOVE)==NOTVISITED) {
        			available++;
        			rightAvailable++;
        		}
        		paths++;
        	}
    	}
    	
    	return new int[] {currentX, currentY, currentHeading, paths, available, haveLeft, haveFront, haveRight, leftAvailable, frontAvailable, rightAvailable};
    	
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
    	boolean leftAvailable = (variables[8] == 1) ? true:false;
    	boolean frontAvailable = (variables[9] == 1) ? true:false;
    	boolean rightAvailable = (variables[MOVE] == 1) ? true:false;
    	
    	
    	int random = 0;
    	
    	if (currentHeading == 0) {
        	switch (paths) {
        	case 3:
        		if (available == 3) { //choose one from the 3 paths
        			random = (int)(Math.random()*3);
        			if (random == LEFT) { //left or
        				coord.add(currentX, currentY-MOVE);
        				navi.goTo(new Waypoint(currentX, currentY-MOVE)); //check if adjustment is needed before that
        			} else if (random == FRONT) { //front or
        				coord.add(currentX+MOVE, currentY);
        				navi.goTo(new Waypoint(currentX+MOVE, currentY)); //for now just put it in like that
        			} else { //right
        				coord.add(currentX, currentY+MOVE);
        				navi.goTo(new Waypoint(currentX, currentY+MOVE));
        			}
        			coord.add(currentX-MOVE, currentY); //add previous
        		} else if (available == 2) { //choose one not visited
        			random = (int)(Math.random()*3);
        			if (random == LEFT) { //left if available or
        				if (leftAvailable == true) {
        					coord.add(currentX, currentY-MOVE);
        					navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				} else if (frontAvailable == true) { //check front
        					coord.add(currentX+MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				} else { //then it should be right
        					coord.add(currentX, currentY+MOVE); 
            				navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				}
        			}
        			if (random == FRONT) { //front if available or
        				if (frontAvailable == true) {
        					coord.add(currentX+MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				} else if (leftAvailable == true) {//check right
        					coord.add(currentX, currentY-MOVE);
        					navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				} else { //then should be right
        					coord.add(currentX, currentY+MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				}
        			} 
        			if (random == RIGHT) { //right
        				if (rightAvailable == true) {
        					coord.add(currentX, currentY+MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				} else if (leftAvailable) { //check left
            				coord.add(currentX, currentY-MOVE);
            				navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				} else { //then should be front';
        					coord.add(currentX+MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				}
        			}
        			coord.markoff(currentX-MOVE, currentY); //mark previous twice
        		} else if (available == 1) {  //go back
        			coord.add(currentX-MOVE, currentY); //add previous
        			coord.markoff(currentX-MOVE, currentY); //mark previous twice
        			navi.goTo(new Waypoint(currentX-MOVE, currentY)); 
        		} else { //if none available go back
        			coord.add(currentX-MOVE, currentY); //add previous
        			coord.markoff(currentX-MOVE, currentY); //mark previous twice
        			navi.goTo(new Waypoint(currentX-MOVE, currentY));
        		}
        		break;
        	case 2:
        		if (available == 2) {
        			//check where are the paths first
        			if (haveLeft && haveFront) { //if there is left
        				random = (int)(Math.random()*2);
    					if (random == LEFT) { //either choose left
        					coord.add(currentX, currentY-MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY-MOVE));
    					} else { //or else choose front
        					coord.add(currentX+MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX+MOVE, currentY));
    					}
        			} else if (haveLeft && haveRight) { //or else right as the second path
    					random = (int)(Math.random()*2);
    					if (random == LEFT) { //either choose left
        					coord.add(currentX, currentY-MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY-MOVE));
    					} else { //or else choose right
        					coord.add(currentX, currentY+MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY+MOVE));
    					}     
   					
        			} else { //or else it's only just front and right as paths
        				random = 1 + (int)(Math.random()*2);
        				if (random == FRONT) {
        					coord.add(currentX+MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				} else { //then right
        					coord.add(currentX, currentY+MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				}
        			}
        			coord.add(currentX-MOVE, currentY);		
        		} else if (available == 1) {
        			if (haveLeft && leftAvailable) { //left is valid path and available
        				coord.add(currentX, currentY-MOVE);
        				navi.goTo(new Waypoint(currentX, currentY-MOVE));
        			} else if (haveFront && frontAvailable) { //front is valid path and available
        				coord.add(currentX+MOVE, currentY);
        				navi.goTo(new Waypoint(currentX+MOVE, currentY));
        			} else { // else just right valid and available
        				coord.add(currentX, currentY+MOVE);
        				navi.goTo(new Waypoint(currentX, currentY+MOVE));
        			}
        			coord.markoff(currentX-MOVE, currentY);
        		} else { //no availability
        			//check for markedoff path first
        			if (coord.checkvisited(currentX, currentY-MOVE)==MARKEDOFF || coord.checkvisited(currentX+MOVE, currentY)==MARKEDOFF || coord.checkvisited(currentX, currentY+MOVE)==MARKEDOFF) {
            			if (haveLeft && haveFront) {
            				if (coord.checkvisited(currentX, currentY-MOVE)==MARKEDOFF) { //if left markedoff
            					coord.markoff(currentX+MOVE, currentY); //mark front
            					navi.goTo(new Waypoint(currentX+MOVE, currentY)); //go front
            						
            				} else {
            					coord.markoff(currentX, currentY-MOVE); //if not, mark left
            					navi.goTo(new Waypoint(currentX, currentY-MOVE)); //go left
            				}
            			} 
            			else if (haveLeft && haveRight) {
            				if (coord.checkvisited(currentX, currentY-MOVE)==MARKEDOFF) { //if left markedoff
        						coord.markoff(currentX, currentY+MOVE); //mark right
        						navi.goTo(new Waypoint(currentX, currentY+MOVE)); //go right
        					} else {
        						coord.markoff(currentX, currentY-MOVE); //if not, mark left 
        						navi.goTo(new Waypoint(currentX, currentY-MOVE)); //go left
        					}
            				
            			} 
            			else { //only front and right have path
            				if (coord.checkvisited(currentX+MOVE, currentY)==MARKEDOFF) { //if front markedoff
        						coord.markoff(currentX, currentY-MOVE); //mark right
        						navi.goTo(new Waypoint(currentX, currentY-MOVE)); //go right
            				} else {
        						coord.markoff(currentX+MOVE, currentY); //if not, mark front
        						navi.goTo(new Waypoint(currentX+MOVE, currentY)); //go front
            				}
            			}
            			coord.markoff(currentX-MOVE, currentY);
        			} else {
        				coord.add(currentX-MOVE, currentY); //add previous
        				coord.markoff(currentX-MOVE, currentY); //markoff previous
        				navi.goTo(new Waypoint(currentX-MOVE,currentY)); //go to previous
        			}
        			break;
        		}
        	case 1:
        		if (available == 1) {
        			if (haveFront) {
        				navi.goTo(new Waypoint(currentX+MOVE, currentY)); //just go front
        			} else if (haveLeft) {
        				coord.add(currentX, currentY-MOVE); //add left
        				navi.goTo(new Waypoint(currentX, currentY-MOVE)); //go left
        				coord.add(currentX-MOVE, currentY); //add previous
        			} else { //else have Right as path
        				coord.add(currentX, currentY+MOVE); //add right
        				navi.goTo(new Waypoint(currentX, currentY+MOVE)); //go right
        				coord.add(currentX-MOVE, currentY); //add previous
        			}
        		} else {
        			if (haveFront) { //if front is the path
        				navi.goTo(new Waypoint(currentX+MOVE, currentY)); //just go front
        			} else if (haveLeft) { //either left is the path
        				coord.markoff(currentX, currentY-MOVE);
        				navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				coord.markoff(currentX-MOVE, currentY);
        			} else { //else it's right as path!
        				coord.markoff(currentX, currentY+MOVE);
        				navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				coord.markoff(currentX-MOVE, currentY);
        			}
        		}
        		break;
        	default:
        		navi.goTo(new Waypoint(currentX-MOVE, currentY)); //just go back if dead end
        		break;	
        		
        	}     	
        }
    	
    	if (currentHeading == 90) {
        	switch (paths) {
        	case 3:
        		if (available == 3) { //choose one from the 3 paths
        			random = (int)(Math.random()*3);
        			if (random == LEFT) { //left or
        				coord.add(currentX+MOVE, currentY); //add left
        				navi.goTo(new Waypoint(currentX+MOVE, currentY)); //check if adjustment is needed before that
        			} else if (random == FRONT) { //front or
        				coord.add(currentX, currentY+MOVE);
        				navi.goTo(new Waypoint(currentX, currentY+MOVE)); //for now just put it in like that
        			} else { //right
        				coord.add(currentX-MOVE, currentY);
        				navi.goTo(new Waypoint(currentX-MOVE, currentY));
        			}
        			coord.add(currentX, currentY-MOVE); //add previous
        		} else if (available == 2) { //choose one not visited
        			random = (int)(Math.random()*3);
        			if (random == LEFT) { //left if available or
        				if (leftAvailable == true) {
        					coord.add(currentX+MOVE, currentY); //add left
        					navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				} else if (frontAvailable == true) { //check front
        					coord.add(currentX, currentY+MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				} else { //then it should be right
        					coord.add(currentX-MOVE, currentY); 
            				navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				}
        			}
        			if (random == FRONT) { //front if available or
        				if (frontAvailable == true) {
        					coord.add(currentX, currentY+MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				} else if (leftAvailable == true) {//check right
        					coord.add(currentX+MOVE, currentY);
        					navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				} else { //then should be right
        					coord.add(currentX-MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				}
        			} 
        			if (random == RIGHT) { //right
        				if (rightAvailable == true) {
        					coord.add(currentX-MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				} else if (leftAvailable) { //check left
            				coord.add(currentX+MOVE, currentY);
            				navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				} else { //then should be front';
        					coord.add(currentX, currentY+MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				}
        			}
        			coord.markoff(currentX, currentY-MOVE); //mark previous off
        		} else if (available == 1) {  //go back
        			coord.add(currentX, currentY-MOVE); //add previous
        			coord.markoff(currentX, currentY-MOVE); //mark previous off
        			navi.goTo(new Waypoint(currentX, currentY-MOVE)); //go previous/back
        		} else { //if none available go back
        			coord.add(currentX, currentY-MOVE); //add previous
        			coord.markoff(currentX, currentY-MOVE); //mark previous off
        			navi.goTo(new Waypoint(currentX, currentY-MOVE));
        		}
        		break;
        	case 2:
        		if (available == 2) {
        			//check where are the paths first
        			if (haveLeft && haveFront) { //if there is left
        				random = (int)(Math.random()*2);
    					if (random == LEFT) { //either choose left
        					coord.add(currentX+MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX+MOVE, currentY));
    					} else { //or else choose front
        					coord.add(currentX, currentY+MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY+MOVE));
    					}
        			} else if (haveLeft && haveRight) { //or else right as the second path
    					random = (int)(Math.random()*2);
    					if (random == LEFT) { //either choose left
        					coord.add(currentX+MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX+MOVE, currentY));
    					} else { //or else choose right
        					coord.add(currentX-MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX-MOVE, currentY));
    					}     
   					
        			} else { //or else it's only just front and right as paths
        				random = 1 + (int)(Math.random()*2);
        				if (random == FRONT) {
        					coord.add(currentX, currentY+MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				} else { //then right
        					coord.add(currentX-MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				}
        			}
        			coord.add(currentX, currentY-MOVE);		
        		} else if (available == 1) {
        			if (haveLeft && leftAvailable) { //left is valid path and available
        				coord.add(currentX+MOVE, currentY);
        				navi.goTo(new Waypoint(currentX+MOVE, currentY));
        			} else if (haveFront && frontAvailable) { //front is valid path and available
        				coord.add(currentX, currentY+MOVE);
        				navi.goTo(new Waypoint(currentX, currentY+MOVE));
        			} else { // else just right valid and available
        				coord.add(currentX-MOVE, currentY);
        				navi.goTo(new Waypoint(currentX-MOVE, currentY));
        			}
        			coord.markoff(currentX, currentY-MOVE);
        		} else { //no availability
        			//check for markedoff path first
        			if (coord.checkvisited(currentX+MOVE, currentY)==MARKEDOFF || coord.checkvisited(currentX, currentY+MOVE)==MARKEDOFF || coord.checkvisited(currentX-MOVE, currentY)==MARKEDOFF) {
            			if (haveLeft && haveFront) {
            				if (coord.checkvisited(currentX+MOVE, currentY)==MARKEDOFF) { //if left markedoff
            					coord.markoff(currentX, currentY+MOVE); //mark front
            					navi.goTo(new Waypoint(currentX, currentY+MOVE)); //go front
            						
            				} else {
            					coord.markoff(currentX+MOVE, currentY); //if not, mark left
            					navi.goTo(new Waypoint(currentX+MOVE, currentY)); //go left
            				}
            			} 
            			else if (haveLeft && haveRight) {
            				if (coord.checkvisited(currentX+MOVE, currentY)==MARKEDOFF) { //if left markedoff
        						coord.markoff(currentX-MOVE, currentY); //mark right
        						navi.goTo(new Waypoint(currentX-MOVE, currentY)); //go right
        					} else {
            					coord.markoff(currentX+MOVE, currentY); //if not, mark left
            					navi.goTo(new Waypoint(currentX+MOVE, currentY)); //go left
        					}
            				
            			} 
            			else { //only front and right have path
            				if (coord.checkvisited(currentX, currentY+MOVE)==MARKEDOFF) { //if front markedoff
        						coord.markoff(currentX-MOVE, currentY); //mark right
        						navi.goTo(new Waypoint(currentX-MOVE, currentY)); //go right
            				} else {
            					coord.markoff(currentX, currentY+MOVE); //mark front
            					navi.goTo(new Waypoint(currentX, currentY+MOVE)); //go front
            				}
            			}
            			coord.markoff(currentX, currentY-MOVE);
        			} else {
        				coord.add(currentX, currentY-MOVE); //add previous
        				coord.markoff(currentX, currentY-MOVE); //markoff previous
        				navi.goTo(new Waypoint(currentX,currentY-MOVE)); //go back/previous
        			}
        			break;
        		}
        	case 1:
        		if (available == 1) {
        			if (haveFront) {
        				navi.goTo(new Waypoint(currentX, currentY+MOVE)); //just go front
        			} else if (haveLeft) {
        				coord.add(currentX+MOVE, currentY); //add left
        				navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				coord.add(currentX, currentY-MOVE); //add previous 
        			} else { //else have Right as path
        				coord.add(currentX-MOVE, currentY);
        				navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				coord.add(currentX, currentY-MOVE); //add previous
        			}
        		} else {
        			if (haveFront) { //if front is the path
        				navi.goTo(new Waypoint(currentX, currentY+MOVE)); //just go front
        			} else if (haveLeft) { //either left is the path
        				coord.markoff(currentX+MOVE, currentY);
        				navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				coord.markoff(currentX, currentY-MOVE);
        			} else { //else it's right as path!
        				coord.markoff(currentX-MOVE, currentY);
        				navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				coord.markoff(currentX, currentY-MOVE);
        			}
        		}
        		break;
        	default:
        		navi.goTo(new Waypoint(currentX, currentY-MOVE)); //just go back if dead end
        		break;	
        		
        	}  
    	}
    	
    	if (currentHeading == 180) {
        	switch (paths) {
        	case 3:
        		if (available == 3) { //choose one from the 3 paths
        			random = (int)(Math.random()*3);
        			if (random == LEFT) { //left or
        				coord.add(currentX, currentY+MOVE); //add left
        				navi.goTo(new Waypoint(currentX, currentY+MOVE)); //check if adjustment is needed before that
        			} else if (random == FRONT) { //front or
        				coord.add(currentX-MOVE, currentY);
        				navi.goTo(new Waypoint(currentX-MOVE, currentY)); //for now just put it in like that
        			} else { //right
        				coord.add(currentX, currentY-MOVE);
        				navi.goTo(new Waypoint(currentX, currentY-MOVE));
        			}
        			coord.add(currentX+MOVE, currentY); //add previous
        		} else if (available == 2) { //choose one not visited
        			random = (int)(Math.random()*3);
        			if (random == LEFT) { //left if available or
        				if (leftAvailable == true) {
        					coord.add(currentX, currentY+MOVE); //add left
        					navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				} else if (frontAvailable == true) { //check front
        					coord.add(currentX-MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				} else { //then it should be right
        					coord.add(currentX, currentY-MOVE); 
            				navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				}
        			}
        			if (random == FRONT) { //front if available or
        				if (frontAvailable == true) {
        					coord.add(currentX-MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				} else if (leftAvailable == true) {//check right
        					coord.add(currentX, currentY+MOVE);
        					navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				} else { //then should be right
        					coord.add(currentX, currentY-MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				}
        			} 
        			if (random == RIGHT) { //right
        				if (rightAvailable == true) {
        					coord.add(currentX, currentY-MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				} else if (leftAvailable) { //check left
            				coord.add(currentX, currentY+MOVE);
            				navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				} else { //then should be front';
        					coord.add(currentX-MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				}
        			}
        			coord.markoff(currentX+MOVE, currentY); //mark previous off
        		} else if (available == 1) {  //go back
        			coord.add(currentX+MOVE, currentY); //add previous
        			coord.markoff(currentX+MOVE, currentY); //mark previous off
        			navi.goTo(new Waypoint(currentX+MOVE, currentY)); //go previous/back
        		} else { //if none available go back
        			coord.add(currentX+MOVE, currentY); //add previous
        			coord.markoff(currentX+MOVE, currentY); //mark previous off
        			navi.goTo(new Waypoint(currentX+MOVE, currentY));
        		}
        		break;
        	case 2:
        		if (available == 2) {
        			//check where are the paths first
        			if (haveLeft && haveFront) { //if there is left
        				random = (int)(Math.random()*2);
    					if (random == LEFT) { //either choose left
        					coord.add(currentX, currentY+MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY+MOVE));
    					} else { //or else choose front
        					coord.add(currentX-MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX-MOVE, currentY));
    					}
        			} else if (haveLeft && haveRight) { //or else right as the second path
    					random = (int)(Math.random()*2);
    					if (random == LEFT) { //either choose left
        					coord.add(currentX, currentY+MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY+MOVE));
    					} else { //or else choose right
        					coord.add(currentX, currentY-MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY-MOVE));
    					}     
   					
        			} else { //or else it's only just front and right as paths
        				random = 1 + (int)(Math.random()*2);
        				if (random == FRONT) {
        					coord.add(currentX-MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				} else { //then right
        					coord.add(currentX, currentY-MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				}
        			}
        			coord.add(currentX+MOVE, currentY);		
        		} else if (available == 1) {
        			if (haveLeft && leftAvailable) { //left is valid path and available
        				coord.add(currentX, currentY+MOVE);
        				navi.goTo(new Waypoint(currentX, currentY+MOVE));
        			} else if (haveFront && frontAvailable) { //front is valid path and available
        				coord.add(currentX-MOVE, currentY);
        				navi.goTo(new Waypoint(currentX-MOVE, currentY));
        			} else { // else just right valid and available
        				coord.add(currentX, currentY-MOVE);
        				navi.goTo(new Waypoint(currentX, currentY-MOVE));
        			}
        			coord.markoff(currentX+MOVE, currentY);
        		} else { //no availability
        			//check for markedoff path first - left, front, then right
        			if (coord.checkvisited(currentX, currentY+MOVE)==MARKEDOFF || coord.checkvisited(currentX-MOVE, currentY)==MARKEDOFF || coord.checkvisited(currentX, currentY-MOVE)==MARKEDOFF) {
            			if (haveLeft && haveFront) {
            				if (coord.checkvisited(currentX, currentY+MOVE)==MARKEDOFF) { //if left markedoff
            					coord.markoff(currentX-MOVE, currentY); //mark front
            					navi.goTo(new Waypoint(currentX-MOVE, currentY)); //go front
            						
            				} else {
            					coord.markoff(currentX, currentY+MOVE); //if not, mark left
            					navi.goTo(new Waypoint(currentX, currentY+MOVE)); //go left
            				}
            			} 
            			else if (haveLeft && haveRight) {
            				if (coord.checkvisited(currentX, currentY+MOVE)==MARKEDOFF) { //if left markedoff
        						coord.markoff(currentX, currentY-MOVE); //mark right
        						navi.goTo(new Waypoint(currentX, currentY-MOVE)); //go right
        					} else {
            					coord.markoff(currentX, currentY+MOVE); //if not, mark left
            					navi.goTo(new Waypoint(currentX, currentY+MOVE)); //go left
        					}
            				
            			} 
            			else { //only front and right have path
            				if (coord.checkvisited(currentX-MOVE, currentY)==MARKEDOFF) { //if front markedoff
        						coord.markoff(currentX, currentY-MOVE); //mark right
        						navi.goTo(new Waypoint(currentX, currentY-MOVE)); //go right
            				} else {
            					coord.markoff(currentX-MOVE, currentY); //mark front
            					navi.goTo(new Waypoint(currentX-MOVE, currentY)); //go front
            				}
            			}
            			coord.markoff(currentX+MOVE, currentY);
        			} else {
        				coord.add(currentX+MOVE, currentY); //add previous
        				coord.markoff(currentX+MOVE, currentY); //markoff previous
        				navi.goTo(new Waypoint(currentX+MOVE,currentY)); //go back/previous
        			}
        			break;
        		}
        	case 1:
        		if (available == 1) {
        			if (haveFront) {
        				navi.goTo(new Waypoint(currentX-MOVE, currentY)); //just go front
        			} else if (haveLeft) {
        				coord.add(currentX, currentY+MOVE); //add left
        				navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				coord.add(currentX+MOVE, currentY); //add previous 
        			} else { //else have Right as path
        				coord.add(currentX, currentY-MOVE); //add right
        				navi.goTo(new Waypoint(currentX, currentY-MOVE)); //go to right
        				coord.add(currentX+MOVE, currentY); //add previous
        			}
        		} else {
        			if (haveFront) { //if front is the path
        				navi.goTo(new Waypoint(currentX-MOVE, currentY)); //just go front
        			} else if (haveLeft) { //either left is the path
        				coord.markoff(currentX, currentY+MOVE);
        				navi.goTo(new Waypoint(currentX, currentY+MOVE));
        				coord.markoff(currentX+MOVE, currentY);
        			} else { //else it's right as path!
        				coord.markoff(currentX, currentY-MOVE); //markoff right
        				navi.goTo(new Waypoint(currentX-MOVE, currentY-MOVE));
        				coord.markoff(currentX+MOVE, currentY); //markoff previous
        			}
        		}
        		break;
        	default:
        		navi.goTo(new Waypoint(currentX+MOVE, currentY)); //just go back if dead end
        		break;	
        		
        	}  
    	}
    	
    	if (currentHeading == -90) {
        	switch (paths) {
        	case 3:
        		if (available == 3) { //choose one from the 3 paths
        			random = (int)(Math.random()*3);
        			if (random == LEFT) { //left or
        				coord.add(currentX-MOVE, currentY); //add left
        				navi.goTo(new Waypoint(currentX-MOVE, currentY)); //check if adjustment is needed before that
        			} else if (random == FRONT) { //front or
        				coord.add(currentX, currentY-MOVE);
        				navi.goTo(new Waypoint(currentX, currentY-MOVE)); //for now just put it in like that
        			} else { //right
        				coord.add(currentX+MOVE, currentY);
        				navi.goTo(new Waypoint(currentX+MOVE, currentY));
        			}
        			coord.add(currentX, currentY+MOVE); //add previous
        		} else if (available == 2) { //choose one not visited
        			random = (int)(Math.random()*3);
        			if (random == LEFT) { //left if available or
        				if (leftAvailable == true) {
        					coord.add(currentX-MOVE, currentY); //add left
        					navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				} else if (frontAvailable == true) { //check front
        					coord.add(currentX, currentY-MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				} else { //then it should be right
        					coord.add(currentX+MOVE, currentY); 
            				navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				}
        			}
        			if (random == FRONT) { //front if available or
        				if (frontAvailable == true) {
        					coord.add(currentX, currentY-MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				} else if (leftAvailable == true) {//check right
        					coord.add(currentX-MOVE, currentY);
        					navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				} else { //then should be right
        					coord.add(currentX+MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				}
        			} 
        			if (random == RIGHT) { //right
        				if (rightAvailable == true) {
        					coord.add(currentX+MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				} else if (leftAvailable) { //check left
            				coord.add(currentX-MOVE, currentY);
            				navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				} else { //then should be front';
        					coord.add(currentX, currentY-MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				}
        			}
        			coord.markoff(currentX, currentY+MOVE); //mark previous off
        		} else if (available == 1) {  //go back
        			coord.add(currentX, currentY+MOVE); //add previous
        			coord.markoff(currentX, currentY+MOVE); //mark previous off
        			navi.goTo(new Waypoint(currentX, currentY+MOVE)); //go previous/back
        		} else { //if none available go back
        			coord.add(currentX, currentY+MOVE); //add previous
        			coord.markoff(currentX, currentY+MOVE); //mark previous off
        			navi.goTo(new Waypoint(currentX, currentY+MOVE));
        		}
        		break;
        	case 2:
        		if (available == 2) {
        			//check where are the paths first
        			if (haveLeft && haveFront) { //if there is left
        				random = (int)(Math.random()*2);
    					if (random == LEFT) { //either choose left
        					coord.add(currentX-MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX-MOVE, currentY));
    					} else { //or else choose front
        					coord.add(currentX, currentY-MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY-MOVE));
    					}
        			} else if (haveLeft && haveRight) { //or else right as the second path
    					random = (int)(Math.random()*2);
    					if (random == LEFT) { //either choose left
        					coord.add(currentX-MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX-MOVE, currentY));
    					} else { //or else choose right
        					coord.add(currentX+MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX+MOVE, currentY));
    					}     
   					
        			} else { //or else it's only just front and right as paths
        				random = 1 + (int)(Math.random()*2);
        				if (random == FRONT) {
        					coord.add(currentX, currentY-MOVE);
        				    navi.goTo(new Waypoint(currentX, currentY-MOVE));
        				} else { //then right
        					coord.add(currentX+MOVE, currentY);
        				    navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				}
        			}
        			coord.add(currentX, currentY+MOVE);		
        		} else if (available == 1) {
        			if (haveLeft && leftAvailable) { //left is valid path and available
        				coord.add(currentX-MOVE, currentY);
        				navi.goTo(new Waypoint(currentX-MOVE, currentY));
        			} else if (haveFront && frontAvailable) { //front is valid path and available
        				coord.add(currentX, currentY-MOVE);
        				navi.goTo(new Waypoint(currentX, currentY-MOVE));
        			} else { // else just right valid and available
        				coord.add(currentX+MOVE, currentY);
        				navi.goTo(new Waypoint(currentX+MOVE, currentY));
        			}
        			coord.markoff(currentX, currentY+MOVE);
        		} else { //no availability
        			//check for markedoff path first
        			if (coord.checkvisited(currentX-MOVE, currentY)==MARKEDOFF || coord.checkvisited(currentX, currentY-MOVE)==MARKEDOFF || coord.checkvisited(currentX+MOVE, currentY)==MARKEDOFF) {
            			if (haveLeft && haveFront) {
            				if (coord.checkvisited(currentX-MOVE, currentY)==MARKEDOFF) { //if left markedoff
            					coord.markoff(currentX, currentY-MOVE); //mark front
            					navi.goTo(new Waypoint(currentX, currentY-MOVE)); //go front
            						
            				} else {
            					coord.markoff(currentX-MOVE, currentY); //if not, mark left
            					navi.goTo(new Waypoint(currentX-MOVE, currentY)); //go left
            				}
            			} 
            			else if (haveLeft && haveRight) {
            				if (coord.checkvisited(currentX-MOVE, currentY)==MARKEDOFF) { //if left markedoff
        						coord.markoff(currentX+MOVE, currentY); //mark right
        						navi.goTo(new Waypoint(currentX+MOVE, currentY)); //go right
        					} else {
            					coord.markoff(currentX-MOVE, currentY); //if not, mark left
            					navi.goTo(new Waypoint(currentX-MOVE, currentY)); //go left
        					}
            				
            			} 
            			else { //only front and right have path
            				if (coord.checkvisited(currentX, currentY-MOVE)==MARKEDOFF) { //if front markedoff
        						coord.markoff(currentX+MOVE, currentY); //mark right
        						navi.goTo(new Waypoint(currentX+MOVE, currentY)); //go right
            				} else {
            					coord.markoff(currentX, currentY-MOVE); //mark front
            					navi.goTo(new Waypoint(currentX, currentY-MOVE)); //go front
            				}
            			}
            			coord.markoff(currentX, currentY+MOVE);
        			} else {
        				coord.add(currentX, currentY+MOVE); //add previous
        				coord.markoff(currentX, currentY+MOVE); //markoff previous
        				navi.goTo(new Waypoint(currentX,currentY+MOVE)); //go back/previous
        			}
        			break;
        		}
        	case 1:
        		if (available == 1) {
        			if (haveFront) {
        				navi.goTo(new Waypoint(currentX, currentY-MOVE)); //just go front
        			} else if (haveLeft) {
        				coord.add(currentX-MOVE, currentY); //add left
        				navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				coord.add(currentX, currentY+MOVE); //add previous 
        			} else { //else have Right as path
        				coord.add(currentX+MOVE, currentY);
        				navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				coord.add(currentX, currentY+MOVE); //add previous
        			}
        		} else {
        			if (haveFront) { //if front is the path
        				navi.goTo(new Waypoint(currentX, currentY-MOVE)); //just go front
        			} else if (haveLeft) { //either left is the path
        				coord.markoff(currentX-MOVE, currentY);
        				navi.goTo(new Waypoint(currentX-MOVE, currentY));
        				coord.markoff(currentX, currentY+MOVE);
        			} else { //else it's right as path!
        				coord.markoff(currentX+MOVE, currentY);
        				navi.goTo(new Waypoint(currentX+MOVE, currentY));
        				coord.markoff(currentX, currentY+MOVE);
        			}
        		}
        		break;
        	default:
        		navi.goTo(new Waypoint(currentX, currentY+MOVE)); //just go back if dead end
        		break;	
        		
        	}  
    	}
    }
}
