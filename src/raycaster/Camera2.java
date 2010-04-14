package raycaster;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.vecmath.*;

public class Camera2 {

	public Point3f position;
	public Vector3f direction;
	public Vector3f up;
	public Vector3f right;
	public Point2i dimensions;
	public Point2f di;
	public float distance;
	public float fovX;
	public float fovY;
	
	private int MAX_REFLECTIONS = 0;
	private boolean lightingEnabled = false;

	public Camera2(Point3f pos, Vector3f dir, Point2i dim, Vector3f up,
			float fovx, float dist) {
		position = pos;
		direction = dir;
		direction.normalize();
		this.up = up;
		this.up.normalize();

		right = new Vector3f();
		right.cross(direction, up);
		dimensions = dim;
		dimensions.absolute();
		fovX = fovx;
		// distancia focal a partir del fov y la imagen
        di = new Point2f();
		distance = dist;
		di.x = (float) (dist * Math.tan(Math.toRadians(fovX/2))) ;
System.out.println(dimensions + ",       " + fovX);

// Calculo del fovY
fovY = ((float)dimensions.y)/ ((float)dimensions.x)*fovX;
		System.out.println("fovY: " + fovY);
		
		di.y = (float) (dist * Math.tan(Math.toRadians(fovY/2))) ;;
		// Calculo del fovY
		right.scale(-di.x /dimensions.x);
		this.up.scale(-di.y /dimensions.y);
		System.out.println(right + "    ,    " + this.up);
		System.out.println(dimensions);
		System.out.println(di);
		
	}

	public void Raytrace() {
		BufferedImage im = new BufferedImage(dimensions.x, dimensions.y,
				BufferedImage.TYPE_INT_RGB);
		Point3f startingPoint = new Point3f((position.x + direction.x
				* distance), (position.y + direction.y * distance),
				(position.z + direction.z * distance));
		System.out.println(position + "," +direction + ";" +distance);
		Vector3f rightauxi = new Vector3f(right);
		rightauxi.scale((float) dimensions.x );
		startingPoint.add(rightauxi);

		Vector3f upauxi = new Vector3f(up);
		upauxi.scale(-(float) dimensions.y );
		startingPoint.add(upauxi);

		System.out.println("Starting Point del Plano de Proyeccion:"
				+ startingPoint);

		for (int i = 0; i < dimensions.x; i++) {

			for (int j = 0; j < dimensions.y; j++) {
				float red = 0, green = 0, blue = 0, coef = 1.0f;

				Point3f pixelPos = new Point3f(startingPoint);

				upauxi = new Vector3f(up);
				upauxi.scale(j*2);
				pixelPos.add(upauxi);
				Vector3f dir = new Vector3f(pixelPos.x - position.x, pixelPos.y
						- position.y, pixelPos.z - position.z);
				dir.normalize();
				//if(j==dimensions.y-1)
					
				//System.out.println(pixelPos);
				
				Ray ray = new Ray(dir, pixelPos);

				int level = 0;

				do {
					for (Object o : Scene.objects)
						o.Intersects(ray);

					if (ray.hit()) {
						Vector3f n = null;
						Object o = ray.getObject();

						n = o.getNormal(ray.intersectionPoint);

						if (lightingEnabled) {
							for (Light l : Scene.lights) {

								Vector3f intersectionToLight = new Vector3f(
										l.position.x - ray.intersectionPoint.x,
										l.position.y - ray.intersectionPoint.y,
										l.position.z - ray.intersectionPoint.z);

								// Si estamos opuestos no hay luz aqui
								if (n.dot(intersectionToLight) < 0)
									continue;

								// Creo un rayo de luz desde el punto de
								// interseccion hasta la luz
								intersectionToLight.normalize();
								Ray lightRay = new Ray(intersectionToLight,
										ray.intersectionPoint);

								for (Object objShadow : Scene.objects)
									objShadow.Intersects(lightRay);

								boolean lightHit = false;
								if (!lightRay.hit())
									lightHit = true;
								else {
									if (!lightRay.isPointInSegment(
											lightRay.position, l.position,
											lightRay.intersectionPoint))
										lightHit = false;
									else
										lightHit = false;
								}

								if (lightHit) {
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
									// del material.
									float lambert = (lightRay.direction.dot(n))
											* coef;

									float[] objectColor = ray.getObject()
											.getColor()
											.getColorComponents(null);
									float[] lightColor = l.intensity
											.getColorComponents(null);
									red += lambert * objectColor[0]
											* lightColor[0];
									green += lambert * objectColor[1]
											* lightColor[1];
									blue += lambert * objectColor[2]
											* lightColor[2];
								}

							}
						}
						else
						{
							float[] objectColor = ray.getObject().getColor()
							.getColorComponents(null);

							red = objectColor[0];
							green = objectColor[1];
							blue = objectColor[2];
						}
						coef *= ray.getObject().getReflection();

						Point3f newstartingPoint = ray.getIntersectionPoint();
						float reflet = 2 * n.dot(ray.direction);
						Vector3f newDirection = new Vector3f(ray.direction.x
								- reflet * n.x, ray.direction.y - reflet * n.y,
								ray.direction.z - reflet * n.z);

						ray = new Ray(newDirection, newstartingPoint);
						level++;
					} else
						coef = 0;
				} while (coef > 0.0f && level < MAX_REFLECTIONS);
				
					red = Math.min(red, 1);
					green = Math.min(green, 1);
					blue = Math.min(blue, 1);


				Color c = new Color(red, green, blue);

				im.setRGB(i, j, c.getRGB());

			}

			rightauxi = new Vector3f(right);
			rightauxi.scale(-2);
			startingPoint.add(rightauxi);
			//System.out.println(startingPoint);
		}
		File render = new File("render");
		try {
			ImageIO.write(im, "png", render);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
