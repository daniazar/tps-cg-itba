package org.cg.rendering;

import java.awt.Color;

public class Material {
	
	protected Color diffuse;
	protected float reflection;
	protected float refraction;
	
	public float getRefraction() {
		return refraction;
	}
	
	public void setRefraction(float refraction) {
		this.refraction = refraction;
	}
	
	public Color getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(Color diffuse) {
		this.diffuse = diffuse;
	}

	public float getReflection() {
		return reflection;
	}

	public void setReflection(float reflection) {
		this.reflection = reflection;
	}

	public Material(Color diffuse, float reflection, float refraction)
	{
		this.diffuse = diffuse;
		this.reflection = reflection;
		this.refraction = refraction;
		
	}

}
