package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class ExportAsPointShapefileAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public ExportAsPointShapefileAction(ActionListener actionListener) {
		super(actionListener, "Exportar el track a un archivo Shapefile de puntos", 
				"EXPORT_TRACK_TO_POINTS_SHAPEFILE", 
				"export.png", "Exportar el track a un fichero shapefile (.shp) de puntos");		
	}
	
}
