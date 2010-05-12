package org.cg.rendering.color;

import java.awt.Color;

import javax.vecmath.Vector3f;

import org.cg.primitives.Primitive;
import org.cg.raycaster.ray.Ray;


public class LambertianColorChooser implements LightColorChooser {

	// Lambert
	// Calculo el coseno de la luz que incide
	// sobre
	// la superficie
	// Si esta entra perpendicular, lo ilumina
	// mas
	// Saco el coseno mediante producto y lo
	// multiplico por el coef
	// que se va modificando con la
	// reflectividad

	@Override
	public Color getColor(Ray ray, Ray lightRay, float coef, Color baseColor,
			Color lightColor) {

		Vector3f n = null;
		Primitive o = ray.getObject();
		
		float colors[] = baseColor.getColorComponents(null);
		float red = colors[0];
		float green = colors[1];
		float blue = colors[2];
		
		n = o.getNormal(ray.intersectionPoint);

		float lambert = (lightRay.direction.dot(n)) * coef;

		float[] objectColor = ray.getObject().getTextureColor(ray.intersectionPoint, ray)
				.getColorComponents(null);
		float[] lightColorComp = lightColor.getColorComponents(null);
		red += lambert * objectColor[0] * lightColorComp[0];
		green += lambert * objectColor[1] * lightColorComp[1];
		blue += lambert * objectColor[2] * lightColorComp[2];

		red = Math.min(red, 1);
		green = Math.min(green, 1);
		blue = Math.min(blue, 1);

	
		return new Color(red,green,blue);
	}

}