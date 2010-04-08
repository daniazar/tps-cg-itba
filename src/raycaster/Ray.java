package raycaster;

import javax.vecmath.*;

public class Ray {

	public Vector3f direction;
	public Point3f position;
	public boolean hit = false;
	public boolean newbesthit = false;
	public Point3f intersectionPoint;
	private Object obj; 
	
	public Ray(Vector3f dir, Point3f pos)
	{
		this.direction = dir;
		this.position = pos;
		this.direction.normalize();

	}

	public void hit(Point3f IntPoint, Object o)
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
	
	public Object getObject()
	{
		return obj;
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
	
	public String toString()
	{
		 return "Ray:{Pos: "+position+",Dir: "+direction+"}";
	}
}
