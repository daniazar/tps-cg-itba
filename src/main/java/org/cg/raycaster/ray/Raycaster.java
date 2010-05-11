package org.cg.raycaster.ray;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.primitives.Primitive;
import org.cg.raycaster.Scene;
import org.cg.rendering.Camera;
import org.cg.rendering.PointLight;
import org.cg.rendering.color.LightColorChooser;
import org.cg.rendering.color.PhongShader;
import org.cg.rendering.color.PlainColorChooser;
import org.cg.spatial.Octree;

public class Raycaster {

	private final static int MAX_REFLECTIONS = 3;
	private final static int MAX_REFRACTIONS = 10;
	private final static float MIN_COEF = 0.00f;
	private final static float ANTIALIASING_RES = 16;
	private Camera camera;
	private Octree octree;
	private boolean OCTREE_ENABLED = false;
	//Si hay menos que esta cantidad, no tiene sentido un octree
	private static float OCTREE_THRESHOLD = 16;
	
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
		
		if(Scene.objects.size() > OCTREE_THRESHOLD)
		{
			octree = new Octree(Scene.objects);
			OCTREE_ENABLED = true;
			System.out.println("OCTREE ENABLED");
		}
	
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
						
						if(i==400 && j== 300)
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

		
		if(OCTREE_ENABLED)
		{
			Set<Primitive> primitives = octree.GetPrimitivesFromIntersectingOctants(ray);

			for(Primitive o : primitives)
				if (o.intersectsBoundingBox(ray))
					o.Intersects(ray);
	
		}
		else
		{
			for (Primitive o : Scene.objects) 
				if (o.intersectsBoundingBox(ray)) 
					o.Intersects(ray);
		}
				
		if (ray.hit()) {
			
			float reflectance = ray.Reflectance();
			float refraction = ray.getObject().getMaterial()
					.getRefraction();
			float reflection = ray.getObject().getMaterial().getReflection();
			
			float transmittance = refraction * (1.0f - reflectance);

			reflectance = reflection * reflectance;
			float total = reflectance + transmittance;
	
			if (total > 0) {
				if (depth < MAX_REFLECTIONS && coef*reflectance > MIN_COEF) {
					float auxicoef = coef * reflectance;
					auxicoef *= reflection;
					c = traceRay(ray.Reflection(), depth + 1, auxicoef,
							c);
				}
				if(depth < MAX_REFRACTIONS && coef*transmittance > MIN_COEF)
				{
			
					float auxicoef = transmittance * coef;
					auxicoef *= refraction;
					c = traceRay(ray.Refraction(),depth+1,auxicoef,c);
				}
			}
		
			if (islightEnabled) {

				for (PointLight l : Scene.lights) {

					Ray lightRay = LightHit(ray, l);

					if (!lightRay.hit()) {

						float auxicoef = (1 - total)*coef;
						c = lambertian.getColor(ray, lightRay, auxicoef, c, l
								.getIntensity());
						c = phong.getColor(ray, lightRay, auxicoef, c, l
								.getIntensity());

					}

				}
			} else
				c = colorChooser.getColor(ray);
		} else
			coef = 0f;
		

		return c;
	}


	@SuppressWarnings("unused")
	private void UnifyRay(Ray ray)
	{
		float aspectRatio =(float)camera.dimensions.y / (float)camera.dimensions.x;
	//	System.out.println("i "+ray.position.x);
		//ray.direction.x *= aspectRatio;
		ray.position.x *= aspectRatio;
		
		//System.out.println("o "+ray.position.x);
		
	}

	@SuppressWarnings("unused")
	private void UnunifyRay(Ray ray)
	{
		float aspectRatio =(float)camera.dimensions.y / (float)camera.dimensions.x;
//		ray.direction.x /= aspectRatio;
		ray.position.x /= aspectRatio;

	}
	public Ray LightHit(Ray ray, PointLight l) {
		Vector3f n = null;
		Primitive o = ray.getObject();
		//esto es para el alcance de la luz
		float dist = ray.intersectionPoint.distance(l.getPosition());
		if (dist > l.pow) {
			Ray missed = new Ray(new Vector3f(), new Point3f());
			missed.hit = true;
			return missed;
		}

		
		n = o.getNormal(ray.intersectionPoint);

		Vector3f intersectionToLight = new Vector3f(l.position.x
				- ray.intersectionPoint.x, l.position.y
				- ray.intersectionPoint.y, l.position.z
				- ray.intersectionPoint.z);

		// Si estamos opuestos no hay luz aqui
		if (n.dot(intersectionToLight) < 0.0001) {
			Ray missed = new Ray(new Vector3f(), new Point3f());
			missed.hit = true;
			return missed;
		}

		//esto reduciria linealmente la potencia de la luz.
		l.setIntensityReduction(1-(dist/l.pow));
		
		// Creo un rayo de luz desde el punto de
		// interseccion hasta la luz
		intersectionToLight.normalize();
		Ray lightRay = new Ray(intersectionToLight, ray.intersectionPoint);

		
		if(OCTREE_ENABLED)
		{
			Set<Primitive> primitives = octree.GetPrimitivesFromIntersectingOctants(lightRay);

			for(Primitive shadow : primitives)
				if (shadow.intersectsBoundingBox(lightRay))
					shadow.Intersects(lightRay);
	
		}
		else
		{
			for (Primitive shadow : Scene.objects) 
				if (shadow.intersectsBoundingBox(lightRay)) 
					shadow.Intersects(lightRay);
		}
	

		return lightRay;

	}
}
