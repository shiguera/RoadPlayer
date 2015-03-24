package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class ExitAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public ExitAction(ActionListener actionListener) {
		super(actionListener, "Exit", "EXIT", "exit.png", "Cierra y sale del programa RoadPlayer");
	}
	
}
