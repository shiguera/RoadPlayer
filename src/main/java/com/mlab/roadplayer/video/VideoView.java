package com.mlab.roadplayer.video;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import com.mlab.roadplayer.swing.SwingView;

/**
 * Un <em>VideoView</em> es un View que contiene un JPanel 
 * destinado a albergar la proyección de vídeo.<br/>
 * Implementa <em>ActionView</em> con lo que tiene acceso al 
 * controlador de vídeo.<br/>
 * 
 * @author shiguera
 *
 */
public abstract class VideoView extends SwingView  {
	private final Logger LOG = Logger.getLogger(getClass().getName());

	protected JPanel mainPanel;
	/**
	 * videoPanel es el JPanel que aloja el EmbeddedMediaPlayerComponent
	 */
	protected JPanel videoPanel;
	/**
	 * El Controller
	 */
	protected VideoController controller;
	
	// Constructor
	protected VideoView(VideoModel model, VideoController controller)  {
		super(model);
		
		this.controller = controller;
		
		videoPanel = createVideoPanel();
		initVideoPanel();

		mainPanel = new JPanel();
		mainPanel.add(videoPanel);
	}
	/**
	 * Crea un nuevo JPanel para videoPanel, ajusta el borde
	 * y el color de fondo. (Realmente el color de fondo lo da 
	 * el propio EmbeddedComponent y se fija en el VideoModel).
	 * @return
	 */
	private JPanel createVideoPanel() {
		// LOG.debug("Creating videopanel");
		JPanel videopanel = new JPanel();
		videopanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		videopanel.setBackground(Color.WHITE);
		return videopanel;
	}
	/**
	 * Remueve el anterior MediaPlayerComponent, si existe,
	 * y añade al videoPanel el MediaPlayer procedente del VideoModel.
	 * 
	 */
	public void initVideoPanel() {
		if(videoPanel!=null) {
			videoPanel.removeAll();
		}
		videoPanel.add(getMediaPlayerComponent());		
	}
	private EmbeddedMediaPlayerComponent getMediaPlayerComponent() {
		return ((VideoModel)observable).getMediaPlayerComponent();		
	}
	// Getters
	public JPanel getPanel() {
		return mainPanel;
	}
	public JPanel getVideoPanel() {
		return videoPanel;
	}
}
