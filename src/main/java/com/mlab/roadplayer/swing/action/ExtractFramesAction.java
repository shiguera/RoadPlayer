package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public class ExtractFramesAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public ExtractFramesAction(ActionListener actionListener) {
		super(actionListener, "Extraer fotogramas equiespaciados", "EXTRACT_FRAMES", "fullscreen.png", 
				"Extrae fotogramas eqiespaciados del VideoTrack actual");
		//KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK)
		
	}
	
}
