package org.cg.rendering.color;

import java.awt.Color;

import raycaster.Ray;

public interface LightColorChooser {
	
	public Color getColor(Ray ray, float coef,  Color c);

}
