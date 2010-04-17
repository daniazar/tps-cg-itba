package org.cg.raycaster.ray;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.primitives.Primitive;
import org.cg.raycaster.Scene;
import org.cg.rendering.Camera;

public class Raycaster {

	private final static int MAX_REFLECTIONS = 0;
	private Camera camera;
	
	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public BufferedImage raycast() {
		
		camera.prepare();
		BufferedImage im = camera.getBufferedImage();
		
		Point3f startingPoint = camera.getStartingPoint();
		
		Vector3f rightauxi = new Vector3f(camera.right);
		rightauxi.scale((float) camera.dimensions.x );
		startingPoint.add(rightauxi);

		Vector3f upauxi = new Vector3f(camera.up);
		upauxi.scale(-(float) camera.dimensions.y );
		startingPoint.add(upauxi);

		for (int i = 0; i < camera.dimensions.x; i++) {

			for (int j = 0; j < camera.dimensions.y; j++) {
				float coef = 1.0f;
				Color c = new Color(0,0,0);
				Point3f pixelPos = new Point3f(startingPoint);

				upauxi = new Vector3f(camera.up);
				upauxi.scale(j*2);
				pixelPos.add(upauxi);

				Vector3f dir = new Vector3f(pixelPos.x - camera.position.x, pixelPos.y
						- camera.position.y, pixelPos.z - camera.position.z);
				dir.normalize();

				Ray ray = new Ray(dir, pixelPos);

				int level = 0;

				do {
					for (Primitive o : Scene.objects)
						o.Intersects(ray);

					if (ray.hit()) {
						
//						Vector3f n = ray.getObject().getNormal(ray.getIntersectionPoint());
//						
//						if (camera.isLightingEnabled()) 
//						{
//							c = camera.getLightchooser().getColor(ray, coef, c);
//							coef *= ray.getObject().getReflection();
//
//							Point3f newstartingPoint = ray.getIntersectionPoint();
//							float reflet = 2 * n.dot(ray.direction);
//							Vector3f newDirection = new Vector3f(ray.direction.x
//									- reflet * n.x, ray.direction.y - reflet * n.y,
//									ray.direction.z - reflet * n.z);
//
//							ray = new Ray(newDirection, newstartingPoint);
//							level++;
//						}
//						else	
							c = camera.getColorchooser().getColor(ray);
				
					} else
						coef = 0;
				} while (coef > 0.0f && level < MAX_REFLECTIONS && camera.isLightingEnabled());
				
				im.setRGB(i, j, c.getRGB());


			}

			rightauxi = new Vector3f(camera.right);
			rightauxi.scale(-2);
			startingPoint.add(rightauxi);

		}
		
		return im;
	}

}
