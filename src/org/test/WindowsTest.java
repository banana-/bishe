package org.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class WindowsTest {

	public WindowsTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("swing test");
        frame.setSize(300, 200);
        frame.setLayout(null);
        JButton btnNewFrame = new JButton("新窗口");
        btnNewFrame.setBounds(30, 30, 80, 40);
        frame.getContentPane().add(btnNewFrame);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭时，直接退出
        btnNewFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO 自动生成
                final JFrame frame = new JFrame("new frame");
                frame.setSize(200, 180);
                frame.getContentPane().add(new JLabel("this is a new frame"));
                frame.setVisible(true);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        // TODO 自动生成
                        frame.dispose();//dispose会隐藏窗体并释放窗体所占用的部分资源
                    }
                });
            }
        });
    }

	

}
