package org.cg.spatial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.cg.boundingbox.BoundingBox;
import org.cg.primitives.Primitive;
import org.cg.raycaster.SunflowScene;
import org.cg.raycaster.ray.Ray;

public class Octree {

	public Octree children[] = new Octree[8];
	public static int PRIM_THRESHOLD = Math.max(128,(int)Math.pow(SunflowScene.objects.size(),0.333333));
	//public static int PRIM_THRESHOLD = Math.max(3,(int)Math.pow(SunflowScene.objects.size(),0.333333));
	ArrayList<Primitive> myPrimitives= new ArrayList<Primitive>();


	float maxZ;
	float minZ;
	float maxY;
	float minY;
	float maxX;
	float minX;
	float lengthX; 
	float lengthY;
	float lengthZ;

	//Constructor especial para la raiz, hace una pasada por todas las primitivas para
	//determinar el espacio
	public Octree(ArrayList<Primitive> primitives) {

		System.out.println("OCTREE: Cantidad de primitivas:"+primitives.size());
		System.out.println("Threshold:"+PRIM_THRESHOLD);
		minX = 9999999;
		maxX = -9999999;
		maxY = maxX;
		minY = minX;
		maxZ = maxY;
		minZ = minY;

		for (Primitive p : primitives) {
			BoundingBox b = p.getBoundingBox();
			if (minX > b.MinX())
				minX = b.MinX();
			if (minY > b.MinY())
				minY = b.MinY();
			if (minZ > b.MinZ())
				minZ = b.MinZ();
			if (maxX < b.MaxX())
				maxX = b.MaxX();
			if (maxY < b.MaxY())
				maxY = b.MaxY();
			if (maxZ < b.MaxZ())
				maxZ = b.MaxZ();
		}

		lengthX = maxX - minX;
		lengthY = maxY - minY;
		lengthZ = maxZ - minZ;

		//Si tengo demasiadas primitivas me parto en 8
		if(primitives.size() > PRIM_THRESHOLD)	
		{
			for (int i = 0; i< 8; i++) 
				children[i] = getOctreePerOctant(i, minX, maxX, minY, maxY, minZ, maxZ, primitives);
			myPrimitives.clear();
		}
		else
			myPrimitives.addAll(primitives);
	}
	
	

	//Constructor recursivo, ve cuales de las primitivas dadas le pertenecen, si son muchas se parte
	public Octree(float minX, float maxX, float minY, float maxY, float minZ,
			float maxZ, ArrayList<Primitive> primitives) {

		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.minZ = minZ;
		lengthX = maxX - minX;
		lengthY = maxY - minY;
		lengthZ = maxZ - minZ;
		
		 myPrimitives = new ArrayList<Primitive>();
		 for(Primitive p : primitives)
		 {
			 BoundingBox b = p.getBoundingBox();
			if(b.MaxX() <= minX)
				continue;
			if(b.MinX() >= maxX)
				continue;
			if(b.MaxY() <= minY)
				continue;
			if(b.MinY() >= maxY)
				continue;
			if(b.MaxZ() <= minZ)
				continue;
			if(b.MinZ() >= maxZ)
				continue;
			myPrimitives.add(p);
		 }
		 if(myPrimitives.size()>PRIM_THRESHOLD)
		 {
			 for(int i = 0; i< 8; i++) 
			 {
				 children[i] = getOctreePerOctant(i, minX, maxX, minY, maxY, minZ, maxZ, myPrimitives);
			 }
			 myPrimitives.clear();
		 }

	}
	
	public Set<Primitive> GetPrimitivesFromIntersectingOctants(Ray ray)
	{
		Set<Primitive> answer = new HashSet<Primitive>();
		
		
		if(myPrimitives.size() != 0)
		{
			answer.addAll(myPrimitives);
			return answer;
		}
		
		for(Octree octant : children)
		{
			if(octant != null)
			{
				if(RayIntersectsOctant(ray, octant))
				{
					answer.addAll(octant.GetPrimitivesFromIntersectingOctants(ray));
				}
			}
		}
		return answer;
	}

	

	public ArrayList<Primitive> getPrimitives() {
		return myPrimitives;
	}
	
	//Metodo que devuelve un octree con dimensiones del octante pedido en base a dimensiones del padre
	//tambien incluye las primitivas pero usa el constructor 2
	public static Octree getOctreePerOctant(int octant, float minX, float maxX, float minY, float maxY
			,float minZ, float maxZ, ArrayList<Primitive> primitives)
	{
		float lengthX = maxX - minX;
		float lengthY = maxY - minY;
		float lengthZ = maxZ - minZ;
		
		switch(octant)
		{
			case 0: return new Octree(minX + lengthX / 2, maxX, minY, minY
						+ lengthY / 2, minZ + lengthZ / 2, maxZ, primitives);
		
			case 1: return  new Octree(minX, minX + lengthX / 2, minY, minY
						+ lengthY / 2, minZ + lengthZ / 2, maxZ, primitives);
		
			case 2: return new Octree(minX, minX + lengthX / 2,
						minY + lengthY / 2, maxY, minZ + lengthZ / 2, maxZ,
						primitives);
		
			case 3: return  new Octree(minX + lengthX / 2, maxX,
						minY + lengthY / 2, maxY, minZ + lengthZ / 2, maxZ,
						primitives);
				
			case 4: return  new Octree(minX + lengthX / 2, maxX, minY, minY
						+ lengthY / 2, minZ, minZ + lengthZ / 2, primitives);
		
			case 5: return  new Octree(minX, minX + lengthX / 2, minY, minY
						+ lengthY / 2, minZ, minZ + lengthZ / 2, primitives);
			
			case 6: return  new Octree(minX, minX + lengthX / 2,
						minY + lengthY / 2, maxY, minZ, minZ + lengthZ / 2,
						primitives);
		
			case 7: return  new Octree(minX + lengthX / 2, maxX,
						minY + lengthY / 2, maxY, minZ, minZ + lengthZ / 2,
						primitives);
		}
		return null;
	}


	
	/*Descubre la interseccion proyectando la recta parametricamente
	 * 
	 * x = x0 + dxt
	 * y = y0 + dyt
	 * z = z0 + dzt
	 * 
	 * con esto veo en que rangos de t interseco con el octante
	 * si, estos se solapan, hay interseccion
	 * 
	 * Ejemplo:
	 * Recta = (0,0,0) + t(1,1,1) 
	 * 
	 * Box = [3,4][3,4][1,2]
	 * 
	 * hallo para x los t que estan en [3,4]
	 * tx = [3,4]
	 * ty = [3,4]
	 * tz = [1,2]
	 * Como no existe t que cumpla con tx, ty, tz, no interseca.
	 * 
	 * Seguro hay una mejor manera, esta es la que se me ocurrio
	 */
	public static boolean RayIntersectsOctant(Ray ray, Octree octant)
	{
		float tx0, ty0, tz0, tx1, ty1,tz1;
		float x0,y0,z0,dx,dy,dz;
		x0 = ray.position.x;
		y0 = ray.position.y;
		z0 = ray.position.z;
		
		dx = ray.direction.x;
		dy = ray.direction.y;
		dz = ray.direction.z;
		
		//x = x0 + dxt -> t = (x - x0)/dx 
		tx0 = (octant.minX - x0)/dx;
		tx1 = (octant.maxX - x0)/dx;
		ty0 = (octant.minY - y0)/dy;
		ty1 = (octant.maxY - y0)/dy;
		tz0 = (octant.minZ - z0)/dz;
		tz1 = (octant.maxZ - z0)/dz;
		
		float auxi_t;
		
		if(tx0 > tx1)
		{
			auxi_t = tx0;
			tx0 = tx1;
			tx1 = auxi_t;
		}
		if(ty0 > ty1)
		{
			auxi_t = ty0;
			ty0 = ty1;
			ty1 = auxi_t;
		}
		if(tz0 > tz1)
		{
			auxi_t = tz0;
			tz0 = tz1;
			tz1 = auxi_t;
		}
		
		//Solo existira un t cuando no suceda que dos estan disjuntos
		//disjuntos significa que t1 < t0
		return !(tx1 < ty0 || ty1 < tz0 || tz1 < tx0 || tz1 < ty0 || tx1 < tz0 || ty1 < tx0);
		
	}
	
	public String toString()
	{
		String string = "X["+minX+","+maxX+"] Y["+minY+","+maxY+"] Z["+minZ+","+maxZ+"]";
		string += "myPrimitives = "+myPrimitives.size()+"\n";
		
		if(myPrimitives.size() == 0)
		{
			string += "{";
			for(int i = 0; i< 8; i++)
				if(children[i] != null)
				string += "child "+i+":"+children[i]+"\n";
		string += "}";
		}
		else
		{
			for(Primitive p : myPrimitives)
				string += (p.getMiddlePoint())+"\n";
		}
		return string;

	}
}
