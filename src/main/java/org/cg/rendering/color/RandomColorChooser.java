package org.cg.rendering.color;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.cg.primitives.Primitive;
import org.cg.raycaster.ray.Ray;

public class RandomColorChooser extends PlainColorChooser {

	public RandomColorChooser(ColorVariator var) {
		super(var);
	}

	private Random r = new Random(System.currentTimeMillis());
	ArrayList<Primitive> assignedColors = new ArrayList<Primitive>();

	public Color getColor(Ray ray) {
		Color ans = null;
		if (assignedColors.contains(ray.getObject())) {
			ans = ray.getObject().getBaseColor();
		} else {
			Color c = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
			ray.getObject().setBaseColor(c);
			assignedColors.add(ray.getObject());
			ans = c;
		}
		
		// return variator.changeColor(baseDist, ans,
		// ray.getIntersectionPoint().distance(ray.position));
		return ans;

	}

}
