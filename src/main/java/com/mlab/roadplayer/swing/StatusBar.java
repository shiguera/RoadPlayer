package com.mlab.roadplayer.swing;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.mlab.roadplayer.MainController;
import com.mlab.roadplayer.util.RPUtil;

public class StatusBar  extends SwingView {
	private final String INFO_ICON = "info16.png";
	private final String WARNING_ICON = "warning16.png";
	private final String ERROR_ICON = "error16.png";
	
	protected JPanel statusPanel;
	protected StatusBarLabelInfo labelInfo;
	
	MainController controller;
	
	// Constructor
	public StatusBar(MainController controller) {
		super(controller.getModel());

		statusPanel = new JPanel();
		statusPanel.setPreferredSize(new Dimension(600,30));
		statusPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createBevelBorder(BevelBorder.LOWERED),
			BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

		statusPanel.add(Box.createHorizontalStrut(10));
		
		statusPanel.add(Box.createHorizontalStrut(10));
		labelInfo = new StatusBarLabelInfo("RoadPlayer status", null);
		statusPanel.add(labelInfo.getPanel());
		
		statusPanel.add(Box.createHorizontalGlue());
		statusPanel.add(Box.createHorizontalStrut(10));
		
		
		//statusPanel.add(new StatusBarLabelSRS(trackMap.getMapController().getMapModel(),"SRS:",null).getPanel());
		statusPanel.add(Box.createHorizontalStrut(10));
		
		//statusPanel.add(new JGeoCoordsStatusBarItem(trackMap.getMapController().getMapModel(), 
		//		trackMap.getMapController().getView().getJMapPane();
		
		statusPanel.add(Box.createHorizontalStrut(10));
		
		statusPanel.add(Box.createHorizontalGlue());

	}

	public void showMessage(int messageLevel, String title, String message) {
		String msg = title + ": " + message;
		switch(messageLevel) {
		case MessageLevel.MESSAGE_LEVEL_ERROR:
			showError(msg);
			break;
		case MessageLevel.MESSAGE_LEVEL_WARNING:
			this.showWarning(msg);
			break;
		default:
			this.showInfo(msg);
		}
	}
	public void showInfo(String info) {
		this.labelInfo.setIcon(createIcon(INFO_ICON));
		this.labelInfo.setText(info);
	}
	public void showWarning(String info) {
		this.labelInfo.setIcon(createIcon(WARNING_ICON));
		this.labelInfo.setText(info);
	}
	public void showError(String info) {
		this.labelInfo.setIcon(createIcon(ERROR_ICON));
		this.labelInfo.setText(info);
	}

	public ImageIcon createIcon(String name) {
		return RPUtil.createImageIcon(name, 16, 16);
	}
	

	public JPanel getStatusPanel() {
		return statusPanel;
	}
	@Override
	public JPanel getPanel() {
		return statusPanel;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	

}
