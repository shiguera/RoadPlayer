package com.mlab.roadplayer.swing;

import com.mlab.patterns.AbstractSingleObserver;
import com.mlab.patterns.Observable;

public abstract class SwingView extends AbstractSingleObserver implements SwingPanel {

	protected SwingView(Observable observable) {
		super(observable);
	}

	

}
