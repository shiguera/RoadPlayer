package com.mlab.roadplayer.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mlab.patterns.Observable;
import com.mlab.roadplayer.command.UpdateCommand;


public class TitleValueLabel extends SwingView {

	JPanel mainPanel;
	JLabel titleLabel;
	JLabel valueLabel;
	UpdateCommand updateCommand;
	
	public TitleValueLabel(String lbltitle, Observable model, UpdateCommand command) {
		super(model); 
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		titleLabel = new JLabel();
		titleLabel.setForeground(Color.DARK_GRAY);
		setTitle(lbltitle);
		mainPanel.add(titleLabel);
		
		valueLabel = new JLabel();
//		valueLabel.setBorder(BorderFactory.createCompoundBorder(
//				BorderFactory.createEtchedBorder(),
//				BorderFactory.createEmptyBorder(2, 5, 2, 5)));
		valueLabel.setForeground(Color.GRAY);
		setValue(" - ");
		mainPanel.add(valueLabel);

		updateCommand = command;
	}
	public String getTitle() {
		return titleLabel.getText();
	}

	public String getValue() {
		return valueLabel.getText();
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	public void setValue(String value) {
		if(value=="") {
			valueLabel.setText(" - ");			
		} else {
			valueLabel.setText(value);			
		}
	}
	@Override
	public void update() {
		//System.out.println("TitleValueLabel..update()");
		setValue(updateCommand.getValue());
	}
	@Override
	public JPanel getPanel() {
		return mainPanel;
	}
}
