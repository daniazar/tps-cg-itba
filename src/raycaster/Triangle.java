package raycaster;

import java.awt.Color;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class Triangle extends Object {

	
	private Vector3f u;
	private Vector3f v;
	private Point3f p1;
	private Vector3f n;
	private Material material;

	public Triangle(Point3f pt1, Point3f pt2, Point3f pt3, Material material) {
		super();

	   // vector form triangle pt1 to pt2
		u = new Vector3f();
		v = new Vector3f();
		u.x = pt2.x - pt1.x;
		u.y = pt2.y - pt1.y;
		u.z = pt2.z - pt1.z;

	   // vector form triangle pt1 to pt3
		v.x = pt3.x - pt1.x;
		v.y = pt3.y - pt1.y;
		v.z = pt3.z - pt1.z;
		n = new Vector3f();
		n.cross(u, v);

		p1 =pt1;
		this.material = material;
	}

	
	@Override
	public void Intersects(Ray ray) {
		// Para optimizar hay que guardar el plano ya creado.
		Plane p = new Plane(n, p1, material);

		//Para no copiar codigo de plane Agregue una funcion que me retorne t si intersecta
		Point3f i  = p.GetIntersectionPoint(ray);
		if( i == null)
			return;
		
		//http://www.cs.princeton.edu/courses/archive/fall00/cs426/lectures/raycast/sld019.htm
		
		//P= a(V2+p1)+b(V3+p1)
		//http://softsurfer.com/Archive/algorithm_0105/algorithm_0105.htm
		
		  // is I inside the triangle?
	    float    uu, uv, vv, wu, wv, D;
	    Vector3f w= new Vector3f();
		w.x = i.x - p1.x;
		w.z = i.z - p1.z;
		w.y = i.y - p1.y;
		
	    uu = u.dot(u);
	    uv = u.dot(v);
	    vv = v.dot(v);
	    wu = w.dot(u);
	    wv = w.dot(v);
	    D = uv * uv - uu * vv;

	    // get and test parametric coords
	    float s, t;
	    s = (uv * wv - vv * wu) / D;
	    if (s < 0.0 || s > 1.0)        // I is outside T
	    {    
			ray.missed();
			return;
	    }
	    t = (uv * wu - uu * wv) / D;
	    if (t < 0.0 || (s + t) > 1.0)  // I is outside T
	    {    
			ray.missed();
			return;
	    }

	    ray.hit(i, this);

	    return ;                      // I is in T		
		
	}

	@Override
	public Color getColor() {
		return material.diffuse;
	}

	@Override
	public Vector3f getNormal(Point3f point) {
		return n;
	}

	@Override
	public float getReflection() {
		return material.reflection;
	}

}
