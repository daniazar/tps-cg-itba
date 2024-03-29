package org.cg.primitives;

import java.awt.Color;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.boundingbox.SphereBoundingBox;
import org.cg.raycaster.ray.Ray;
import org.cg.rendering.Material;
import org.cg.rendering.shader.Shader;
import org.cg.util.TransformUtil;

public class Box extends Primitive {

	private Point3f middlePoint;
	private float maxDistanceFromMiddle;
    private Quadrilateral q1, q2, q3, q4, q5, q6;
    private Shader shader;
    
    public Box(Point3f pt1, Point3f pt2, Material material, Shader shader) {
    	Point3f p1 = pt1;
    	Point3f p2 = new Point3f(pt1.x, pt1.y, pt2.z);
    	Point3f p3 = new Point3f(pt2.x, pt1.y, pt2.z);
    	Point3f p4 = new Point3f(pt2.x, pt1.y, pt1.z);
    	Point3f p5 = new Point3f(pt2.x, pt2.y, pt1.z);
    	Point3f p6 = new Point3f(pt1.x, pt2.y, pt1.z);
    	Point3f p7 = new Point3f(pt1.x, pt2.y, pt2.z);
    	Point3f p8 = pt2;
    	
    	this.setShader(shader);
		initBox(p1,p2,p3,p4,p5,p6,p7,p8, material,shader);
    
    }

	public Box(Point3f pt1, Point3f pt2, Material material, Shader boxShader,
			Matrix4f transform) {
		Point3f p1 = pt1;
    	Point3f p2 = new Point3f(pt1.x, pt1.y, pt2.z);
    	Point3f p3 = new Point3f(pt2.x, pt1.y, pt2.z);
    	Point3f p4 = new Point3f(pt2.x, pt1.y, pt1.z);
    	Point3f p5 = new Point3f(pt2.x, pt2.y, pt1.z);
    	Point3f p6 = new Point3f(pt1.x, pt2.y, pt1.z);
    	Point3f p7 = new Point3f(pt1.x, pt2.y, pt2.z);
    	Point3f p8 = pt2;
    	
    	this.setShader(shader);
    	
    	TransformUtil.transform(p1, transform);
    	TransformUtil.transform(p2, transform);
    	TransformUtil.transform(p3, transform);
    	TransformUtil.transform(p4, transform);
    	TransformUtil.transform(p5, transform);
    	TransformUtil.transform(p6, transform);
    	TransformUtil.transform(p7, transform);
    	TransformUtil.transform(p8, transform);
    	
		initBox(p1,p2,p3,p4,p5,p6,p7,p8, material, boxShader);
	}
	

	private void initBox(Point3f pt1, Point3f pt2, Point3f pt3, Point3f pt4,
			Point3f pt5, Point3f pt6, Point3f pt7, Point3f pt8,
			Material material, Shader shader) {
		q1 = new Quadrilateral(pt1, pt2, pt3, pt4, material, shader); // abajo
		q2 = new Quadrilateral(pt6, pt5, pt8, pt7, material, shader); // arriba
		q3 = new Quadrilateral(pt5, pt4, pt3, pt8, material, shader); // adelante
		q4 = new Quadrilateral(pt5, pt6, pt1, pt4, material, shader); // izq
		q5 = new Quadrilateral(pt7, pt8, pt3, pt2, material, shader); // derecha
		q6 = new Quadrilateral(pt6, pt7, pt2, pt1, material, shader); // atras

            middlePoint = new Point3f();
            middlePoint.x = (pt1.x + pt2.x +pt3.x + pt4.x + pt5.x + pt6.x) / 6;
            middlePoint.y = (pt1.y + pt2.y +pt3.y + pt4.y + pt5.y + pt6.y) / 6;
            middlePoint.z = (pt1.z + pt2.z +pt3.z + pt4.z + pt5.z + pt6.z) / 6;
            
            maxDistanceFromMiddle = Math.max(
            							Math.max(middlePoint.distance(pt1), 
            									Math.max(middlePoint.distance(pt2), middlePoint.distance(pt3))),
            							Math.max(middlePoint.distance(pt4), 
            									Math.max(middlePoint.distance(pt5), middlePoint.distance(pt6)))
            								);
            
            boundingBox = new SphereBoundingBox(this);
		
	}

    
    /* (non-Javadoc)
     * @see org.cg.primitives.Primitive#Intersects(org.cg.raycaster.ray.Ray)
     */
    @Override
    public boolean Intersects(Ray ray) {
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

            */
            
            return true;
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
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
            return "Box [q1=" + q1 + ", q2=" + q2 + ", q3=" + q3 + ", q4=" + q4
                            + ", q5=" + q5 + ", q6=" + q6 + "]";
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



	@Override
	public float getMaxDistanceFromMiddle() {
		return maxDistanceFromMiddle;
	}

	@Override
	public float getRefraction() {
		return q1.getRefraction();
	}


	@Override
	public Point3f getMiddlePoint() {
		return middlePoint;
	}



	@Override
	public Material getMaterial() {
		return q1.getMaterial();
	}

	@Override
	public Color getTextureColor(Point3f intersectionPoint, Ray ray) {
		Quadrilateral q = q6;
		Point3f inter = new Point3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		
		if(q1.Intersects(ray)) {
			if(ray.position.distance(inter) < ray.position.distance(ray.intersectionPoint)) {
				q = q1;
				inter = ray.intersectionPoint;
			}
		} else if(q2.Intersects(ray)) {
			if(ray.position.distance(inter) < ray.position.distance(ray.intersectionPoint)) {
				q = q2;
				inter = ray.intersectionPoint;
			}
		} else if(q3.Intersects(ray)) {
			if(ray.position.distance(inter) < ray.position.distance(ray.intersectionPoint)) {
				q = q3;
				inter = ray.intersectionPoint;
			}
		} else if(q4.Intersects(ray)) {
			if(ray.position.distance(inter) < ray.position.distance(ray.intersectionPoint)) {
				q = q4;
				inter = ray.intersectionPoint;
			}
		} else if(q5.Intersects(ray)) {
			if(ray.position.distance(inter) < ray.position.distance(ray.intersectionPoint)) {
				q = q5;
				inter = ray.intersectionPoint;
			}
		} else {
			q = q6;
			inter = ray.intersectionPoint;
		}
		
		return q.getTextureColor(intersectionPoint, ray);
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}

	public Shader getShader() {
		return shader;
	}

}