package com.mlab.roadplayer.swing;


public abstract class ToolBarState {

	protected ToolBar toolBar;
	
	public ToolBarState(ToolBar toolbar) {
		this.toolBar = toolbar;
	}
	
	public abstract void doAction();
	
}
