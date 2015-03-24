package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public class OpenVideoTrackAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public OpenVideoTrackAction(ActionListener actionListener) {
		super(actionListener, "Abrir VideoTrack", "OPEN_VIDEOTRACK", "movie_folder.png", "Abre un fichero de VideoTrack");
		
		//putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		
	}
	
}
