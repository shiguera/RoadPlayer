package com.mlab.roadplayer.swing;


public abstract class MenuState {

	protected MainMenu menu;
	
	public MenuState(MainMenu menu) {
		this.menu = menu;
	}
	
	public abstract void doAction();
	
}
