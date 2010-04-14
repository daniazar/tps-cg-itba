package raycaster;

import java.awt.Color;

import javax.vecmath.Vector3f;

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
	public Color getColor(Ray ray, float coef, Color c) {

		Vector3f n = null;
		Object o = ray.getObject();
		
		float colors[] = c.getColorComponents(null);
		float red = colors[0];
		float green = colors[1];
		float blue = colors[2];
		
		n = o.getNormal(ray.intersectionPoint);
		for (Light l : Scene.lights) {

			Vector3f intersectionToLight = new Vector3f(l.position.x
					- ray.intersectionPoint.x, l.position.y
					- ray.intersectionPoint.y, l.position.z
					- ray.intersectionPoint.z);

			// Si estamos opuestos no hay luz aqui
			if (n.dot(intersectionToLight) < 0)
				continue;

			// Creo un rayo de luz desde el punto de
			// interseccion hasta la luz
			intersectionToLight.normalize();
			Ray lightRay = new Ray(intersectionToLight, ray.intersectionPoint);

			for (Object objShadow : Scene.objects)
				objShadow.Intersects(lightRay);

			boolean lightHit = false;
			if (!lightRay.hit())
				lightHit = true;
			else {
				if (!lightRay.isPointInSegment(lightRay.position, l.position,
						lightRay.intersectionPoint))
					lightHit = false;
				else
					lightHit = false;
			}

			if (lightHit) {
				// del material.
				float lambert = (lightRay.direction.dot(n)) * coef;

				float[] objectColor = ray.getObject().getColor()
						.getColorComponents(null);
				float[] lightColor = l.intensity.getColorComponents(null);
				red += lambert * objectColor[0] * lightColor[0];
				green += lambert * objectColor[1] * lightColor[1];
				blue += lambert * objectColor[2] * lightColor[2];

				red = Math.min(red, 1);
				green = Math.min(green, 1);
				blue = Math.min(blue, 1);
			}

		}
		return new Color(red,green,blue);
	}
}