package org.cg.rendering.shader;

import org.cg.rendering.Material;

public class Shader {

	String name;
	String texturePath;
	Material material;
	ShaderType type;
	
	public ShaderType getType() {
		return type;
	}


	public void setType(ShaderType type) {
		this.type = type;
	}


	public Shader(String name){
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTexturePath() {
		return texturePath;
	}

	public void setTexturePath(String texturePath) {
		this.texturePath = texturePath;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	
}
