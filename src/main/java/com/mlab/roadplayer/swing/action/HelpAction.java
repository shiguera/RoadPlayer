package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class HelpAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public HelpAction(ActionListener actionListener) {
		super(actionListener, "Ayuda de RoadPlayer", 
				"HELP", 
				"help.png", "Ayuda del programa RoadPlayer");		
	}
	
}
