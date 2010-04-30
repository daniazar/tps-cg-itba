package org.cg.raycaster.ray;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.primitives.Primitive;
import org.cg.raycaster.Scene;
import org.cg.rendering.Camera;
import org.cg.rendering.color.LightColorChooser;
import org.cg.rendering.color.PlainColorChooser;

public class Raycaster {

	private final static int MAX_REFLECTIONS = 5;
	private final static int MAX_REFFRACTIONS = 5;
	private final static float MIN_COEF = 0.05f;
	
	private Camera camera;

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	PlainColorChooser colorChooser ;
	boolean islightEnabled ;
	LightColorChooser light ;
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

		light = camera.getLightchooser();
		islightEnabled = camera.isLightingEnabled();
		colorChooser = camera.getColorchooser();
		int prog= 0;
		for (int i = 0; i < camera.dimensions.x; i++) {

			for (int j = 0; j < camera.dimensions.y; j++) {
				Point3f pixelPos = new Point3f(startingPoint);

				upauxi = new Vector3f(camera.up);
				upauxi.scale(j * 2);
				pixelPos.add(upauxi);

				Vector3f dir = new Vector3f(pixelPos.x - camera.position.x,
						pixelPos.y - camera.position.y, pixelPos.z
								- camera.position.z);
				dir.normalize();

				Ray ray = new Ray(dir, pixelPos);

				int level = 0;
				if(i ==320 && j ==240)
					i=i;
				Color c = traceRay(ray, level, 1.0f);
				
				im.setRGB(i, j, c.getRGB());

			}
			if (progress)
			{
				int prog2= (i * 100 / camera.dimensions.x);
				if(prog != prog2)
				{
					prog=prog2;
				if ((i * 100 / camera.dimensions.x) % 5 == 0)
					System.out.println("Progress = " + (i * 100 / camera.dimensions.x) + "%" );
				}
			}
			rightauxi = new Vector3f(camera.right);
			rightauxi.scale(-2);
			startingPoint.add(rightauxi);

		}

		return im;
	}
	
	
	public Color traceRay(Ray ray, int depth, Float coef){
		Color c = new Color(0, 0, 0);
			for (Primitive o : Scene.objects) {
				if (o.intersectsBoundingBox(ray)) {
					o.Intersects(ray);
				}
			}

			if (ray.hit()) {
				if (islightEnabled) {
					depth++;
					c  = light.getColor(ray, coef, c);
					if(depth < MAX_REFLECTIONS && coef > MIN_COEF)
					{
						
						coef *= ray.getObject().getReflection();
						Color creflect = traceRay(ray.Reflection(), depth, coef);
						float col[] = c.getRGBColorComponents(null);
						float col2[] = creflect.getRGBColorComponents(null);

						if(col2[0] != 0 || col2[1] != 0 || col2[2] != 0)
						{
						col[0] = col[0]* (1-ray.getObject().getReflection()) + col2[0] * ray.getObject().getReflection();
						col[1] = col[1]* (1-ray.getObject().getReflection()) + col2[1] * ray.getObject().getReflection();
						col[2] = col[2]*(1-ray.getObject().getReflection())  + col2[2] * ray.getObject().getReflection();
						c = new Color(col[0], col[1], col[2]);
						}
					}
					
					if(depth < MAX_REFFRACTIONS && coef > MIN_COEF)
					{
						
						coef *= ray.getObject().getRefraction();
						
						Color crefract = traceRay(ray.Refraction(), depth, coef);
						
						float col[] = c.getRGBColorComponents(null);
						float col2[] = crefract.getRGBColorComponents(null);

						if(col2[0] != 0 || col2[1] != 0 || col2[2] != 0)
						{
						
						col[0] = col[0]* (1-ray.getObject().getRefraction())  + col2[0] * ray.getObject().getRefraction();
						col[1] = col[1]* (1-ray.getObject().getRefraction())  + col2[1] * ray.getObject().getRefraction();
						col[2] = col[2]* (1-ray.getObject().getRefraction())  + col2[2] * ray.getObject().getRefraction();
						c = new Color(col[0], col[1], col[2]);
						}	
					}
					
              
				} else
					c = colorChooser.getColor(ray);

			} else
				coef = 0f;
		return c;
	}

}
