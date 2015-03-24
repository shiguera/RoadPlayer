package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class SlopesReportAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public SlopesReportAction(ActionListener actionListener) {
		super(actionListener, "Diagrama de pendientes del Track", "TRACK_SLOPES", 
				"diagram_32.png", "Informe del diagrama de pendientes del Track");		
	}
	
}
