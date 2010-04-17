package org.cg.rendering.color;

import java.awt.Color;

import org.cg.primitives.Primitive;

import raycaster.Ray;

public class ObjectColorChooser extends PlainColorChooser {


	public ObjectColorChooser(ColorVariator var) {
		super(var);
		// TODO Auto-generated constructor stub
	}

	public
	Color getColor(Ray ray) {
		Primitive o = ray.getObject();
		Color ans = null;
		if(o != null )
		{
			ans = o.getColor();
		}
		
		return ans;

	}

}
