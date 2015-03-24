package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class ExportAsLineShapefileAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public ExportAsLineShapefileAction(ActionListener actionListener) {
		super(actionListener, "Exportar el track a un archivo Shapefile de línea", "EXPORT_TRACK_TO_LINE_SHAPEFILE", 
				"export.png", "Exportar el track a un fichero shapefile (.shp) de línea");		
	}
	
}
