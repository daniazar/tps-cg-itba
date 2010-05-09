package org.cg.rendering.color;

import java.awt.Color;

import javax.vecmath.Vector3f;

import org.cg.raycaster.Scene;
import org.cg.raycaster.ray.Ray;



public class PhongShader implements LightColorChooser {


	@Override
	public Color getColor(Ray ray, Ray lightRay, float coef, Color baseColor,
			Color lightColor) {
		//Calculo el vector V que apunta a la camara
		Vector3f V = new Vector3f(Scene.cam.position.x - ray.getIntersectionPoint().x,
				Scene.cam.position.y - ray.getIntersectionPoint().y,
				Scene.cam.position.z - ray.getIntersectionPoint().z);
		
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
		

		
		red += specularRefl * Math.pow(R.dot(V),shininess)*specColors[0];
		blue += specularRefl * Math.pow(R.dot(V),shininess)*specColors[1];
		green += specularRefl * Math.pow(R.dot(V),shininess)*specColors[2];
		
		red = Math.min(1, red);
		green = Math.min(1, green);
		blue = Math.min(1, blue);
		return new Color(red,green,blue);
	}

}
