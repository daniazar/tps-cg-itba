package org.cg.rendering.color;

import java.awt.Color;

public class LinearColorVariator extends ColorVariator {

	private float step = 0.006f;
	@Override
	public Color changeColor(float baseDistance, Color baseColor, float distance) {
		
		float[] colors = baseColor.getColorComponents(null);
		
		while(distance > baseDistance)
		{
			colors[0] -= step;
			colors[1] -= step;
			colors[2] -= step;
			distance--;
		}
		colors[0] = Math.max(colors[0], 0);
		colors[1] = Math.max(colors[1], 0);
		colors[2] = Math.max(colors[2], 0);
		
		colors[0] = Math.min(colors[0], 1);
		colors[1] = Math.min(colors[1], 1);
		colors[2] = Math.min(colors[2], 1);
		return new Color(colors[0], colors[1], colors[2]);
	}

}
