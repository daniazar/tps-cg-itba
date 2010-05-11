package org.cg.spatial;

import java.util.ArrayList;
import java.util.HashSet;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.primitives.Primitive;
import org.cg.primitives.Sphere;
import org.cg.raycaster.ray.Ray;

public class OctreeTest {

	public static void main(String[] args)
	{
		ArrayList<Primitive> prims = new ArrayList<Primitive>();
		prims.add(new Sphere(new Point3f(10,0,0), 1, null));
		prims.add(new Sphere(new Point3f(-10,0,0), 1, null));
		prims.add(new Sphere(new Point3f(-10,10,0), 1, null));
		prims.add(new Sphere(new Point3f(-10,10,-10), 1, null));
		prims.add(new Sphere(new Point3f(-8,8,-8), 1, null));
		prims.add(new Sphere(new Point3f(-9,9,-9), 1, null));
		prims.add(new Sphere(new Point3f(-3,6,-6), 1, null));
		
		
		Octree root = new Octree(prims);
		System.out.println(root);
		Ray ray = new Ray(new Vector3f(1,1,0), new Point3f(-11,11,-11));
		HashSet<Primitive> p = (HashSet<Primitive>) root.GetPrimitivesFromIntersectingOctants(ray);
	
	System.out.println("asdf");
		System.out.println(p);
	}
}
