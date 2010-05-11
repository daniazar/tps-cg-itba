package org.cg.boundingbox;

import javax.vecmath.Point3f;

import org.cg.raycaster.ray.Ray;


public interface BoundingBox{

	public boolean Intersects(Ray ray);
	
	public float MaxX();
	public float MaxY();
	public float MaxZ();
	public float MinX();
	public float MinY();
	public float MinZ();
	
	
}
