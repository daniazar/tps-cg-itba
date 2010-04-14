package raycaster;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.vecmath.*;

public class Camera {

	public Point3f position;
	public Vector3f direction;
	public Vector3f up;
	public Vector3f right;
	public Point2i dimensions;
	public float distance;
	public float fovX;
	public float fovY;
	public String extension = "png";
	
	private ColorVariator colorvariator = new LinearColorVariator();
	private PlainColorChooser colorchooser = new RandomColorChooser(colorvariator);
	
	private LightColorChooser lightchooser = new LambertianColorChooser();
	private int MAX_REFLECTIONS = 0;
	private boolean lightingEnabled = false;

	public Camera(Point3f pos, Vector3f dir, Point2i dim, Vector3f up,
			float fovx) {
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
		distance = (float) (dimensions.x / (2 * Math
				.tan(Math.toRadians(fovX) / 2)));

		// Calculo del fovY
		fovY = 2 * (float) Math.atan(dimensions.y / (2 * distance));
		fovY = (float) Math.toDegrees(fovY);

		//Sacar
	//	colorchooser = new DistanceColorChooser(pos, colorvariator);
	}

	public void setColorChooser(PlainColorChooser chooser)
	{
		colorchooser = chooser;
	}
	
	public void Raytrace() {
		
		BufferedImage im = new BufferedImage(dimensions.x, dimensions.y,
				BufferedImage.TYPE_INT_RGB);
		Point3f startingPoint = new Point3f((position.x + direction.x
				* distance), (position.y + direction.y * distance),
				(position.z + direction.z * distance));

		Vector3f rightauxi = new Vector3f(right);
		rightauxi.scale((float) dimensions.x / 2);
		startingPoint.add(rightauxi);

		Vector3f upauxi = new Vector3f(up);
		upauxi.scale(-(float) dimensions.y / 2);
		startingPoint.add(upauxi);


		System.out.println("Starting Point del Plano de Proyeccion:"
				+ startingPoint);

		for (int i = 0; i < dimensions.x; i++) {

			for (int j = 0; j < dimensions.y; j++) {
				float coef = 1.0f;
				Color c = new Color(0,0,0);
				Point3f pixelPos = new Point3f(startingPoint);

				upauxi = new Vector3f(up);
				upauxi.scale(dimensions.y - j);
				pixelPos.add(upauxi);

				Vector3f dir = new Vector3f(pixelPos.x - position.x, pixelPos.y
						- position.y, pixelPos.z - position.z);
				dir.normalize();

				Ray ray = new Ray(dir, pixelPos);

				int level = 0;

				do {
					for (Object o : Scene.objects)
						o.Intersects(ray);

					if (ray.hit()) {
						
						Vector3f n = ray.getObject().getNormal(ray.getIntersectionPoint());
						
						if (lightingEnabled) 
						{
							c = lightchooser.getColor(ray, coef, c);
							coef *= ray.getObject().getReflection();

							Point3f newstartingPoint = ray.getIntersectionPoint();
							float reflet = 2 * n.dot(ray.direction);
							Vector3f newDirection = new Vector3f(ray.direction.x
									- reflet * n.x, ray.direction.y - reflet * n.y,
									ray.direction.z - reflet * n.z);

							ray = new Ray(newDirection, newstartingPoint);
							level++;
						}
						else	
							c = colorchooser.getColor(ray);
				
					} else
						coef = 0;
				} while (coef > 0.0f && level < MAX_REFLECTIONS && lightingEnabled);
				

				
				im.setRGB(i, j, c.getRGB());


			}

			rightauxi = new Vector3f(right);
			rightauxi.scale(-1);
			startingPoint.add(rightauxi);

		}
		File render = new File("render");
		try {
			ImageIO.write(im, extension, render);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
