package uk.mrinterbugs.aedan;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;


public class CalSensor {
	//calibrate sensor,need 3 samples for left right forward, if sample < min then call method to calculate record path coordinates
	private EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
	private SampleProvider reflect = cs.getRedMode();
	private float max = 0.0f;
	private float min = 1.0f;
	float[] level = new float[1];
	float colortotal = 0;
	int samplecount = 0;
	float LIGHT_AVERAGE = 0;


	public void calSensor(){
		while(Button.ENTER.isUp()){
			reflect.fetchSample(level,0);
			if(level[0]>this.max)this.max = level[0];

			if(level[0] <this.min)this.min = level[0];

			colortotal = colortotal + level[0];
			samplecount++;
		}
		LIGHT_AVERAGE = colortotal/samplecount;


	}
	public void getSample(float[] f,int index) {
		reflect.fetchSample(f, index);
		if(f[index]<=LIGHT_AVERAGE) {
			//call poseprovider to get coords
			//Coord.add(coord1,coord2);
		}


	}
	

}
