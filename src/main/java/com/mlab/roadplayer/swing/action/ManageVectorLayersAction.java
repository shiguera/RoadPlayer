package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionListener;

public class ManageVectorLayersAction extends MainAction {
	private static final long serialVersionUID = 1L;

	public ManageVectorLayersAction(ActionListener actionListener) {
		super(actionListener, "Configurar capas del mapa", "MANAGE_VECTOR_LAYERS", "layers.png", 
				"Configurar el estilo de visualización de las capas del mapa");
		
	}
	
}
