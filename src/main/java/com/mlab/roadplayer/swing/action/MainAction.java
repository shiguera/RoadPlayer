package com.mlab.roadplayer.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import com.mlab.roadplayer.util.RPUtil;

public abstract class MainAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	public final static int LARGE_ICON_WIDTH = 32;
	public final static int LARGE_ICON_HEIGHT = 32;
	public final static int SMALL_ICON_WIDTH = 18;
	public final static int SMALL_ICON_HEIGHT = 18;
	
	protected ActionListener actionListener;
	
	public MainAction(ActionListener actionListener, String text, String actionCommand, 
			String iconName, String desc) {
		super(text);
		putValue(LARGE_ICON_KEY,  RPUtil.createImageIcon(iconName, LARGE_ICON_WIDTH, LARGE_ICON_HEIGHT));
		putValue(SMALL_ICON,  RPUtil.createImageIcon(iconName, SMALL_ICON_WIDTH, SMALL_ICON_HEIGHT));		
		putValue(SHORT_DESCRIPTION, desc);
		putValue(ACTION_COMMAND_KEY, actionCommand);
		this.actionListener = actionListener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		actionListener.actionPerformed(e);
	}	

}
