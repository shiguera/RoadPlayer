package com.mlab.roadplayer.swing;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.mlab.map.TrackMapModel;
import com.mlab.patterns.AbstractCompositeObserver;
import com.mlab.roadplayer.MainController;
import com.mlab.roadplayer.command.GetAccumulateDistanceCommand;
import com.mlab.roadplayer.command.GetMobileAltitudeCommand;
import com.mlab.roadplayer.command.GetMobileBearingCommand;
import com.mlab.roadplayer.command.GetMobileLatitudeCommand;
import com.mlab.roadplayer.command.GetVideoPositionCommand;
import com.mlab.roadplayer.command.NewGetMobileAccuracyCommand;
import com.mlab.roadplayer.command.NewGetMobileLongitudeCommand;
import com.mlab.roadplayer.command.NewGetMobileSpeedKmHCommand;
import com.mlab.roadplayer.command.NewGetMobileSpeedMetersCommand;
import com.mlab.roadplayer.command.NewGetMobileXCommand;
import com.mlab.roadplayer.command.NewGetMobileYCommand;
import com.mlab.roadplayer.video.VideoModel;

public class InfoPanel extends AbstractCompositeObserver {

	private final String VIDEOPOSITION_TITLE = "T: ";
	private final String POSLAT_TITLE = "Latitud:";
	private final String POSLON_TITLE = "Longitud:";
	private final String ALTITUDE_TITLE = "Altitud:";
	private final String SPEED_TITLE = "m/s:";
	private final String SPEEDKMH_TITLE = "Km/h:";
	private final String BEARING_TITLE = "Rumbo:";

	private final String ACCDISTANCE_TITLE = "Dist (m)";
	private final String POSX_TITLE = "X:";
	private final String POSY_TITLE = "Y:";
	private final String ACCURACY_TITLE = "Precisión (m):";
	


	TitleValueLabel lblPosLat, lblPosLon, lblAltitude, lblSpeed, lblSpeedKmh;
	TitleValueLabel lblPosX, lblPosY, lblBearing, lblAccuracy, labelAccDistance;;
	TitleValueLabel labelVideoPosition; 
	
	MainController controller;
	JPanel mainPanel;
	
	public InfoPanel(MainController controller) {
		super(controller.getModel());
		this.controller = controller;
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.DARK_GRAY),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		//this.mainPanel.setBackground(Color.GRAY);
		
		mainPanel.add(this.createFirstLine());
		mainPanel.add(this.createSecondLine());
		
		update();
	}
	JPanel createFirstLine() {
		JPanel panel = new JPanel();
		
		

		lblPosLon = new TitleValueLabel(POSLON_TITLE,
				getMapModel(),
				new NewGetMobileLongitudeCommand(getMapModel()));
		addComponent(lblPosLon);
		panel.add(lblPosLon.getPanel());
		
		panel.add(Box.createHorizontalStrut(5));

		lblPosLat = new TitleValueLabel(POSLAT_TITLE,
				getMapModel(),
				new GetMobileLatitudeCommand(getMapModel()));
		addComponent(lblPosLat);
		panel.add(lblPosLat.getPanel());

		panel.add(Box.createHorizontalStrut(5));

		lblPosX = new TitleValueLabel(POSX_TITLE,
				getMapModel(), new NewGetMobileXCommand(getMapModel()));
		addComponent(lblPosX);
		panel.add(lblPosX.getPanel());

		panel.add(Box.createHorizontalStrut(5));

		lblPosY = new TitleValueLabel(POSY_TITLE,
				getMapModel(),
				new NewGetMobileYCommand(getMapModel()));
		addComponent(lblPosY);
		panel.add(lblPosY.getPanel());

		panel.add(Box.createHorizontalStrut(5));

		lblAccuracy = new TitleValueLabel(ACCURACY_TITLE,
				getMapModel(),
				new NewGetMobileAccuracyCommand(getMapModel()));
		addComponent(lblAccuracy);
		panel.add(lblAccuracy.getPanel());
		
		panel.add(Box.createHorizontalStrut(5));
		
		lblAltitude = new TitleValueLabel(ALTITUDE_TITLE,
				getMapModel(),
				new GetMobileAltitudeCommand(getMapModel()));
		addComponent(lblAltitude);
		panel.add(lblAltitude.getPanel());

		panel.add(Box.createHorizontalStrut(5));

		
		return panel;
		
	}
	JPanel createSecondLine() {
		JPanel panel = new JPanel();

		labelVideoPosition = new TitleValueLabel(VIDEOPOSITION_TITLE, getVideoModel(), 
				new GetVideoPositionCommand(getVideoModel()));
		panel.add(this.labelVideoPosition.getPanel());
		addComponent(labelVideoPosition);
		panel.add(Box.createHorizontalStrut(5));
		
		labelAccDistance = new TitleValueLabel(ACCDISTANCE_TITLE,
				getMapModel(),	new GetAccumulateDistanceCommand(getMapModel()));
		System.out.println("addComponent: "+addComponent(labelAccDistance));
		panel.add(labelAccDistance.getPanel());
		panel.add(Box.createHorizontalStrut(5));

		lblBearing = new TitleValueLabel(BEARING_TITLE,
				getMapModel(),
				new GetMobileBearingCommand(getMapModel()));
		addComponent(lblBearing);
		panel.add(lblBearing.getPanel());

		panel.add(Box.createHorizontalStrut(5));

		lblSpeedKmh = new TitleValueLabel(SPEEDKMH_TITLE,
				getMapModel(),
				new NewGetMobileSpeedKmHCommand(getMapModel()));
		addComponent(lblSpeedKmh);
		panel.add(lblSpeedKmh.getPanel());

		panel.add(Box.createHorizontalStrut(5));

		lblSpeed = new TitleValueLabel(SPEED_TITLE,
				getMapModel(),
				new NewGetMobileSpeedMetersCommand(getMapModel()));
		addComponent(lblSpeed);
		panel.add(lblSpeed.getPanel());

		panel.add(Box.createHorizontalGlue());
		
		//panel.add(Box.createHorizontalGlue());

		return panel;
		
	}
	
	private TrackMapModel getMapModel() {
		return controller.getMapController().getMapModel();
	}
	private VideoModel getVideoModel() {
		return (VideoModel)controller.getVideoController().getModel();
	}
	public JPanel getPanel() {
		return mainPanel;
	}


}
