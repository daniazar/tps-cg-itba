package raycaster;

import java.awt.Color;

public interface LightColorChooser {
	
	public Color getColor(Ray ray, float coef,  Color c);

}
