package uk.mrinterbugs.aedan;

import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public class LineFollower implements Behavior {
	private Navigator navi;
	private SampleProvider colorSampler;
	private float[] lightLevels;
	private int whiteIndex = 0;
	private int blackIndex = 1;
	private float averageLight;

	public LineFollower(Navigator navi, SampleProvider colorSampler, float[] lightLevels) {
		this.navi = navi;
		this.colorSampler = colorSampler;
		this.lightLevels = lightLevels;
		this.averageLight = (lightLevels[whiteIndex] + lightLevels[blackIndex]) / 2;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		while(this.onDarkSurface()) {
			navi.getMoveController().forward();
		}
	}

	public void suppress() {

	}

	private boolean onDarkSurface() {
		return getLightSample() < getAverageLight();
	}

	private float getLightSample() {
		float lightSample[] = new float[1];
		this.getColorSampler().fetchSample(lightSample, 0);
		return lightSample[0];
	}

	private float getAverageLight() {
		return this.averageLight;
	}

	private void setAverageLight(float averageLight) {
		this.averageLight = averageLight;
	}

	private Navigator getNavi() {
		return navi;
	}

	private void setNavi(Navigator navi) {
		this.navi = navi;
	}

	private SampleProvider getColorSampler() {
		return colorSampler;
	}

	private void setColorSampler(SampleProvider colorSampler) {
		this.colorSampler = colorSampler;
	}

	private float[] getLightLevels() {
		return lightLevels;
	}

	private void setLightLevels(float[] lightLevels) {
		this.lightLevels = lightLevels;
	}

	private int getWhiteIndex() {
		return whiteIndex;
	}

	private void setWhiteIndex(int whiteIndex) {
		this.whiteIndex = whiteIndex;
	}

	private int getBlackIndex() {
		return blackIndex;
	}

	private void setBlackIndex(int blackIndex) {
		this.blackIndex = blackIndex;
	}
}
