package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class ConfigAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public ConfigAction(ActionListener actionListener) {
		super(actionListener, "Configuración de RoadPlayer", 
				"CONFIG", 
				"help.png", "Configuración de opciones y preferencias del programa RoadPlayer");		
	}
	
}
