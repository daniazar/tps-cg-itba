/**
 * 
 */
package org.cg.primitives;

import java.awt.Color;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.raycaster.ray.Ray;
import org.cg.rendering.Material;

/**
 * @author dani
 *
 */
public class Box extends Primitive {

	
	private Quadrilateral q1, q2, q3, q4, q5, q6;

	public Box(Point3f pt1, Point3f pt2, Point3f pt3, Point3f pt4,
			Point3f pt5, Point3f pt6, Point3f pt7, Point3f pt8,
			Material material) {
		q1 = new Quadrilateral(pt1, pt2, pt4, pt3, material);
		q2 = new Quadrilateral(pt2, pt4, pt6, pt5, material);
		q3 = new Quadrilateral(pt3, pt4, pt6, pt7, material);
		q4 = new Quadrilateral(pt1, pt2, pt5, pt8, material);
		q5 = new Quadrilateral(pt7, pt8, pt5, pt6, material);
		q6 = new Quadrilateral(pt1, pt3, pt7, pt8, material);
		
	}

	
	
	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#Intersects(org.cg.raycaster.ray.Ray)
	 */
	@Override
	public void Intersects(Ray ray) {
		q1.Intersects(ray);
		q2.Intersects(ray);
		q3.Intersects(ray);
		q4.Intersects(ray);
		q5.Intersects(ray);
		q6.Intersects(ray);
		/*if (!ray.hit) {
			return;
		} else if (ray.getObject().equals(q1) || ray.getObject().equals(q2) || ray.getObject().equals(q3) 
		|| ray.getObject().equals(q4) || ray.getObject().equals(q5) || ray.getObject().equals(q6)) {
			ray.hit(ray.intersectionPoint, this);
		} else {
			ray.missed();
		}

		return;*/
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getAnyPoint()
	 */
	@Override
	public Point3f getAnyPoint() {
		return q1.getAnyPoint();
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getBaseColor()
	 */
	@Override
	public Color getBaseColor() {
		return q1.getBaseColor();
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getDistanceToClosestPoint(javax.vecmath.Point3f)
	 */
	@Override
	public float getDistanceToClosestPoint(Point3f origin) {
		return getAnyPoint().distance(origin);
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getNormal(javax.vecmath.Point3f)
	 */
	@Override
	public Vector3f getNormal(Point3f point) {
		return q1.getNormal(point);

	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getReflection()
	 */
	@Override
	public float getReflection() {
		return q1.getReflection();
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#setBaseColor(java.awt.Color)
	 */
	@Override
	public void setBaseColor(Color c) {
			q1.setBaseColor(c);
			q2.setBaseColor(c);
			q3.setBaseColor(c);
			q4.setBaseColor(c);
			q5.setBaseColor(c);
			q6.setBaseColor(c);

		
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((q1 == null) ? 0 : q1.hashCode());
		result = prime * result + ((q2 == null) ? 0 : q2.hashCode());
		result = prime * result + ((q3 == null) ? 0 : q3.hashCode());
		result = prime * result + ((q4 == null) ? 0 : q4.hashCode());
		result = prime * result + ((q5 == null) ? 0 : q5.hashCode());
		result = prime * result + ((q6 == null) ? 0 : q6.hashCode());
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Box other = (Box) obj;
		if (q1 == null) {
			if (other.q1 != null)
				return false;
		} else if (!q1.equals(other.q1))
			return false;
		if (q2 == null) {
			if (other.q2 != null)
				return false;
		} else if (!q2.equals(other.q2))
			return false;
		if (q3 == null) {
			if (other.q3 != null)
				return false;
		} else if (!q3.equals(other.q3))
			return false;
		if (q4 == null) {
			if (other.q4 != null)
				return false;
		} else if (!q4.equals(other.q4))
			return false;
		if (q5 == null) {
			if (other.q5 != null)
				return false;
		} else if (!q5.equals(other.q5))
			return false;
		if (q6 == null) {
			if (other.q6 != null)
				return false;
		} else if (!q6.equals(other.q6))
			return false;
		return true;
	}

}
