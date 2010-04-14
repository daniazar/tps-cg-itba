package raycaster;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class RandomColorChooser extends PlainColorChooser {

	public RandomColorChooser(ColorVariator var) {
		super(var);
		// TODO Auto-generated constructor stub
	}
	private Random r = new Random(System.currentTimeMillis());
	ArrayList<Object> assignedPrimitives = new ArrayList<Object>();
	public Color getColor(Ray ray) {
		
		if(assignedPrimitives.contains(ray.getObject()))
				return ray.getObject().getColor();
		else
		{	
			Color c = new Color(r.nextFloat(),r.nextFloat(),r.nextFloat());
			ray.getObject().setColor(c);
			assignedPrimitives.add(ray.getObject());
			return c;
		}
		

	}

}
