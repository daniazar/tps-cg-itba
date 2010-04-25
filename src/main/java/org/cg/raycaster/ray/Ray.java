package org.cg.raycaster.ray;

import javax.vecmath.*;

import org.cg.boundingbox.BoundingBox;
import org.cg.primitives.Primitive;

public class Ray {

	public Vector3f direction;
	public Point3f position;
	public boolean hit = false;
	public boolean newbesthit = false;
	public Point3f intersectionPoint;
	private Primitive obj;
	private BoundingBox bbox; 
	
	public Ray(Vector3f dir, Point3f pos)
	{
		this.direction = dir;
		this.position = pos;
		this.direction.normalize();

	}

	public void hit(Point3f IntPoint, Primitive o)
	{
		if(!hit)
		{
			intersectionPoint = IntPoint;
			newbesthit = true;
			hit = true;
			obj = o;
			return;
		}
		else
		{
			if(position.distance(IntPoint) < (position.distance(intersectionPoint)+ 0.1))
			{
				intersectionPoint = IntPoint;
				newbesthit = true;
				obj = o;

			}
			else
			{
				newbesthit = false;
			}

		}
	}

	public void hit(Point3f IntPoint, BoundingBox o)
	{
		if(!hit)
		{
			intersectionPoint = IntPoint;
			newbesthit = true;
			hit = true;
			setBoundingBox(o);
			return;
		}
		else
		{
			if(position.distance(IntPoint) < (position.distance(intersectionPoint)+ 0.1))
			{
				intersectionPoint = IntPoint;
				newbesthit = true;
				setBoundingBox(o);

			}
			else
			{
				newbesthit = false;
			}

		}
	}
	
	public Point3f getLinePoint(float t)
	{
		Point3f ans = new Point3f(direction);
		ans.scale(t);
		ans.add(position);
		
		return ans;
		
	}
	public void missed()
	{
		newbesthit = false;
		if(hit == true)
			return;
		else			
			hit = false;
	}

	public boolean hit()
	{
		return hit;
	}

	public boolean newBestHit()
	{
		return newbesthit;
	}

	public Point3f getIntersectionPoint()
	{
		return intersectionPoint;
	}
	
	public Primitive getObject()
	{
		return obj;
	}
	
	public Ray Refraction(){
		Vector3f n = obj.getNormal(intersectionPoint);

		Point3f newstartingPoint = intersectionPoint;
		float reflet = 2 * n.dot(direction);
		Vector3f newDirection = new Vector3f(
				direction.x - reflet * n.x,
				direction.y - reflet * n.y,
				direction.z - reflet * n.z);

		return new Ray(newDirection, newstartingPoint);
		
	}
	
	public boolean isPointInSegment(Point3f SegmentStart, Point3f SegmentFinish,
			Point3f p) 
	{


		
		if(!isPointInRay(p))
			return false;
		
		float distancesegment = SegmentStart.distance(SegmentFinish);
		float dStartP = SegmentStart.distance(p);
		float dFinishP = SegmentStart.distance(p);
		if(dStartP <( distancesegment + 0.1) && dFinishP < (distancesegment +0.1))
			return true;
		else
			return false;
	}
	
	public boolean isPointInRay(Point3f p)
	{
		float tx;
		float ty;
		float tz;
	
		tx = (p.x - position.x )/direction.x;
		ty = (p.y - position.y )/direction.y;
		tz = (p.z - position.z )/direction.z;

		if(p.epsilonEquals(position, 0.01f))
		{
			return true;
		}
		
		if(Math.abs(tx) - Math.abs(ty) > 0.01f)
			return false;
		
		if(Math.abs(ty) -  Math.abs(tz) > 0.01f)
			return false;
		
		return true;
	}
	
	public Ray Reflection(){
		
		Vector3f n = obj.getNormal(intersectionPoint);

		Point3f newstartingPoint = intersectionPoint;
		float reflet = 2 * n.dot(direction);
		Vector3f newDirection = new Vector3f(
				direction.x - reflet * n.x,
				direction.y - reflet * n.y,
				direction.z - reflet * n.z);

		return new Ray(direction, newstartingPoint);
	}
	public String toString()
	{
		 return "Ray:{Pos: "+position+",Dir: "+direction+"}";
	}

	public void setBoundingBox(BoundingBox bbox) {
		this.bbox = bbox;
	}

	public BoundingBox getBoundingBox() {
		return bbox;
	}
}
