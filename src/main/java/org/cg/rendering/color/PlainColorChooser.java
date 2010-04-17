package org.cg.rendering.color;

import java.awt.Color;

import raycaster.Ray;

public  abstract class PlainColorChooser {


	ColorVariator variator;
	
	public PlainColorChooser(ColorVariator var)
	{
		variator = var;
	}
	public abstract Color getColor(Ray ray);
}
