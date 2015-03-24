package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class SnapshotAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public SnapshotAction(ActionListener actionListener) {
		super(actionListener, "Extraer fotograma (Snapshot)", "SNAPSHOT", "camera_add2.png", "Extrae el fotograma actual a un fichero de fotografía");
		
	}
	
}
