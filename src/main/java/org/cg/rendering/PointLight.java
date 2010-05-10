package org.cg.rendering;

import java.awt.Color;

import javax.vecmath.*;

public class PointLight {
	
	public Point3f position;
	private Color intensity;
	public Float pow;
	public Float intensityReduction= 0.1f;
	float[] compontents = new float[4];
	public PointLight(Point3f position, Color intensity, Float pow)
	{
		this.setPosition(position);
		this.setIntensity(intensity);
		this.pow = pow;
	}
	
	public void setPow(Float pow) {
		this.pow= pow;
	}
	
	 public void setIntensityReduction(Float intensityReduction) {
		this.intensityReduction = intensityReduction;
	}
	public Float getpow() {
		return pow;
	}

	public void setPosition(Point3f position) {
		this.position = position;
	}

	public Point3f getPosition() {
		return position;
	}

	public void setIntensity(Color intensity) {
		this.intensity = intensity;
	}

	public Color getIntensity() {
		intensity.getComponents(compontents);
		return new Color(compontents[0]*intensityReduction,compontents[1]*intensityReduction, compontents[2]*intensityReduction );
	}

}
