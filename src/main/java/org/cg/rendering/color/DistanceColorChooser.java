package org.cg.rendering.color;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.vecmath.Point3f;

import org.cg.primitives.Primitive;
import org.cg.raycaster.SunflowScene;
import org.cg.raycaster.ray.Ray;


//Calcula el color en base a la distancia a la camara.

//Agarra cualquier punto de la primitiva, no necesariamente el mas cercano

//Cuando cicla a traves de todos los colores, vuelve a empezar

public class DistanceColorChooser extends PlainColorChooser {

	ArrayList<Primitive> assignedPrimitives = new ArrayList<Primitive>();
	public Point3f camPos;
	Color[] colores = new Color[] { new Color(182, 86, 193), Color.BLUE,
			Color.green, Color.yellow, Color.orange, Color.red };

	public DistanceColorChooser(Point3f CameraPos, ColorVariator var) {
		super(var);
		camPos = CameraPos;
		assignedPrimitives = SunflowScene.objects;
		Collections.sort(assignedPrimitives, new DistanceComparator());

	}

	@Override
	public Color getColor(Ray ray) {

		int lejania = assignedPrimitives.indexOf(ray.getObject());

		int index = lejania % colores.length;
		//Aplico el variador de color
		float baseDistance = ray.getObject().getDistanceToClosestPoint(camPos);
		return variator.changeColor(baseDistance,  colores[index], ray.getIntersectionPoint().distance(camPos) );

	}

	public class DistanceComparator implements Comparator<Primitive> {

		float distance1;
		float distance2;

		public int compare(Primitive arg0, Primitive arg1) {
			distance1 = arg0.getAnyPoint().distance(camPos);
			distance2 = arg1.getAnyPoint().distance(camPos);
			return (int) (distance1 - distance2);
		}

	}

}
