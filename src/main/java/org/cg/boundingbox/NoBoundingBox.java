package org.cg.boundingbox;

import org.cg.primitives.Primitive;
import org.cg.raycaster.ray.Ray;

public class NoBoundingBox implements BoundingBox{
	
	public NoBoundingBox(Primitive primitive) {

	}

	@Override
	public boolean Intersects(Ray ray) {
		return true;
	}
	
}
