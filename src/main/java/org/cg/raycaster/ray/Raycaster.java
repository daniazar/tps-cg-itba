package org.cg.raycaster.ray;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.primitives.Primitive;
import org.cg.raycaster.Scene;
import org.cg.rendering.Camera;
import org.cg.rendering.PointLight;
import org.cg.rendering.color.LightColorChooser;
import org.cg.rendering.color.PhongShader;
import org.cg.rendering.color.PlainColorChooser;

import com.sun.org.apache.bcel.internal.generic.FMUL;

public class Raycaster {

	private final static int MAX_REFLECTIONS = 100;
	private final static int MAX_REFRACTIONS = 0;
	private final static float MIN_COEF = 0.00f;
	private final static float ANTIALIASING_RES = 4;
	private Camera camera;

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	PlainColorChooser colorChooser;
	boolean islightEnabled;
	LightColorChooser lambertian;
	LightColorChooser phong = new PhongShader();

	public BufferedImage raycast(boolean progress) {

		camera.prepare();
		BufferedImage im = camera.getBufferedImage();

		Point3f startingPoint = camera.getStartingPoint();

		Vector3f rightauxi = new Vector3f(camera.right);
		rightauxi.scale((float) camera.dimensions.x);
		startingPoint.add(rightauxi);

		Vector3f upauxi = new Vector3f(camera.up);
		upauxi.scale(-(float) camera.dimensions.y);
		startingPoint.add(upauxi);

		lambertian = camera.getLightchooser();
		islightEnabled = camera.isLightingEnabled();
		colorChooser = camera.getColorchooser();
		int prog = 0;
		for (int i = 0; i < camera.dimensions.x; i++) {

			for (int j = 0; j < camera.dimensions.y; j++) {

				int level = 0;
				Color c = new Color(0, 0, 0);
				float fragmentCoef = 1 / ANTIALIASING_RES;
				Point3f pixelPos = new Point3f(startingPoint);

				upauxi = new Vector3f(camera.up);
				upauxi.scale((j) * 2);
				pixelPos.add(upauxi);

				float antiStep = 1 / (float) Math.sqrt(ANTIALIASING_RES);

				for (float fragmentX = 0; fragmentX < 1; fragmentX += antiStep) {
					for (float fragmentY = 0; fragmentY < 1; fragmentY += antiStep) {
						Point3f pixelFragPos = new Point3f(pixelPos);

						Vector3f upfragment = new Vector3f(camera.up);
						upfragment.scale(fragmentY);
						pixelFragPos.add(upfragment);

						Vector3f rightfragment = new Vector3f(camera.right);
						rightfragment.scale(-2 * fragmentX);
						pixelFragPos.add(rightfragment);

						Color fragColor = new Color(0, 0, 0);
						Vector3f dir = new Vector3f(pixelFragPos.x
								- camera.position.x, pixelFragPos.y
								- camera.position.y, pixelFragPos.z
								- camera.position.z);
						dir.normalize();
						Ray ray = new Ray(dir, pixelFragPos);

						fragColor = traceRay(ray, level, 1.0f, fragColor);
						float[] cComps = c.getColorComponents(null);
						float[] fragComps = fragColor.getColorComponents(null);
						for (int k = 0; k < 3; k++)
							cComps[k] += fragmentCoef * fragComps[k];
						c = new Color(cComps[0], cComps[1], cComps[2]);
					}

				}
				im.setRGB(i, j, c.getRGB());

			}
			rightauxi = new Vector3f(camera.right);
			rightauxi.scale(-2);
			startingPoint.add(rightauxi);
			
			if (progress) {
				int prog2 = (i * 100 / camera.dimensions.x);
				if (prog != prog2) {
					prog = prog2;
					if ((i * 100 / camera.dimensions.x) % 5 == 0)
						System.out.println("Progress = "
								+ (i * 100 / camera.dimensions.x) + "%");
				}
			}
		}



		return im;
	}

	public Color traceRay(Ray ray, int depth, Float coef, Color c) {

		for (Primitive o : Scene.objects) {
			if (o.intersectsBoundingBox(ray)) {
				o.Intersects(ray);
			}
		}

		if (ray.hit()) {

			if (islightEnabled) {

				for (PointLight l : Scene.lights) {

					if (depth < MAX_REFLECTIONS && coef > MIN_COEF) {
						Ray lightRay = LightHit(ray, l);

						if (!lightRay.hit()) {
							c = lambertian.getColor(ray, lightRay, coef, c, l
									.getIntensity());
							c = phong.getColor(ray, lightRay, coef, c, l
									.getIntensity());
							float auxicoef = coef;
							auxicoef *= ray.getObject().getReflection();
							c = traceRay(ray.Reflection(), depth + 1, auxicoef,
									c);
						}

					}

					/*
					 * if(depth < MAX_REFRACTIONS && coef > MIN_COEF) { float
					 * auxicoef = coef; auxicoef *=
					 * ray.getObject().getRefraction();
					 * 
					 * Color crefract = traceRay(ray.Refraction(), depth,
					 * auxicoef,c); if (crefract != null) {
					 * 
					 * float col[] = c.getRGBColorComponents(null); float col2[]
					 * = crefract.getRGBColorComponents(null);
					 * 
					 * 
					 * /*col[0] = col[0]* (1-ray.getObject().getRefraction()) +
					 * col2[0] * ray.getObject().getRefraction(); col[1] =
					 * col[1]* (1-ray.getObject().getRefraction()) + col2[1] *
					 * ray.getObject().getRefraction(); col[2] = col[2]*
					 * (1-ray.getObject().getRefraction()) + col2[2] *
					 * ray.getObject().getRefraction();
					 */
					/*
					 * col[0] += col2[0]; col[1] += col2[1]; col[2] += col2[2];
					 * col[0] = Math.min(col[0],1); col[1] = Math.min(col[1],1);
					 * col[2] = Math.min(col[2],1); c = new Color(col[0],
					 * col[1], col[2]); } }
					 */

				}
			} else
				c = colorChooser.getColor(ray);
		} else
			coef = 0f;
		return c;
	}

	public Ray LightHit(Ray ray, PointLight l) {
		Vector3f n = null;
		Primitive o = ray.getObject();

		n = o.getNormal(ray.intersectionPoint);

		Vector3f intersectionToLight = new Vector3f(l.getPosition().x
				- ray.intersectionPoint.x, l.getPosition().y
				- ray.intersectionPoint.y, l.getPosition().z
				- ray.intersectionPoint.z);

		// Si estamos opuestos no hay luz aqui
		if (n.dot(intersectionToLight) < 0.0001) {
			Ray missed = new Ray(new Vector3f(), new Point3f());
			missed.hit = true;
			return missed;
		}

		// Creo un rayo de luz desde el punto de
		// interseccion hasta la luz
		intersectionToLight.normalize();

		Ray lightRay = new Ray(intersectionToLight, ray.intersectionPoint);

		for (Primitive objShadow : Scene.objects)
			objShadow.Intersects(lightRay);

		return lightRay;

	}
}
