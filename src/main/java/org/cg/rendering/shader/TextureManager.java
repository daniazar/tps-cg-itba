package org.cg.rendering.shader;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {

	static Map<String, Texture> texturesMap = new HashMap<String, Texture>();
	
	public static Texture get(String texturePath) {
		Texture tex = texturesMap.get(texturePath);
		if(tex == null) {
			tex = new Texture(texturePath);
			texturesMap.put(texturePath, tex);
		}
		return tex;
	}

}
