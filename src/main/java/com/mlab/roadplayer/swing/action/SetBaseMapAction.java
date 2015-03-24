package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class SetBaseMapAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public SetBaseMapAction(ActionListener actionListener) {
		super(actionListener, "Seleccionar el mapa base", "SET_BASEMAP", "maps.png", "Seleccionar el mapa base");
		
	}
	
}
