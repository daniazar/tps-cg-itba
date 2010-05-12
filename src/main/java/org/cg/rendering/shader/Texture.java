package org.cg.rendering.shader;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {

	private BufferedImage bi;
	private int width;
	private int height;
	
	public Texture(String texturePath) {
		 try {
			 
			bi = ImageIO.read(new File(texturePath));
			width = bi.getWidth();
			height = bi.getHeight();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Color getUV(float u, float v) {
		int u0 = (int) (width * u);
		int v0 = (int) (height * v);

		try {
			return new Color(bi.getRGB(u0, v0));
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			System.out.println(width + "," + height);
			System.out.println("u " + u + " u0 " + u0);
			System.out.println("v " + v + " v0 " + v0);
			e.printStackTrace();
		}
		return new Color(0);
	}

}
