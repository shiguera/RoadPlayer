package com.mlab.roadplayer.synchrovideo;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;

/**
 * Proporciona la herramienta para que al pinchar en el mapa posicione el vídeo
 * @author shiguera
 *
 */
public class SynchroVideoTool extends CursorTool {
	private static Logger LOG = Logger.getLogger(SynchroVideoTool.class);
	
	 /** Tool name */
    public static final String TOOL_NAME = "SynchroVideoTool";
    
    /** Tool tip text */
    public static final String TOOL_TIP = "Posicionar el vídeo en el punto más proximo al del mapa";
    
    /** Cursor */
    public static final String CURSOR_IMAGE = "/org/geotools/swing/icons/pointer.png";
    
    /** Cursor hotspot coordinates */
    public static final Point CURSOR_HOTSPOT = new Point(15, 15);
    
    /** Icon for the control */
    public static final String ICON_IMAGE = "/com/mlab/roadplayer/impl/trackMap/videotool.png";
    
    private Cursor cursor;
    private SynchroVideoToolListener listener;
    
    private Point panePos;
    private DirectPosition2D worldPos;
    /**
     * Constructor
     */
    public SynchroVideoTool(SynchroVideoToolListener listener) {
    	LOG.info("SynchroVideoTool: "+listener.toString());
    	this.listener = listener;
        Toolkit tk = Toolkit.getDefaultToolkit();
        ImageIcon imgIcon = new ImageIcon(getClass().getResource(CURSOR_IMAGE));
        cursor = tk.createCustomCursor(imgIcon.getImage(), CURSOR_HOTSPOT, TOOL_NAME);
    }
    /**
     * Respond to a mouse button press event from the trackMap mapPane. This may
     * signal the start of a mouse drag. Records the event's window position.
     * @param ev the mouse event
     */
    @Override
    public void onMousePressed(MapMouseEvent ev) {
        panePos = ev.getPoint();
        DirectPosition2D wpos = ev.getWorldPos();
        LOG.debug("SynchroVideoTool: " + panePos.toString() + " - " + wpos.toString());
        listener.setPosition(wpos);
    }
    /**
     * Get the mouse cursor for this tool
     */
    @Override
    public Cursor getCursor() {
        return cursor;
    }
    
    /**
     * Returns false to indicate that this tool does not draw a box
     * on the trackMap display when the mouse is being dragged
     */
    @Override
    public boolean drawDragBox() {
        return false;
    }
}
