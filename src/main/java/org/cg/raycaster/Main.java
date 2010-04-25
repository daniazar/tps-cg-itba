package org.cg.raycaster;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.cg.raycaster.ray.Raycaster;
import org.cg.rendering.Camera;
import org.cg.util.Parameters;
import org.cg.util.RenderTimer;
import org.cg.util.ShowImage;

public class Main {
	
	public static void main(String[] args) throws Exception
	{
		RenderTimer timer = new RenderTimer();

		Parameters p = Parameters.getInstance(args);
		p.ParseParameters();
		
		Scene.startScene(p.getInputFile());
		Camera c = Scene.cam;
		Raycaster raycaster = new Raycaster();
		
		c.setColorMode(p.getCm(), p.getCv());
		c.setImageDim(p.getSize().x, p.getSize().y);
		c.setImageFov((float)p.getFov());
		raycaster.setCamera(c);
		
		BufferedImage im = raycaster.raycast(p.isProgress());
		
		String extension = "";
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
		
		if (p.isShow()) {
			
			try
			{
				//create frame
				JFrame f = new JFrame();
				f.setTitle(p.getOutputFile());
				f.setSize(im.getWidth(), im.getHeight());
				//create panel with selected file
				ShowImage panel = new ShowImage( im );
				//add panel to pane
				f.getContentPane().add(panel);
			    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				//show frame
				f.setVisible(true);
			}
			catch(Exception e)
			{
				System.out.println ( "Bad image file");	
			}		
	
		}
	}

}
