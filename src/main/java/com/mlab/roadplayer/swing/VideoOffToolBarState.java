package com.mlab.roadplayer.swing;


public class VideoOffToolBarState extends ToolBarState {

	public VideoOffToolBarState(ToolBar toolbar) {
		super(toolbar);
	}

	@Override
	public void doAction() {
		toolBar.getBtnOpen().setEnabled(true);
		
		//toolBar.getBtnFullScreen().setEnabled(true);
		toolBar.getBtnSnapshot().setEnabled(false);
		toolBar.getBtnEquiframes().setEnabled(false);
		
		toolBar.getBtnSetBasemap().setEnabled(true);
		toolBar.getBtnAddShape().setEnabled(true);
		toolBar.getBtnTrackStyle().setEnabled(false);
		toolBar.getBtnMobileStyle().setEnabled(false);
		toolBar.getBtnExportLineShp().setEnabled(false);
		toolBar.getBtnExportPointShp().setEnabled(false);
		toolBar.getBtnExportKml().setEnabled(false);
		toolBar.getBtnTrackReport().setEnabled(false);
		toolBar.getBtnVerticalProfile().setEnabled(false);
		toolBar.getBtnSlopeProfile().setEnabled(false);
		toolBar.getBtnSpeedProfile().setEnabled(false);

		toolBar.getBtnAbout().setEnabled(true);
		toolBar.getBtnHelp().setEnabled(true);
		
		toolBar.setState(this);

	}

}
