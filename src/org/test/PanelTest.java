package org.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.border.*;

import org.swingconsole.*;

public class PanelTest extends JFrame{
	JPanel panel;
	public PanelTest() {
		// TODO Auto-generated constructor stub
		setLayout(new BorderLayout());
		JButton up = new JButton("remove");
	    panel = new JPanel();
		panel.add(new JButton("hello"));
		add(up, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		up.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				panel.removeAll();
				
				
				panel.add(new JButton("nihao"));
				//panel.invalidate();
				panel.validate();
				
			}
		});
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				dispose();
			}
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingConsole.run(new PanelTest(), 1000, 700);
	}

}
