package org.cg.rendering;

import java.awt.Color;

import javax.vecmath.*;

public class Light {
	
	private Point3f position;
	private Color intensity;
	
	public Light(Point3f position, Color intensity)
	{
		this.setPosition(position);
		this.setIntensity(intensity);
		
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
		return intensity;
	}

}
