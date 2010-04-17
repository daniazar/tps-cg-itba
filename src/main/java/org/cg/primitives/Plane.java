package org.cg.primitives;

import java.awt.Color;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.rendering.Material;

import raycaster.Ray;

public class Plane extends Primitive {

	private Vector3f n;
	private float d;
	private Material material;

	public Plane(Vector3f n, Point3f p, Material mat) {
		this.d = -n.dot(new Vector3f(p));
		this.n = n;
		this.material = mat;
	}

	@Override
	public void Intersects(Ray ray) {
		float den = ray.direction.dot(n);

		if (Math.abs(den) < 0.01f) {
			ray.missed();
			return;
		}

		float num = -d - n.dot(new Vector3f(ray.position));

		if (Math.abs(num) < 0.01f) {
			ray.missed();
			return;
		}

		float t = num / den;

		if (t < 0.1) {
			ray.missed();
		} else {

			ray.hit(ray.getLinePoint(t), this);
		}

	}

	public Point3f GetIntersectionPoint(Ray ray) {
		float den = ray.direction.dot(n);

		if (Math.abs(den) < 0.01f) {
			ray.missed();
			return null;
		}

		double num = -d - n.dot(new Vector3f(ray.position));

		if (Math.abs(num) < 0.01f) {
			ray.missed();
			return null;
		}

		double t = num / den;

		if (t < 0.1) {
			ray.missed();
			return null;
		} else {

			return ray.getLinePoint((float) t);
		}
	}

	@Override
	public Color getColor() {
		return material.getDiffuse();
	}

	@Override
	public Vector3f getNormal(Point3f point) {
		return n;
	}

	@Override
	public float getReflection() {
		return material.getReflection();
	}

	@Override
	public void setColor(Color c) {
		this.material.setDiffuse(c);

	}

	@Override
	public Point3f getAnyPoint() {
		return new Point3f(n.x, n.y, n.z + d);
	}

	public boolean equals(Primitive o) {

		if (!(o instanceof Plane))
			return false;
		else {
			Plane p = (Plane) o;
			if (p.d == d && p.n.equals(n))
				return true;
			else
				return false;
		}
	}

	public String toString() {
		return "Plane";
	}

	@Override
	public float getDistanceToClosestPoint(Point3f origin) {
		return getAnyPoint().distance(origin);
	}

}
