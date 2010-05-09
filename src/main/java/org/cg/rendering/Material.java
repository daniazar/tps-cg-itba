package org.cg.rendering;

import java.awt.Color;

public class Material {
	
	protected Color diffuse;
	protected float reflection;
	protected float refract;
	protected float refraction;
	protected Color specularColor;
	protected float shininess;
	protected float specularReflection;
	
	
	public Color getSpecular() {
		return specularColor;
	}

	public void setSpecular(Color specular) {
		this.specularColor = specular;
	}

	public float getShininess() {
		return shininess;
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
	}

	public float getSpecularReflection() {
		return specularReflection;
	}

	public void setSpecularReflection(float specularReflection) {
		this.specularReflection = specularReflection;
	}

	
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
	public void setRefract(float refract) {
		this.refract = refract;
	}
	public float getRefract() {
		return refract;
	}

	public Material(Color diffuse, float reflection, float refraction,
			float specularReflection, Color specularColor, float shininess, float refract)
	{
		this.diffuse = diffuse;
		this.reflection = reflection;
		this.refraction = refraction;
		this.refract = refract;
		
		this.specularReflection = specularReflection;
		this.specularColor = specularColor;
		this.shininess = shininess;
		
		
	}

}
