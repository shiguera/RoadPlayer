package com.mlab.roadplayer.swing;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class StatusBarLabelInfo extends StatusBarLabel {

	
	public StatusBarLabelInfo(String text, ImageIcon icon) {
		super(text, icon);
	}

	@Override
	protected void setLayout() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.add(label);
			
	}


}
