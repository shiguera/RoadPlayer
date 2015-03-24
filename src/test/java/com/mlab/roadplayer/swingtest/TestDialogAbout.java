package com.mlab.roadplayer.swingtest;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.mlab.roadplayer.swing.dialogs.AboutDialog;

public class TestDialogAbout {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TestDialogAbout();
			}
		});
	}
	
	private TestDialogAbout() {
		JFrame frame = new JFrame("TestDialogAbout");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(600, 400));
		
		AboutDialog d = new AboutDialog(frame);
		d.setVisible(true);
		
		frame.pack();
		frame.setVisible(true);
	}
}
