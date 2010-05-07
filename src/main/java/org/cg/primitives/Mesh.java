/**
 * 
 */
package org.cg.primitives;

import java.awt.Color;
import java.util.ArrayList;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.boundingbox.SphereBoundingBox;
import org.cg.raycaster.ray.Ray;
import org.cg.rendering.Material;

/**
 * @author dani
 *
 */
public class Mesh extends Primitive {

	private ArrayList<Triangle> list;
	private Point3f middlePoint;

	private float maxDistanceFromMiddle = 0;

	public Mesh(Point3f[] pts1,Point3f[] pts2, Point3f[] pts3 , Vector3f[] normals, Material material) {
		list = new ArrayList<Triangle>();
		middlePoint = new Point3f(0,0,0);
		int count = 0;
		for (int i = 0; i < pts1.length; i++) {
			list.add(new Triangle(pts1[i], pts2[i], pts3[i],normals[i], material));
			middlePoint.x+=(pts1[i].x + pts2[i].x + pts3[i].x);
			middlePoint.y+=(pts1[i].y + pts2[i].y + pts3[i].y);
			middlePoint.z+=(pts1[i].z + pts2[i].z + pts3[i].z);
			
			count+=3;
		}
		
		
		
		middlePoint.x = middlePoint.x / count;
		middlePoint.y = middlePoint.y / count;
		middlePoint.z = middlePoint.z / count;
		
		for (int i = 0; i < pts1.length; i++) {
			maxDistanceFromMiddle = Math.max(Math.max(middlePoint.distance(pts1[i]), middlePoint.distance(pts2[i])), middlePoint.distance(pts3[i]));			
		}
		boundingBox = new SphereBoundingBox(this);

	}
	
	
	public boolean Intersects(Ray ray) {
		
		for (Triangle t : list) {
			t.Intersects(ray);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getAnyPoint()
	 */
	@Override
	public Point3f getAnyPoint() {
		return list.get(0).getAnyPoint();
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getBaseColor()
	 */
	@Override
	public Color getBaseColor() {
		return list.get(0).getBaseColor();
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getDistanceToClosestPoint(javax.vecmath.Point3f)
	 */
	@Override
	public float getDistanceToClosestPoint(Point3f origin) {
		return  list.get(0).getDistanceToClosestPoint(origin);
		
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getMaxDistanceFromMiddle()
	 */
	@Override
	public float getMaxDistanceFromMiddle() {
		return maxDistanceFromMiddle; 
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getMiddlePoint()
	 */
	@Override
	public Point3f getMiddlePoint() {
		return middlePoint;
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getNormal(javax.vecmath.Point3f)
	 */
	@Override
	public Vector3f getNormal(Point3f point) {
		return  list.get(0).getNormal(point);
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getReflection()
	 */
	@Override
	public float getReflection() {
		return  list.get(0).getReflection();
	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#getRefraction()
	 */
	@Override
	public float getRefraction() {
		return  list.get(0).getRefraction();

	}

	/* (non-Javadoc)
	 * @see org.cg.primitives.Primitive#setBaseColor(java.awt.Color)
	 */
	@Override
	public void setBaseColor(Color c) {
		for (Triangle t : list) {
			t.setBaseColor(c);
		}  
	}

}
