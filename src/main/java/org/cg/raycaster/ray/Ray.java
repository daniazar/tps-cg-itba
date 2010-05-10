package org.cg.raycaster.ray;

import javax.vecmath.*;

import org.cg.boundingbox.BoundingBox;
import org.cg.primitives.Primitive;
import org.cg.rendering.Material;

public class Ray {

	public Vector3f direction;
	public Point3f position;
	public boolean hit = false;
	public boolean newbesthit = false;
	public Point3f intersectionPoint;
	private Primitive obj;
	private BoundingBox bbox; 
	private float cosThetaI;
	private float cosThetaO;
	private float sinThetaI;
	private float sinThetaO;
	private float reflectance;
	private float oldRefraction;
	private float newRefraction;
	private static float AIR_REFRACTION = 1.0f;
	
	public Ray(Vector3f dir, Point3f pos)
	{
		this.direction = dir;
		this.position = pos;
		this.direction.normalize();
		newRefraction = AIR_REFRACTION;

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
		
		Vector3f normal = getObject().getNormal(getIntersectionPoint());
		boolean inside = (normal.dot(direction) > 0.0f);
		oldRefraction = newRefraction;
		if (inside)
		{
			normal.scale(-1);
			//Estoy saliendo del medio
			newRefraction = AIR_REFRACTION; 
			
		}
		else
			newRefraction = getObject().getMaterial().getRefraction();
		
		Point3f start = getIntersectionPoint();
		Vector3f vN = new Vector3f(normal);
		vN.scale(cosThetaI);
		Vector3f newDir = new Vector3f(direction);
		newDir.add(vN);
		newDir.scale(oldRefraction/newRefraction);
		vN = new Vector3f(normal);
		vN.scale(-cosThetaO);
		newDir.add(vN);
		return new Ray(newDir,start);
		
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
		return new Ray(newDirection, newstartingPoint);
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
	
	//Ecuaciones de Fresnel, devuelvo la Reflectancia
	//a partir de esto calculo el coeficiente de Transmitancia
	//y Reflectividad
	
	public float Reflectance()
	{
		Material mat = this.getObject().getMaterial();
		float reflection = mat.getReflection();
		float refraction = mat.getRefraction();
		float density = mat.getDensity();

		Vector3f normal = new Vector3f(getObject().getNormal(getIntersectionPoint()));
		boolean inside = (normal.dot(direction) > 0.0f);
		if (inside)
			normal.scale(-1);

		if (reflection != 0 || refraction != 0 || density != 0) {
	
			float density1 = newRefraction;
			float density2;
			if (inside)
				density2 = AIR_REFRACTION;
			else
				density2 = density;

			cosThetaI = Math.abs(direction.dot(normal));

			if (cosThetaI > 0.9999f) {
				reflectance = (density1 - density2) / (density1 + density2);
				reflectance = reflectance * reflectance;
				sinThetaI = 0;
				sinThetaO = 0;
				cosThetaO = 1;

			} else {
				sinThetaI = (float) Math.sqrt(1 - cosThetaI * cosThetaI);
				sinThetaO = (density1 / density2) * sinThetaI;

				if (sinThetaI * sinThetaO > 0.9999f) {
					reflectance = 1;
					cosThetaO = 0;
				} else {

					cosThetaO = (float) Math.sqrt(1 - sinThetaO * sinThetaO);

					float reflectanceOrtho = (density1 * cosThetaO - density1
							* cosThetaI)
							/ (density2 * cosThetaO + density1 * cosThetaI);

					reflectanceOrtho = reflectanceOrtho * reflectanceOrtho;

					float reflectanceParal = (density1 * cosThetaO - density2
							* cosThetaI)
							/ (density1 * cosThetaO + density2 * cosThetaI);

					reflectanceParal = reflectanceParal * reflectanceParal;

					// The reflectance coefficient is the average of those two.
					// If we consider a light that hasn't been previously
					// polarized.
					reflectance = 0.5f * (reflectanceOrtho + reflectanceParal);
				}

			}
		}
		else
		{
			reflectance = 1.0f;
			cosThetaI = 1.0f;
			cosThetaO = 1.0f;
		}
		return reflectance;
	}
}
