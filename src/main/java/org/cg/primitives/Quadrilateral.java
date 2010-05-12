package org.cg.primitives;

import java.awt.Color;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.boundingbox.SphereBoundingBox;
import org.cg.raycaster.ray.Ray;
import org.cg.rendering.Material;


public class Quadrilateral extends Primitive {

	private Triangle t1, t2;
	private Point3f middlePoint;
	private float maxDistanceFromMiddle;

	public Quadrilateral(Point3f pt1, Point3f pt2, Point3f pt3, Point3f pt4,
			Material material) {
		t1 = new Triangle(pt1, pt4, pt2, material);
		t2 = new Triangle(pt4, pt3, pt2, material);
		
		middlePoint = new Point3f();
		middlePoint.x = (pt1.x + pt2.x + pt3.x + pt4.x) / 4;
		middlePoint.y = (pt1.y + pt2.y + pt3.y + pt4.y) / 4;
		middlePoint.z = (pt1.z + pt2.z + pt3.z + pt4.z) / 4;
		
		maxDistanceFromMiddle = Math.max(
									Math.max(middlePoint.distance(pt1), middlePoint.distance(pt2)), 
									Math.max(middlePoint.distance(pt3), middlePoint.distance(pt4))
									);
		
		boundingBox = new SphereBoundingBox(this);
	}

	@Override
	public float getRefraction() {
		return t1.getRefraction();
	}
	
	@Override
	public boolean Intersects(Ray ray) {
		if (!t1.Intersects(ray)){
			t2.Intersects(ray);
		}
		
		
		
		if (!ray.hit) {
			return false;
		} else if (ray.getObject().equals(t1) || ray.getObject().equals(t2)) {
			ray.hit(ray.intersectionPoint, this);
		} else {
			ray.missed();
		}

		return true;
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

	@Override
	public float getMaxDistanceFromMiddle() {
		return maxDistanceFromMiddle;
	}

	@Override
	public Point3f getMiddlePoint() {
		return middlePoint;
	}

	@Override
	public Material getMaterial() {
		return t1.getMaterial();
	}

	@Override
	public Color getTextureColor(Point3f intersectionPoint) {
		// TODO change to correct implementation
		return getBaseColor();
	}
}
