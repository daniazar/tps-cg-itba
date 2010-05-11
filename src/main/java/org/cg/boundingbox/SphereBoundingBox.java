package org.cg.boundingbox;

import javax.vecmath.Point3f;

import org.cg.primitives.Primitive;
import org.cg.raycaster.ray.Ray;

public class SphereBoundingBox implements BoundingBox{

	private Point3f center;
	private float radius;
	
	public SphereBoundingBox(Primitive primitive) {
		this.center = primitive.getMiddlePoint();
		this.radius = primitive.getMaxDistanceFromMiddle();
	}

	@Override
	public boolean Intersects(Ray ray) {

		float B = 2 * ray.direction.x * (ray.position.x - center.x) + 2
				* ray.direction.y * (ray.position.y - center.y) + 2
				* ray.direction.z * (ray.position.z - center.z);

		float C = (float) (Math.pow((ray.position.x - center.x), 2)
				+ Math.pow(ray.position.y - center.y, 2)
				+ Math.pow(ray.position.z - center.z, 2) - Math.pow(radius, 2));

		float discr = (float) (Math.pow(B, 2) - 4 * C);

		if (discr <= 0) {
			return false;
		}

		float t0 = (float) ((-B - Math.sqrt(Math.pow(B, 2) - 4 * C)) / 2);
		if (t0 < 0.0001) {
			float t1 = (float) ((-B + Math.sqrt(Math.pow(B, 2) - 4 * C)) / 2);
			if (t1 > 0.0001f) {
				return true;
			} else {
				return false;
			}

		} else if (t0 > 0.0001f) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public float MaxX() {
		return center.x + radius;
	}

	@Override
	public float MaxY() {
		return center.y + radius;
	}

	@Override
	public float MaxZ() {
		return center.z + radius;
	}

	@Override
	public float MinX() {
		return center.x - radius;
	}

	@Override
	public float MinY() {
		return center.y - radius;
	}

	@Override
	public float MinZ() {
		return center.z - radius;
	}
	
}
