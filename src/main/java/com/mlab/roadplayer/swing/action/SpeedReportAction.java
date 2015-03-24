package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class SpeedReportAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public SpeedReportAction(ActionListener actionListener) {
		super(actionListener, "Diagrama de velocidades del Track", "TRACK_VELOCITYPROFILE", 
				"diagram_32.png", "Diagrama de velocidades del Track");		
	}
	
}
