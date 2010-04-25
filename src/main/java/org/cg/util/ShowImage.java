package org.cg.util;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

public class ShowImage extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		//image object
		private Image img;
		
		public ShowImage(BufferedImage img)	 throws IOException
		{
			this.img = img;
			
		}
		
		//override paint method of panel
		public void paint(Graphics g)
		{
			//draw the image
			if( img != null)
				g.drawImage(img,0,0, this);
		}
		
	}
				

