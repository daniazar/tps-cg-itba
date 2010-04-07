package raycaster;

import java.awt.Color;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class Plane extends Object {

	private Vector3f n;
	private float d;
	private Material material;
	
	public Plane(Vector3f n, Point3f p, Material mat)
	{
		this.d  = -n.dot(new Vector3f(p));
		this.n = n;
		this.material = mat;
	}
	@Override
	public void Intersects(Ray ray) {
		float den = ray.direction.dot(n); 

		if( den == 0)
		{
			ray.missed();
			return;
		}
		
		float num = -d - n.dot(new Vector3f(ray.position));
		
		if(num == 0)
		{
			ray.missed();
			return;
		}
		
		float t = num / den;
		
		if(t < 0)
			ray.missed();
		else
		{
		
			ray.hit(ray.getLinePoint(t), this);
		}

	}

	public Point3f GetIntersectionPoint(Ray ray){
		float den = ray.direction.dot(n); 

		if( den == 0)
		{
			ray.missed();
			return null;
		}
		
		double num = -d - n.dot(new Vector3f(ray.position));
		
		if(num == 0)
		{
			ray.missed();
			return null;
		}
		
		double t = num / den;
		
		if(t < 0)
		{
			ray.missed();
			return null;
		}
		else
		{
		
			return ray.getLinePoint((float)t);
		}
	}

	@Override
	public Color getColor() {
		return material.diffuse;
	}

	@Override
	public Vector3f getNormal(Point3f point) {
		return n;
	}

	@Override
	public float getReflection() {
		return material.reflection;
	}

}
