package org.cg.primitives;

import java.awt.Color;

import javax.vecmath.*;

import org.cg.rendering.Material;

import raycaster.Ray;

public class Sphere extends Primitive {

	public Point3f center;
	private float radius;
	private Material material;
	
	public Sphere(Point3f center, float radius, Material mat)
	{
		this.radius = radius;
		this.center = center;
		this.material = mat;
	}
	
	public void Intersects(Ray ray){
;
		float B = 2*ray.direction.x*(ray.position.x - center.x) +
				  2*ray.direction.y*(ray.position.y - center.y) +
				  2*ray.direction.z*(ray.position.z - center.z);
		
		float C = (float)(Math.pow((ray.position.x - center.x), 2)+
					Math.pow(ray.position.y - center.y, 2)+
					Math.pow(ray.position.z - center.z, 2)-
					Math.pow(radius, 2));
		
		float discr  = (float)(Math.pow(B, 2) - 4*C);
		
		if(discr <= 0)
		{
			ray.missed();
			return;
		}
		
		float t0 = (float)((-B - Math.sqrt(Math.pow(B, 2) - 4*C)) / 2);
		if(t0 < 0.0001 )
		{
			float t1 = (float)((-B + Math.sqrt(Math.pow(B, 2) - 4*C)) / 2);
			if(t1 > 0.0001f)
			{
				ray.hit(new Point3f(ray.position.x + t1*ray.direction.x,
						ray.position.y + t1*ray.direction.y, 
						ray.position.z + t1*ray.direction.z), this);
				return ;
			}
			else
			{
				ray.missed();
				return;
			}
				
		}
		else if(t0 > 0.0001f)
		{
			ray.hit(new Point3f(ray.position.x + t0*ray.direction.x,
					ray.position.y + t0*ray.direction.y, 
					ray.position.z + t0*ray.direction.z), this);
			return;
		}
		else
		{
			ray.missed();
			return;
		}
		
	}

	@Override
	public Color getColor() {
		return material.getDiffuse();
	}
	
	public float getReflection()
	{
		return material.getReflection();
	}



	@Override
	public Vector3f getNormal(Point3f point) {
		Vector3f n = new Vector3f(point.x - center.x,
				point.y- center.y, point.z-center.z);
		n.normalize();
		return n;
	}

	@Override
	public void setColor(Color c) {
		this.material.setDiffuse(c);
		
	}

	@Override
	public Point3f getAnyPoint() {
		return new Point3f(center.x, center.y, center.z - radius);
	}

	public boolean equals(Primitive o)
	{
		if(!(o instanceof Sphere))
			return false;
		else
		{
			Sphere s = (Sphere)o;
			if(s.radius == radius && s.center.equals(center) )
				return true;
			else
				return false;
		}
	}
	
	public String toString()
	{
		return "Esfera";
	}



	@Override
	public float getDistanceToClosestPoint(Point3f origin) {
		Vector3f dirPointSphere = new Vector3f(center.x - origin.x ,
				center.y - origin.y, center.z - origin.z);
		Ray ray = new Ray(dirPointSphere,origin);
		Intersects(ray);
		return ray.intersectionPoint.distance(origin);
	}




	
}