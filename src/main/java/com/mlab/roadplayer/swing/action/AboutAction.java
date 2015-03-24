package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class AboutAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public AboutAction(ActionListener actionListener) {
		super(actionListener, "Acerca de RoadPlayer", 
				"ABOUT", 
				"info.png", "Información acerca del programa RoadPlayer");		
	}
	
}
