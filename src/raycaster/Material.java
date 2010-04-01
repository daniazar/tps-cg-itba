package raycaster;

import java.awt.Color;

public class Material {
	
	Color diffuse;
	float reflection;
	
	public Material(Color diffuse, float reflection)
	{
		this.diffuse = diffuse;
		this.reflection =  reflection;
	}

}
