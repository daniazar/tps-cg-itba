package org.cg.rendering.color;

import java.awt.Color;

import org.cg.raycaster.ray.Ray;


public  abstract class PlainColorChooser {


	ColorVariator variator;
	
	public PlainColorChooser(ColorVariator var)
	{
		variator = var;
	}
	public abstract Color getColor(Ray ray);
}
