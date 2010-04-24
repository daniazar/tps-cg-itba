package org.cg.primitives;

import java.awt.Color;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.boundingbox.BoundingBox;
import org.cg.raycaster.ray.Ray;


public abstract class Primitive implements Cloneable{

	Boolean colorHasBeenSet = false;
	BoundingBox boundingBox;
	
	public abstract Point3f getMiddlePoint();
	public abstract float getMaxDistanceFromMiddle();
	
	public Color getOrSetBaseColor(Color c) {
		if(colorHasBeenSet)
			return getBaseColor();
		synchronized (colorHasBeenSet) {
			if(colorHasBeenSet)
				return getBaseColor();
			
			colorHasBeenSet = true;
			setBaseColor(c);
			
			return c;
		}
	}
	
	public abstract void Intersects(Ray ray);
	
	public abstract Color getBaseColor();
	
	public abstract void setBaseColor(Color c);

	public abstract Vector3f getNormal(Point3f point);
	
	public abstract float getReflection();
	
	public abstract Point3f getAnyPoint();
	
	public abstract float getDistanceToClosestPoint(Point3f origin);
	
}
