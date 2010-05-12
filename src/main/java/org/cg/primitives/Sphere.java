package org.cg.primitives;

import java.awt.Color;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.boundingbox.NoBoundingBox;
import org.cg.raycaster.ray.Ray;
import org.cg.rendering.Material;
import org.cg.rendering.shader.Shader;
import org.cg.rendering.shader.ShaderType;
import org.cg.rendering.shader.TextureManager;


public class Sphere extends Primitive {

	private Point3f center;
	private float radius;
	private Material material;
	private Shader shader;
	public Point3f getCenter() {
		return center;
	}

	public void setCenter(Point3f center) {
		this.center = center;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Sphere() {
		this.radius = 0;
		this.center = new Point3f(0,0,0);;
		this.material = null;
	}
	
	public Sphere(Point3f center, float radius, Material mat) {
		this.radius = radius;
		this.center = center;
		this.material = mat;
		boundingBox = new NoBoundingBox(this);
	}

	public Sphere(Point3f center, float radius, Material mat, Shader shader) {
		this.setShader(shader);
		this.radius = radius;
		this.center = center;
		this.material = mat;
		boundingBox = new NoBoundingBox(this);
	}
	
	public boolean Intersects(Ray ray) {

		float B = 2 * ray.direction.x * (ray.position.x - center.x) + 2
				* ray.direction.y * (ray.position.y - center.y) + 2
				* ray.direction.z * (ray.position.z - center.z);

		float C = (float) (Math.pow((ray.position.x - center.x), 2)
				+ Math.pow(ray.position.y - center.y, 2)
				+ Math.pow(ray.position.z - center.z, 2) - Math.pow(radius, 2));

		float discr = (float) (Math.pow(B, 2) - 4 * C);

		if (discr < 0.1) {
			ray.missed();
			return false;
		}

		float t0 = (float) ((-B - Math.sqrt(Math.pow(B, 2) - 4 * C)) / 2);
		if (t0 < 0.1) {
			float t1 = (float) ((-B + Math.sqrt(Math.pow(B, 2) - 4 * C)) / 2);
			if (t1 > 0.1f) {
				ray.hit(new Point3f(ray.position.x + t1 * ray.direction.x,
						ray.position.y + t1 * ray.direction.y, ray.position.z
								+ t1 * ray.direction.z), this);
				return true;
			} else {
				ray.missed();
				return false;
			}

		} else if (t0 > 0.1f) {
			ray.hit(new Point3f(ray.position.x + t0 * ray.direction.x,
					ray.position.y + t0 * ray.direction.y, ray.position.z + t0
							* ray.direction.z), this);
			return true;
		} else {
			ray.missed();
			return false;
		}

	}

	@Override
	public Color getBaseColor() {
		return material.getDiffuse();
	}
	@Override
	public float getRefraction() {
		return material.getRefraction();
	}

	public float getReflection() {
		return material.getReflection();
	}

	@Override
	public Vector3f getNormal(Point3f point) {
		Vector3f n = new Vector3f(point.x - center.x, point.y - center.y,
				point.z - center.z);
		n.normalize();
		return n;
	}

	@Override
	public void setBaseColor(Color c) {
		this.material.setDiffuse(c);

	}

	@Override
	public Point3f getAnyPoint() {
		return new Point3f(center.x, center.y, center.z - radius);
	}

	public boolean equals(Primitive o) {
		if (!(o instanceof Sphere))
			return false;
		else {
			Sphere s = (Sphere) o;
			if (s.radius == radius && s.center.equals(center))
				return true;
			else
				return false;
		}
	}

	@Override
	public float getDistanceToClosestPoint(Point3f origin) {
		Vector3f dirPointSphere = new Vector3f(center.x - origin.x, center.y
				- origin.y, center.z - origin.z);
		Ray ray = new Ray(dirPointSphere, origin);
		Intersects(ray);
		return ray.intersectionPoint.distance(origin);
	}

	@Override
	public String toString() {
		return "Sphere [center=" + center + ", material=" + material
				+ ", radius=" + radius + "]";
	}

	@Override
	public float getMaxDistanceFromMiddle() {
		return radius;
	}

	@Override
	public Point3f getMiddlePoint() {
		return center;
	}
	
	public Object clone()
	{
		Sphere s = new Sphere(center,radius,material);
		s.boundingBox = new  NoBoundingBox(s);
		return s;
		
	}
	
	public float MaxX() {
		return center.x + radius;
	}


	public float MaxY() {
		return center.y + radius;
	}

	
	public float MaxZ() {
		return center.z + radius;
	}

	public float MinX() {
		return center.x - radius;
	}


	public float MinY() {
		return center.y - radius;
	}

	
	public float MinZ() {
		return center.z - radius;
	}

	@Override
	public Color getTextureColor(Point3f intersectionPoint) {
		if(this.shader.getType() == ShaderType.PHONG) {
			Point3f point = (Point3f) intersectionPoint.clone();
			point.sub(this.center);
			
			float u = (float) (point.x / (Math.pow(point.x, 2) + Math.pow(point.y, 2) + Math.pow(point.z, 2))) + 0.5f;
			float v = (float) (point.z / (Math.pow(point.x, 2) + Math.pow(point.y, 2) + Math.pow(point.z, 2))) + 0.3f;
			
			Color color;
			if(shader.getTexturePath() != null) {
				color = TextureManager.get(shader.getTexturePath()).getUV(u,v);
			} else {
				color = getBaseColor();
			}
			return color;
		}
		return getBaseColor();
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}

	public Shader getShader() {
		return shader;
	}
	
}
