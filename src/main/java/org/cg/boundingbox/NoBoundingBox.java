package org.cg.boundingbox;

import org.cg.primitives.Primitive;
import org.cg.primitives.Sphere;
import org.cg.raycaster.ray.Ray;

public class NoBoundingBox implements BoundingBox{
	
	Primitive primitive;
	public NoBoundingBox(Primitive primitive) {
			this.primitive = primitive;
	}

	@Override
	public boolean Intersects(Ray ray) {
		return true;
	}

	@Override
	public float MaxX() {
		return ((Sphere)primitive).MaxX();
	}

	@Override
	public float MaxY() {
		return ((Sphere)primitive).MaxY();
	}

	@Override
	public float MaxZ() {
		return ((Sphere)primitive).MaxZ();
	}

	@Override
	public float MinX() {
		return ((Sphere)primitive).MinX();
	}

	@Override
	public float MinY() {
		return ((Sphere)primitive).MinY();
	}

	@Override
	public float MinZ() {
		return ((Sphere)primitive).MinZ();
	}
	
}
