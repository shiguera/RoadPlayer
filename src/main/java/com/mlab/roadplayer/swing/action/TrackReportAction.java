package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class TrackReportAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public TrackReportAction(ActionListener actionListener) {
		super(actionListener, "Informe del Track", "TRACK_REPORT", "diagram_32.png", 
				"Informe del Track: longitud, inicio, final, duración,...");
		
	}
	
}
