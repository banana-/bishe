/**
 * 
 */
package org.test;

import javax.swing.*;

import org.gui.ShowGraphic;
import org.swingconsole.SwingConsole;

import java.awt.event.*;
import java.awt.*;
/**
 * @author renyu
 *
 */
public class ListenerTest extends JFrame{

	/**
	 * 
	 */
	public ListenerTest() {
		JButton newWindow = new JButton("new");
		newWindow.addActionListener(new ButtonListener());
		add(newWindow);
	}
	
	
	
	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			SwingConsole.run(new PanelTest(), 1000, 700, JFrame.DISPOSE_ON_CLOSE);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingConsole.run(new ListenerTest(), 500, 300);
	}

}
