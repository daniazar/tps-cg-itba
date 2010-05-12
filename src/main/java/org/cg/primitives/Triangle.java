package org.cg.primitives;

import java.awt.Color;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.boundingbox.SphereBoundingBox;
import org.cg.raycaster.ray.Ray;
import org.cg.rendering.Material;
import org.cg.rendering.shader.Shader;
import org.cg.rendering.shader.ShaderType;
import org.cg.rendering.shader.TextureManager;


public class Triangle extends Primitive {

	private Point3f middlePoint;
	private float maxDistanceFromMiddle;
	private Vector3f tp1;
	private Vector3f tp2;
	private Point3f p1;
	private Vector3f n;
	private Material material;
	private Point2f uv1;
	private Point2f uv2;
	private Point2f uv3;
	private Shader shader;
	
	public void setMaterial(Material material) {
		this.material = material;
	}

    float    uu, uv, vv, wu, wv, D;
    Plane p;
	
	public Triangle(Point3f pt1, Point3f pt2, Point3f pt3, Material material) {

	   // vector form triangle pt1 to pt2
		tp1 = new Vector3f();
		tp2 = new Vector3f();
		tp1.x = pt3.x - pt1.x;
		tp1.y = pt3.y - pt1.y;
		tp1.z = pt3.z - pt1.z;

	   // vector form triangle pt1 to pt3
		tp2.x = pt2.x - pt1.x;
		tp2.y = pt2.y - pt1.y;
		tp2.z = pt2.z - pt1.z;
		n = new Vector3f();
		n.cross(tp1, tp2);

		p1 =pt1;
		this.material = material;
		
		middlePoint = new Point3f();
		middlePoint.x = (pt1.x + pt2.x + pt3.x) / 3;
		middlePoint.y = (pt1.y + pt2.y + pt3.y) / 3;
		middlePoint.z = (pt1.z + pt2.z + pt3.z) / 3;
		
		maxDistanceFromMiddle = Math.max(Math.max(middlePoint.distance(pt1), middlePoint.distance(pt2)), middlePoint.distance(pt3));
		boundingBox = new SphereBoundingBox(this);
		
	    uu = tp1.dot(tp1);
	    uv = tp1.dot(tp2);
	    vv = tp2.dot(tp2);

	    D = uv * uv - uu * vv;
		p = new Plane(n, p1, material);

	}

	public Triangle(Point3f pt1, Point3f pt2, Point3f pt3, Material material, Shader shader) {

		this.setShader(shader);
		   // vector form triangle pt1 to pt2
			tp1 = new Vector3f();
			tp2 = new Vector3f();
			tp1.x = pt2.x - pt1.x;
			tp1.y = pt2.y - pt1.y;
			tp1.z = pt2.z - pt1.z;

		   // vector form triangle pt1 to pt3
			tp2.x = pt3.x - pt1.x;
			tp2.y = pt3.y - pt1.y;
			tp2.z = pt3.z - pt1.z;
			n = new Vector3f();
			n.cross(tp1, tp2);

			p1 =pt1;
			this.material = material;
			
			middlePoint = new Point3f();
			middlePoint.x = (pt1.x + pt2.x + pt3.x) / 3;
			middlePoint.y = (pt1.y + pt2.y + pt3.y) / 3;
			middlePoint.z = (pt1.z + pt2.z + pt3.z) / 3;
			
			maxDistanceFromMiddle = Math.max(Math.max(middlePoint.distance(pt1), middlePoint.distance(pt2)), middlePoint.distance(pt3));
			boundingBox = new SphereBoundingBox(this);
			
		    uu = tp1.dot(tp1);
		    uv = tp1.dot(tp2);
		    vv = tp2.dot(tp2);

		    D = uv * uv - uu * vv;
			p = new Plane(n, p1, material);

		}
	public Triangle(Point3f pt1, Point3f pt2, Point3f pt3, Vector3f n, Material material) {

		   // vector form triangle pt1 to pt2
			tp1 = new Vector3f();
			tp2 = new Vector3f();
			tp1.x = pt2.x - pt1.x;
			tp1.y = pt2.y - pt1.y;
			tp1.z = pt2.z - pt1.z;

		   // vector form triangle pt1 to pt3
			tp2.x = pt3.x - pt1.x;
			tp2.y = pt3.y - pt1.y;
			tp2.z = pt3.z - pt1.z;
			this.n=n;
			p1 =pt1;
			this.material = material;
			
			middlePoint = new Point3f();
			middlePoint.x = (pt1.x + pt2.x + pt3.x) / 3;
			middlePoint.y = (pt1.y + pt2.y + pt3.y) / 3;
			middlePoint.z = (pt1.z + pt2.z + pt3.z) / 3;
			
			maxDistanceFromMiddle = Math.max(Math.max(middlePoint.distance(pt1), middlePoint.distance(pt2)), middlePoint.distance(pt3));
			boundingBox = new SphereBoundingBox(this);
			
		    uu = tp1.dot(tp1);
		    uv = tp1.dot(tp2);
		    vv = tp2.dot(tp2);

		    D = uv * uv - uu * vv;
			p = new Plane(n, p1, material);

		}

	
	public Triangle(Point3f pt1, Point3f pt2, Point3f pt3, Material material2,
			Shader shader, Point2f uv1, Point2f uv2, Point2f uv3) {
		this.shader = shader;
		this.setUv1(uv1);
	 	this.setUv2(uv2);
		this.setUv3(uv3);
		
		// vector form triangle pt1 to pt2
		tp1 = new Vector3f();
		tp2 = new Vector3f();
		tp1.x = pt2.x - pt1.x;
		tp1.y = pt2.y - pt1.y;
		tp1.z = pt2.z - pt1.z;

	   // vector form triangle pt1 to pt3
		tp2.x = pt3.x - pt1.x;
		tp2.y = pt3.y - pt1.y;
		tp2.z = pt3.z - pt1.z;
		n = new Vector3f();
		n.cross(tp1, tp2);

		p1 =pt1;
		this.material = material2;
		
		middlePoint = new Point3f();
		middlePoint.x = (pt1.x + pt2.x + pt3.x) / 3;
		middlePoint.y = (pt1.y + pt2.y + pt3.y) / 3;
		middlePoint.z = (pt1.z + pt2.z + pt3.z) / 3;
		
		maxDistanceFromMiddle = Math.max(Math.max(middlePoint.distance(pt1), middlePoint.distance(pt2)), middlePoint.distance(pt3));
		boundingBox = new SphereBoundingBox(this);
		
	    uu = tp1.dot(tp1);
	    uv = tp1.dot(tp2);
	    vv = tp2.dot(tp2);

	    D = uv * uv - uu * vv;
		p = new Plane(n, p1, material);
	}


	@Override
	public boolean Intersects(Ray ray) {
		// Para optimizar hay que guardar el plano ya creado.

		//Para no copiar codigo de plane Agregue una funcion que me retorne t si intersecta
		Point3f i  = p.GetIntersectionPoint(ray);

		if( i == null)
			return false;

		//http://www.cs.princeton.edu/courses/archive/fall00/cs426/lectures/raycast/sld019.htm
		
		//P= a(V2+p1)+b(V3+p1)
		//http://softsurfer.com/Archive/algorithm_0105/algorithm_0105.htm
		
		  // is I inside the triangle?
	    Vector3f w= new Vector3f();
		w.x = i.x - p1.x;
		w.z = i.z - p1.z;
		w.y = i.y - p1.y;
		
	    wu = w.dot(tp1);
	    wv = w.dot(tp2);

	    // get and test parametric coords
	    float s, t;
	    s = (uv * wv - vv * wu) / D;
	    
	    
	    if (s < 0 || s >1)        // I is outside T
	    {    
			ray.missed();
			return false;
	    }
	    t = (uv * wu - uu * wv) / D;
	    if (t < 0 || (s + t) > 1)  // I is outside T
	    {    
			ray.missed();
			return false;
	    }

	    ray.hit(i, this);

	    return true;                      // I is in T		
		
	}

	@Override
	public Color getBaseColor() {
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
	public float getRefraction() {
		return material.getRefraction();
	}
	@Override
	public void setBaseColor(Color c) {
		this.material.setDiffuse(c);
		
	}

	@Override
	public Point3f getAnyPoint() {
		return p1;
	}

	public boolean equals(Primitive o)
	{
		if(!(o instanceof Triangle))
		{

			return false;
		}
		else
		{
			Triangle t = (Triangle)o;
			if(t.p1.equals(p1) )
				return true;
			else
				return false;
		}
	}
	
	@Override
	public float getDistanceToClosestPoint(Point3f origin) {
		return getAnyPoint().distance(origin);
	}

	@Override
	public String toString() {
		return "Triangle [material=" + material + ", n=" + n + ", p1=" + p1
				+ ", u=" + tp1 + ", v=" + tp2 + "]";
	}

	@Override
	public Point3f getMiddlePoint() {
		return middlePoint;
	}
	
	@Override
	public float getMaxDistanceFromMiddle() {
		return maxDistanceFromMiddle;
	}


	@Override
	public Material getMaterial() {
		// TODO Auto-generated method stub
		return material;
	}
	
	@Override
	public Color getTextureColor(Point3f intersectionPoint) {
		float u0;
		float v0;
		
		intersectionPoint.sub(p1);
		Vector3f intersectionVec = new Vector3f(intersectionPoint.x, intersectionPoint.y, intersectionPoint.z);
		float alpha = (float) (Math.abs(intersectionVec.dot(tp1)) / Math.pow(tp1.length(), 2));
		float beta = (float) (Math.abs(intersectionVec.dot(tp2)) / Math.pow(tp2.length(), 2));
		
		Point2f uv1uv2 = (Point2f) uv1.clone();
		Point2f uv1uv3 = (Point2f) uv3.clone();
		
		uv1uv2.sub(uv2);
		uv1uv3.sub(uv2);
		
		uv1uv2.scale(alpha);
		uv1uv3.scale(beta);
		
		Point2f uv0 = uv1uv2;
		uv0.add(uv1uv3);
		
		u0 = uv0.x;
		v0 = uv0.y;
		
		System.out.println(u0 + "," + v0);
		if(this.shader.getType() == ShaderType.PHONG) {
			Color color;
			if(shader.getTexturePath() != null) {
				color = TextureManager.get(shader.getTexturePath()).getUV(u0,v0);
			} else {
				color = getBaseColor();
			}
			return color;
		}
		return getBaseColor();
	}


	public void setUv1(Point2f uv1) {
		this.uv1 = uv1;
	}


	public Point2f getUv1() {
		return uv1;
	}


	public void setUv2(Point2f uv2) {
		this.uv2 = uv2;
	}


	public Point2f getUv2() {
		return uv2;
	}


	public void setUv3(Point2f uv3) {
		this.uv3 = uv3;
	}


	public Point2f getUv3() {
		return uv3;
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}

	public Shader getShader() {
		return shader;
	}
}
