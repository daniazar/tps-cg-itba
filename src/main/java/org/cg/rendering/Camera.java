package org.cg.rendering;

import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.rendering.color.ColorVariator;
import org.cg.rendering.color.DistanceColorChooser;
import org.cg.rendering.color.LambertianColorChooser;
import org.cg.rendering.color.LightColorChooser;
import org.cg.rendering.color.LinearColorVariator;
import org.cg.rendering.color.LogColorVariator;
import org.cg.rendering.color.ObjectColorChooser;
import org.cg.rendering.color.PlainColorChooser;
import org.cg.rendering.color.RandomColorChooser;

public class Camera {

	public Point3f position;
	public Vector3f direction;
	public Vector3f up;
	public Vector3f right;
	public Point dimensions;
	public float distance;
	public Point2f di;
	public float fovX;
	public float fovY;

	private ColorVariator colorvariator = new LinearColorVariator();
	private PlainColorChooser colorchooser = new RandomColorChooser(colorvariator);
	
	private LightColorChooser lightchooser = new LambertianColorChooser();
	private boolean lightingEnabled = true;

	public Camera(Point3f pos, Vector3f dir, Point dim, Vector3f up, float fovx) {
		distance = 1;
		position = pos;
		direction = dir;
		direction.normalize();
		this.up = up;
		this.up.normalize();
		fovX = fovx;
		right = new Vector3f();
		right.cross(direction, up);
		dimensions = dim;
	}

	private void makeAbsolute(Point d) {
		d.x = Math.abs(d.x);
		d.y = Math.abs(d.y);
	}

	public void setImageDim(int x, int y) {
		dimensions = new Point(x, y);
	}

	public void setImageFov(float fov) {
		fovX = fov;
	}

	public void prepare() {
		makeAbsolute(dimensions);

		// distancia focal a partir del fov y la imagen
		di = new Point2f();
		di.x = (float) (distance * Math.tan(Math.toRadians(fovX / 2)));

		// Calculo del fovY
		fovY = ((float) dimensions.y) / ((float) dimensions.x) * fovX;
		di.y = (float) (distance * Math.tan(Math.toRadians(fovY / 2)));

		right.normalize();
		up.normalize();
		right.scale(-di.x / dimensions.x);
		up.scale(-di.y / dimensions.y);
	}

	public void setColorMode(String mode, String variation) throws Exception {

		if (variation.equalsIgnoreCase("linear")) {
			colorvariator = new LinearColorVariator();
		} else if (variation.equalsIgnoreCase("log")) {
			colorvariator = new LogColorVariator();
		}
		if (mode.equalsIgnoreCase("object")) {
			colorchooser = new ObjectColorChooser(colorvariator);
		} else if (mode.equalsIgnoreCase("random")) {
			colorchooser = new RandomColorChooser(colorvariator);
		} else if (mode.equalsIgnoreCase("distance")) {
			colorchooser = new DistanceColorChooser(position, colorvariator);
		} else
			throw new Exception(
					"Valid Args are: linear, log, random, distance. " + mode
							+ ";" + variation);
	}

	public BufferedImage getBufferedImage() {
		return new BufferedImage(dimensions.x, dimensions.y,BufferedImage.TYPE_INT_RGB);
	}

	public Point3f getStartingPoint() {
		return new Point3f((position.x + direction.x
				* distance), (position.y + direction.y * distance),
				(position.z + direction.z * distance));
	}

	public void setLightingEnabled(boolean lightingEnabled) {
		this.lightingEnabled = lightingEnabled;
	}

	public boolean isLightingEnabled() {
		return lightingEnabled;
	}

	public void setLightchooser(LightColorChooser lightchooser) {
		this.lightchooser = lightchooser;
	}

	public LightColorChooser getLightchooser() {
		return lightchooser;
	}

	public void setColorchooser(PlainColorChooser colorchooser) {
		this.colorchooser = colorchooser;
	}

	public PlainColorChooser getColorchooser() {
		return colorchooser;
	}

}