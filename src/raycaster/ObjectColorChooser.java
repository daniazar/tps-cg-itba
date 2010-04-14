package raycaster;

import java.awt.Color;

public class ObjectColorChooser extends PlainColorChooser {


	public ObjectColorChooser(ColorVariator var) {
		super(var);
		// TODO Auto-generated constructor stub
	}

	public
	Color getColor(Ray ray) {
		Object o = ray.getObject();
		Color ans = null;
		if(o != null )
		{
			ans = o.getColor();
		}
		
		return ans;

	}

}
