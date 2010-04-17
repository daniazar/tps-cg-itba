package org.cg.primitives;

import java.awt.Color;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.raycaster.ray.Ray;
import org.cg.rendering.Material;


public class Quadrilateral extends Primitive {

	private Triangle t1, t2;

	public Quadrilateral(Point3f pt1, Point3f pt2, Point3f pt3, Point3f pt4,
			Material material) {
		t1 = new Triangle(pt1, pt2, pt4, material);
		t2 = new Triangle(pt4, pt2, pt3, material);
	}

	@Override
	public void Intersects(Ray ray) {
		t1.Intersects(ray);
		t2.Intersects(ray);
		if (!ray.hit) {
			return;
		} else if (ray.getObject().equals(t1) || ray.getObject().equals(t2)) {
			ray.hit(ray.intersectionPoint, this);
		} else {
			ray.missed();
		}

		return;
	}

	@Override
	public Color getBaseColor() {
		return t1.getBaseColor();
	}

	@Override
	public Vector3f getNormal(Point3f point) {
		return t1.getNormal(point);
	}

	@Override
	public float getReflection() {
		return t1.getReflection();
	}

	@Override
	public void setBaseColor(Color c) {
		t1.setBaseColor(c);
		t2.setBaseColor(c);

	}

	@Override
	public Point3f getAnyPoint() {
		return t1.getAnyPoint();
	}

	public boolean equals(Primitive o) {

		if (o instanceof Quadrilateral) {
			Quadrilateral q = (Quadrilateral) o;
			if ((q.t1.equals(t1) && q.t2.equals(t2)) || (q.t1.equals(t2) && q.t2.equals(t1)))
				return true;
		}
		return false;

	}

	@Override
	public float getDistanceToClosestPoint(Point3f origin) {
		return getAnyPoint().distance(origin);
	}

	@Override
	public String toString() {
		return "Quadrilateral [t1=" + t1 + ", t2=" + t2 + "]";
	}

}
