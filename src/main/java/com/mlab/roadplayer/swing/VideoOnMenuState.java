package com.mlab.roadplayer.swing;


public class VideoOnMenuState extends MenuState {

	public VideoOnMenuState(MainMenu menu) {
		super(menu);

	}

	@Override
	public void doAction() {
		// Menu file
		menu.getMiOpenVideoTrack().setEnabled(true);
		menu.getMiExit().setEnabled(true);
		// Menu video
		menu.getMiSnapshot().setEnabled(true);
		menu.getMiExtractFrames().setEnabled(true);
		// Menu trackMap
		menu.getMiSetBaseMap().setEnabled(true);
		menu.getMiAddShapefile().setEnabled(true);
		menu.getMiMapSnapshot().setEnabled(true);		
		// Menu Track
		menu.getMiTrackReport().setEnabled(true);
		menu.getMiTrackVerticalProfile().setEnabled(true);
		menu.getMiTrackSlopes().setEnabled(true);
		menu.getMiTrackVelocityProfile().setEnabled(true);
		menu.getMiTrackStyle().setEnabled(true);
		menu.getMiMobileStyle().setEnabled(true);
		menu.getMiExporTrackAsLineShapefile().setEnabled(true);
		menu.getMiExporTrackAsPointsShapefile().setEnabled(true);
		menu.getMiExportTrackAsKml().setEnabled(true);
		
		// Menu help
		menu.getMiConfig().setEnabled(true);
		menu.getMiAbout().setEnabled(true);
		menu.getMiHelp().setEnabled(true);
		
		menu.setState(this);
		
	}

}
