package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class ExportToKmlAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public ExportToKmlAction(ActionListener actionListener) {
		super(actionListener, "Exportar el track a un archivo KML (.kml)", 
				"EXPORT_TRACK_TO_KML", 
				"googleearth-icon.png", "Exportar el track a un fichero .kml");		
	}
	
}
