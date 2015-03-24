package com.mlab.roadplayer.video;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class RPVideoView extends VideoView {

	private final Logger LOG = Logger.getLogger(getClass().getName());
	
	protected VideoButtonsPanel btns;
	
	public RPVideoView(VideoModel model, VideoController controller) {
		super(model, controller);
		//LOG.debug("Creating RPVideoView");

		//LOG.debug("Creating mainPanel");
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		//mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//LOG.debug("Adding videoPanel");
		mainPanel.add(videoPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(5,4)));

		btns = createBtnsPanel();
		mainPanel.add(btns.getPanel());
		//addComponent(btns);
		
		mainPanel.add(Box.createRigidArea(new Dimension(5,4)));
		
		
	}
	private VideoButtonsPanel createBtnsPanel() {
		//LOG.info("ComplexVideoView.createBtnsPanel()");
		VideoButtonsPanel btns = new VideoButtonsPanel((VideoModel)observable, controller);
		return btns;
	}
	public VideoButtonsPanel getBtns() {
		return btns;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
