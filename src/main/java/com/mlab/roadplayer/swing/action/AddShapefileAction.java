package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class AddShapefileAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public AddShapefileAction(ActionListener actionListener) {
		super(actionListener, "Añadir capa shapefile (.shp)", "ADD_SHAPEFILE", "db_add.png", 
				"Añadir una capa shapefile al mapa");
		
	}
	
}
