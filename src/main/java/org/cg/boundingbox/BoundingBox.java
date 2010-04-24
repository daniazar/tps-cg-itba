package org.cg.boundingbox;

import org.cg.raycaster.ray.Ray;


public interface BoundingBox{

	public boolean Intersects(Ray ray);
	
}
