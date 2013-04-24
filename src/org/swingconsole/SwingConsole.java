//:org/gui/SwingConsole.java
//Tool for running Swing demos from the console, 
//both applets and JFrames

package org.swingconsole;
import javax.swing.*;

public class SwingConsole{
	public static void run(final JFrame f, final int width, final int height){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				try{
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				}catch(Exception e){}
				
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setSize(width,height);
				f.setVisible(true);
			}
			
		});
	}
	
	public static void run(final JFrame f, final int width, final int height, final int type){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				try{
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				}catch(Exception e){}
				
				f.setDefaultCloseOperation(type);
				f.setSize(width,height);
				f.setVisible(true);
			}
			
		});
	}
}
