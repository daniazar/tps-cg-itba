package org.cg.rendering.color;

import java.awt.Color;

import javax.vecmath.Vector3f;

import org.cg.raycaster.SunflowScene;
import org.cg.raycaster.ray.Ray;



public class PhongShading implements LightColorChooser {


	@Override
	public Color getColor(Ray ray, Ray lightRay, float coef, Color baseColor,
			Color lightColor) {
		//Calculo el vector V que apunta a la camara
		Vector3f V = new Vector3f(SunflowScene.cam.position.x - ray.getIntersectionPoint().x,
				SunflowScene.cam.position.y - ray.getIntersectionPoint().y,
				SunflowScene.cam.position.z - ray.getIntersectionPoint().z);
		
		Vector3f R = ray.Reflection().direction;
		R.normalize();
		V.normalize();
		
		float shininess = ray.getObject().getMaterial().getShininess();
		
		Color specularColor = ray.getObject().getMaterial().getSpecular();
		
		float specularRefl = ray.getObject().getMaterial().getSpecularReflection();
		
		float colors[] = baseColor.getColorComponents(null);
		float red = colors[0];
		float green = colors[1];
		float blue = colors[2];
		
		float specColors[] = specularColor.getColorComponents(null);
		
		red += coef*specularRefl * Math.pow(R.dot(V),shininess)*specColors[0];
		blue += coef*specularRefl * Math.pow(R.dot(V),shininess)*specColors[1];
		green += coef*specularRefl * Math.pow(R.dot(V),shininess)*specColors[2];
		
		red = Math.min(red, 1);
		green = Math.min(green, 1);
		blue = Math.min(blue, 1);
	/*	float exposure = -0.5f;
		red = (float) (1 - Math.exp(exposure*red));
		green = (float) (1 - Math.exp(exposure*green));
		blue = (float) (1 - Math.exp(exposure*blue));*/
		
		return new Color(red,green,blue);
	}

}
