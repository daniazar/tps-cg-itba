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
		// Para optimizar hacer que le pregunte al primero si lo toca y no invocar al segundo. 

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
}
