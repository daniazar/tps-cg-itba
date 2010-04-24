package org.cg.boundingbox;

import javax.vecmath.Point3f;

import org.cg.primitives.Primitive;

public class SphereBoundingBox extends BoundingBox{

	private Point3f center;
	private float radius;
	
	public SphereBoundingBox(Primitive primitive) {
		this.center = primitive.getMiddlePoint();
		this.radius = primitive.getMaxDistanceFromMiddle();
	}
	
}
