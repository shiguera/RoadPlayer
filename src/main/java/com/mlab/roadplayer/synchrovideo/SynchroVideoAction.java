package com.mlab.roadplayer.synchrovideo;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import com.mlab.map.TrackMap;
import com.mlab.map.action.MapAction;

public class SynchroVideoAction extends MapAction {
	private final Logger LOG = Logger.getLogger(SynchroVideoAction.class);
	final static String TEXT = "";
	final static String ACTION_COMMAND = "SYNCHRO_VIDEO";
	final static String ICON_NAME = "movie.png";
	final static String DESCRIPTION = "Synchro video";

	TrackMap trackMap;
	SynchroVideoToolListener listener;
	private static final long serialVersionUID = 1L;

	/**
     * Constructor. The associated control will be labelled with an icon.
     * 
     * @param mapPane the trackMap pane being serviced by this action
     */
    public SynchroVideoAction(TrackMap trackmap, SynchroVideoToolListener listener) {
        super(trackmap, TEXT, ACTION_COMMAND, ICON_NAME, DESCRIPTION);
        this.listener = listener;
        this.trackMap = trackmap;
    }

    
    /**
     * Called when the associated control is activated. Leads to the
     * trackMap pane's cursor tool being set to a PanTool object
     *
     * @param ev the event (not used)
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        trackMap.getView().getJMapPane().setCursorTool(new SynchroVideoTool(listener));
    }


}
