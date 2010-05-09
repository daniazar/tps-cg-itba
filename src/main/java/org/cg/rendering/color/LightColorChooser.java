package org.cg.rendering.color;

import java.awt.Color;

import org.cg.raycaster.ray.Ray;


public interface LightColorChooser {
	
	public Color getColor(Ray ray, Ray lightRay, float coef,  Color baseColor, Color lightColor);

}
