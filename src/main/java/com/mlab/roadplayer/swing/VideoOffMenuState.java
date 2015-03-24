package com.mlab.roadplayer.swing;


public class VideoOffMenuState extends MenuState {

	public VideoOffMenuState(MainMenu menu) {
		super(menu);

	}

	@Override
	public void doAction() {
		// Menu file
		menu.getMiOpenVideoTrack().setEnabled(true);
		menu.getMiExit().setEnabled(true);
		// Menu video
		menu.getMiSnapshot().setEnabled(false);
		menu.getMiExtractFrames().setEnabled(false);
		// Menu trackMap
		menu.getMiSetBaseMap().setEnabled(true);
		menu.getMiAddShapefile().setEnabled(true);
		menu.getMiMapSnapshot().setEnabled(true);
		// Menu track
		menu.getMiTrackReport().setEnabled(false);
		menu.getMiTrackVerticalProfile().setEnabled(false);
		menu.getMiTrackSlopes().setEnabled(false);
		menu.getMiTrackVelocityProfile().setEnabled(false);
		menu.getMiTrackStyle().setEnabled(false);
		menu.getMiMobileStyle().setEnabled(false);
		menu.getMiExporTrackAsLineShapefile().setEnabled(false);
		menu.getMiExporTrackAsPointsShapefile().setEnabled(false);
		menu.getMiExportTrackAsKml().setEnabled(false);
		// Menu help
		menu.getMiConfig().setEnabled(true);
		menu.getMiAbout().setEnabled(true);
		menu.getMiHelp().setEnabled(true);
		
		menu.setState(this);
		
	}

}
