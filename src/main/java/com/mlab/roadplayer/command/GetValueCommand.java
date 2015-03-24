package com.mlab.roadplayer.command;

import com.mlab.patterns.Observable;

public abstract class GetValueCommand implements UpdateCommand {
	
	protected Observable model;
	
	protected GetValueCommand(Observable model) {
		this.model = model;
	}
	
	public Observable getModel() {
		return this.model;
	}
	
	

}
