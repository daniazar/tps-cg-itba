package raycaster;

import java.awt.Color;

public  abstract class PlainColorChooser {


	ColorVariator variator;
	
	public PlainColorChooser(ColorVariator var)
	{
		variator = var;
	}
	public abstract Color getColor(Ray ray);
}
