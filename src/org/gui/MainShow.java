/**
*图像化显示的主界面
*@author jiaorenyu
*Create on 2013-03-24 10:03
*/
package org.gui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.swingconsole.*;
/**
*图形化主类
*/
public class MainShow extends JFrame{
	String sourceCode = null;
	/**
	*布置主界面框架
	*@throws 无
	*@return void
	*/
	public MainShow(){
		add(codeInput());	
	}
	/**
	*程序运行开始时的界面，用来粘贴将要被解析的代码
	*@return JPanel 返回粘贴代码的面板，以被添加到框架中。
	*/
	private JPanel codeInput(){
		final JPanel panel = new JPanel();
		FlowLayout layout = new FlowLayout();
		panel.setLayout(layout);
		final JButton analysis = new JButton("解析");
		final JTextArea code = new JTextArea(35, 90);
		code.setText("在这里输入要解析的代码");
		//给"解析"按钮添加监听器
		analysis.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				sourceCode = code.getText();
				//点击"解析"按钮后，删除面板上的所有组建
				panel.removeAll();
				//在面板上添加新的组建，设置新的布局，并重画
				panel.setLayout(new BorderLayout());
				panel.add(UpButton(), BorderLayout.NORTH);
				panel.add(centerShow(), BorderLayout.CENTER);
				panel.add(downPrompt(), BorderLayout.SOUTH);
				panel.validate();
				panel.repaint();
			}
		});
		panel.add(analysis);
		panel.add(new JScrollPane(code));
		return panel;
	}

    private JPanel logicalShow(){
		JButton button = new JButton("hello");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JTextArea text = new JTextArea(35,90);
		text.setText(sourceCode);
		panel.add(button);
		panel.add(text);
		return panel;
	}
	/**
	*各种控制按钮
	*@return JPanel 返回包含控制按钮的面板
	*/
	private JPanel UpButton(){
		JButton nextStep = null, reStart = null, reInput = null;
		nextStep = new JButton("下一步");
		reStart = new JButton("重新开始");
		reInput = new JButton("重新选择");
		JPanel upper = new JPanel();
		upper.setLayout(new FlowLayout());
		upper.add(nextStep);
		upper.add(reStart);
		upper.add(reInput);
		return upper;
	}

	private JPanel centerShow(){
		JPanel center = new JPanel();
		JButton b = new JButton("center");
		center.add(b);
		return center;
	}

	private JPanel downPrompt(){
		JTextField prompt = new JTextField(50);
		JPanel down = new JPanel();
		down.add(prompt);
		return down;
	}
	public static void main(String[] args){
		SwingConsole.run(new MainShow(), 1000, 700);	
	}
}	


