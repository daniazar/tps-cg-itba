package org.cg.primitives;

import java.awt.Color;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import raycaster.Ray;

public abstract class Primitive implements Cloneable{

	public abstract void Intersects(Ray ray);
	
	public abstract Color getColor();
	
	public abstract Vector3f getNormal(Point3f point);
	
	public abstract float getReflection();
	
	public abstract void setColor(Color c);
	
	public abstract Point3f getAnyPoint();
	
	public abstract float getDistanceToClosestPoint(Point3f origin);
	
}
