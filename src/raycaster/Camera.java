package raycaster;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
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
	public Point2f di;
	public float fovX;
	public float fovY;
	public String extension = "png";
	
	private ColorVariator colorvariator = new LinearColorVariator();
	private PlainColorChooser colorchooser = new ObjectColorChooser(colorvariator);
	
	private LightColorChooser lightchooser = new LambertianColorChooser();
	private int MAX_REFLECTIONS = 0;
	private boolean lightingEnabled = false;

	public Camera(Point3f pos, Vector3f dir, Point2i dim, Vector3f up,
			float fovx) {
		distance = 1;
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

		di.x = (float) (distance * Math.tan(Math.toRadians(fovX/2))) ;


		// 	Calculo del fovY
		fovY = ((float)dimensions.y)/ ((float)dimensions.x)*fovX;


		di.y = (float) (distance * Math.tan(Math.toRadians(fovY/2))) ;

		right.scale(-di.x /dimensions.x);
		this.up.scale(-di.y /dimensions.y);
		
	}
	
	public  void setImageDim(int x, int y)
	{
		right.scale(((float)dimensions.x)/-di.x);
		this.up.scale(((float)dimensions.y)/-di.y);
		Point2i dim = new Point2i(x,y);
		dimensions = dim;
		dimensions.absolute();
        di = new Point2f();
		fovY = ((float)dimensions.y)/ ((float)dimensions.x)*fovX;
		di.y = (float) (distance * Math.tan(Math.toRadians(fovY/2))) ;
		di.x = (float) (distance * Math.tan(Math.toRadians(fovX/2))) ;
		right.scale(-di.x /dimensions.x);
		this.up.scale(-di.y /dimensions.y);
	}
	
	public  void setImageFov(float fov)
	{
		fovX = fov;		
		right.scale(((float)dimensions.x)/-di.x);
		this.up.scale(((float)dimensions.y)/-di.y);
		di = new Point2f();
		fovY = ((float)dimensions.y)/ ((float)dimensions.x)*fovX;
		di.y = (float) (distance * Math.tan(Math.toRadians(fovY/2))) ;
		di.x = (float) (distance * Math.tan(Math.toRadians(fovX/2))) ;
		right.scale(-di.x /dimensions.x);
		this.up.scale(-di.y /dimensions.y);
	}
	
	

	public void setColorMode(String mode, String variation) throws Exception
	{
		ColorVariator var = null;
		PlainColorChooser chooser;
		
		if(variation.equalsIgnoreCase("linear"))
		{
			var = new LinearColorVariator();
		}
		else if(variation.equalsIgnoreCase("log"))
		{
			var = new LogColorVariator();
		}
			
		if(mode.equalsIgnoreCase("random"))
		{
			chooser = new RandomColorChooser(var);
		}
		else if(mode.equalsIgnoreCase("distance"))
		{
			chooser = new DistanceColorChooser(position, var);
		}
		else
			throw new Exception("Valid Args are: linear, log, random, distance. "+mode+";"+variation);
			
			
	}
	public void Raytrace() {
		
		BufferedImage im = new BufferedImage(dimensions.x, dimensions.y,
				BufferedImage.TYPE_INT_RGB);
		Point3f startingPoint = new Point3f((position.x + direction.x
				* distance), (position.y + direction.y * distance),
				(position.z + direction.z * distance));

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
				float coef = 1.0f;
				Color c = new Color(0,0,0);
				Point3f pixelPos = new Point3f(startingPoint);

				upauxi = new Vector3f(up);
				upauxi.scale(j*2);
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
			rightauxi.scale(-2);
			startingPoint.add(rightauxi);

		}
		File render = new File(Parameters.o);
		try {
			if(Parameters.o.contains(".bmp"))
				extension = "bmp";
			else
				extension = "png";
			
			ImageIO.write(im, extension, render);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}