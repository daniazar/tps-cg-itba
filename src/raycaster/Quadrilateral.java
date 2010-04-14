package raycaster;

import java.awt.Color;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class Quadrilateral extends Object {

	private Triangle t1, t2;
	
	public Quadrilateral(Point3f pt1, Point3f pt2, Point3f pt3, Point3f pt4, Material material) {
		super();

	t1 = new Triangle(pt1, pt2, pt4, material);
	t2 = new Triangle(pt4, pt2, pt3, material);

	}

	
	@Override
	public void Intersects(Ray ray) {
		t1.Intersects(ray);
		t2.Intersects(ray);	
		if(!ray.hit)
			return;
		else if(ray.getObject().equals(t1))
		{
			
				ray.hit(ray.intersectionPoint, this);
		}
		else if(ray.getObject().equals(t2))
		{
				ray.hit(ray.intersectionPoint,this);
		}
		else
		{
			ray.missed();
		}

		return;
	}

	@Override
	public Color getColor() {
		return t1.getColor();
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
	public void setColor(Color c) {
		t1.setColor(c);
		t2.setColor(c);
		
	}


	@Override
	public Point3f getAnyPoint() {
		// TODO Auto-generated method stub
		return t1.getAnyPoint();
	}
	
	public boolean equals(Object o)
	{
	

		if(o instanceof Quadrilateral)
		{
			Quadrilateral q = (Quadrilateral)o;
			if(q.t1.equals(t1) && q.t2.equals(t2)||(
					q.t1.equals(t2)||q.t2.equals(t1)))
				return true;
			else
				return false;
		}
		else
			return false;
		
	}
	
	public String toString()
	{
		return "Quad";
	}


	@Override
	public Point3f getClosestPoint(Point3f point) {
		return getAnyPoint();
	}
}
