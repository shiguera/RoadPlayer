package com.mlab.roadplayer.swing;

import com.mlab.patterns.Observable;

public interface Controller {
	
	Observable getModel();
	SwingView getView();
	void release();

}
