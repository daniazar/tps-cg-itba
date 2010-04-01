package raycaster;

import java.awt.Color;

import javax.vecmath.*;

public class Light {
	
	Point3f position;
	Color intensity;
	
	public Light(Point3f position, Color intensity)
	{
		this.position = position;
		this.intensity = intensity;
		
	}

}
