package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class MapSnapshotAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public MapSnapshotAction(ActionListener actionListener) {
		super(actionListener, "Exportar imagen del mapa", "MAP_SNAPSHOT", "export_to_picture_document.png", 
				"Exportar la imagen del mapa a fichero de imagen");		
	}
	
}
