package com.mlab.roadplayer.swing;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class StatusBarLabel implements SwingPanel {
	
	private final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	protected JPanel mainPanel;
	protected JLabel label;

	
	public StatusBarLabel(String text, ImageIcon icon) {
		this.label = new JLabel(text);
		this.label.setFont(FONT);
		this.label.setIcon(icon);		
		setLayout();
	}
	
	abstract protected void setLayout();
	
	public void setIcon(ImageIcon icon) {
		this.label.setIcon(icon);
	}

	public void setText(String text) {
		this.label.setText(text);
	}

	@Override
	public JPanel getPanel() {
		return mainPanel;
	}

	public void setSize(int width, int height) {
		this.mainPanel.setPreferredSize(new Dimension(width, height));
	}


}
