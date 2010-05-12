package org.cg.util;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector4f;

public class TransformUtil {
	
	public static void transform(Point3f p,Matrix4f m){
		Vector4f v = new Vector4f(p.x, p.y, p.z, 1);
		m.transform(v);
		p.x = v.x;
		p.y = v.y;
		p.z = v.z;
	}

}
