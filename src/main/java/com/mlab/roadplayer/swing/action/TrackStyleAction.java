package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class TrackStyleAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public TrackStyleAction(ActionListener actionListener) {
		super(actionListener, "Estilo de línea del Track", "TRACK_STYLE", 
				"color_line.png", "Estilo de línea en el mapa para el Track");		
	}
	
}
