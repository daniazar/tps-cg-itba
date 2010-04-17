package org.cg.raycaster;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.cg.raycaster.ray.Raycaster;
import org.cg.rendering.Camera;
import org.cg.util.RenderTimer;

public class Main {
	
	public static void main(String[] args) throws Exception
	{
		RenderTimer timer = new RenderTimer();

		Parameters p = Parameters.getInstance(args);
		p.ParseParameters();
		
		Scene.startScene(p.getI());
		Camera c = Scene.cam;
		Raycaster raycaster = new Raycaster();
		
		c.setColorMode(p.getCm(), p.getCv());
		c.setImageDim(p.getSize().x, p.getSize().y);
		c.setImageFov((float)p.getFov());
		raycaster.setCamera(c);
		
		BufferedImage im = raycaster.raycast();
		
		String extension;
		File render = new File(p.getOutputFile());
		try {
			if(p.getOutputFile().contains(".bmp"))
				extension = "bmp";
			else
				extension = "png";
			
			ImageIO.write(im, extension, render);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(p.time)
			System.out.println("Render took " + timer.getTime() + "ms");
	}

}
