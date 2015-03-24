package com.mlab.roadplayer.swing;


public class VideoOnToolBarState extends ToolBarState {

	public VideoOnToolBarState(ToolBar toolbar) {
		super(toolbar);
	}

	@Override
	public void doAction() {
		toolBar.getBtnOpen().setEnabled(true);
		
		//toolBar.getBtnFullScreen().setEnabled(true);
		toolBar.getBtnSnapshot().setEnabled(true);
		toolBar.getBtnEquiframes().setEnabled(true);
		
		toolBar.getBtnSetBasemap().setEnabled(true);
		toolBar.getBtnAddShape().setEnabled(true);
		toolBar.getBtnTrackStyle().setEnabled(true);
		toolBar.getBtnMobileStyle().setEnabled(true);
		toolBar.getBtnExportLineShp().setEnabled(true);
		toolBar.getBtnExportPointShp().setEnabled(true);
		toolBar.getBtnExportKml().setEnabled(true);
		toolBar.getBtnTrackReport().setEnabled(true);
		toolBar.getBtnVerticalProfile().setEnabled(true);
		toolBar.getBtnSlopeProfile().setEnabled(true);
		toolBar.getBtnSpeedProfile().setEnabled(true);

		toolBar.getBtnAbout().setEnabled(true);
		toolBar.getBtnHelp().setEnabled(true);
		
		toolBar.setState(this);

	}

}
