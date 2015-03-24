package com.mlab.roadplayer.swingtest;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.mlab.roadplayer.util.RPUtil;

public class TestSwing {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TestSwing();
			}
		});
	}

	TestSwing() {
		JFrame frame = new JFrame("Pruebas");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(600,400));
		frame.setLocation(300, 300);
		
		MyAction action = new MyAction("Botón", RPUtil.createImageIcon("close.png", 35, 35),
				"Botón de pruebas");
		JButton btn = new JButton(action);
		frame.getContentPane().add(btn);
		
		frame.pack();
		frame.setVisible(true);
	}
	class MyAction extends AbstractAction {
		
		public MyAction(String text, ImageIcon icon, String desc) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("MyAction.actionPerformed()");
		}
		
	}
	
}
