package org.cg.util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.cg.raycaster.Main;

public class Screen {


 public Screen() {
	    String title = "RayTracer";
	    final JFrame frame = new JFrame(title);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JPanel panel = new JPanel(new GridLayout(0, 1));
	    Border border = BorderFactory.createTitledBorder("Select options");
	    panel.setBorder(border);
	    final JCheckBox time = new JCheckBox("Time");
	    panel.add(time);
	    final JCheckBox progress = new JCheckBox("Progress");
	    panel.add(progress);
	    final JCheckBox show = new JCheckBox("Show image");
	    panel.add(show);
	    JButton button = new JButton("Submit");
	    Container contentPane = frame.getContentPane();
	    contentPane.add(panel, BorderLayout.NORTH);
	    JPanel pan = new JPanel(new GridLayout(0, 2));
	    JLabel label = new JLabel("Arhcivo de entrada:");
	    pan.add(label);
	    final JTextField input = new JTextField();
	    pan.add(input);
	    label = new JLabel("Arhcivo de salida:");
	    pan.add(label);
	    final JTextField output = new JTextField();
	    pan.add(output);
	    contentPane.add(pan, BorderLayout.CENTER);
	    MouseListener mouseListener = new MouseAdapter() {
	        public void mousePressed(MouseEvent mouseEvent) {
	          if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
	        	  String[] args = new String[5];
	        	  args[0] = "";
	        	  args[1] = "";
	        	  args[2] = "";
	        	  args[3] = "";
	        	  args[4] = "";
	             if(time.isSelected())
	            	 args[0] = "-time";
	             if(progress.isSelected())
	            	 args[1] = "-progress";
	             if(show.isSelected())
	            	 args[2] = "-show";
	             if (input.getText().length() != 0)
	             {
	             args[3] = "-i " + input.getText();
	             }
	             if (output.getText().length() != 0)
	             {
	            	 System.out.println("aaa" + output.getText() + "bbb");
	             args[4] = "-o " + output.getText();	             
	             }
	             frame.dispose();
	             try {
	            	 
					Main.main(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
					
	        	  
	            }
	        }

	      };
	      button.addMouseListener(mouseListener);
	    contentPane.add(button, BorderLayout.SOUTH);
	    frame.setSize(300, 200);
	    	      
	    frame.setVisible(true);
	    
	    
	  }	
}

