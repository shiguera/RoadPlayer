package com.mlab.roadplayer.video;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.mlab.roadplayer.swing.SwingView;
import com.mlab.roadplayer.util.RPUtil;

public class VideoButtonsPanel extends SwingView implements ActionListener {
	private final Logger LOG = Logger.getLogger(getClass().getName());

	private final int BUTTON_WIDTH = 40;
	private final int BUTTON_HEIGHT = 34;
	private final int ICON_WIDTH = 28;
	private final int ICON_HEIGHT = 28;
	
	private JPanel mainPanel;
	private JPanel btnPanel;
	private VideoController controller;

	public VideoButtonsPanel(VideoModel model, VideoController controller) {
		super(model);
		this.controller = controller;

		createBtnsPanel();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setMinimumSize(new Dimension(500,34));
		mainPanel.add(btnPanel);
	}
	
	private void createBtnsPanel() {
		//LOG.info("VideoPlayerButtons.createBtnsPanel()");
		btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.LINE_AXIS));
		//btnPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT));
		
		JButton btnBegin = createButton("BEGIN", "start.png");
		btnPanel.add(btnBegin);
		btnPanel.add(Box.createHorizontalStrut(5));
		
		JButton btnSkipB = createButton("SKIPBK", "rewind.png");
		btnPanel.add(btnSkipB);
		btnPanel.add(Box.createHorizontalStrut(5));
		
		JButton btnPlay = createButton("PLAY", "play.png");
		btnPanel.add(btnPlay);
		btnPanel.add(Box.createHorizontalStrut(5));
		
		JButton btnPause = createButton("PAUSE", "pause.png");
		btnPanel.add(btnPause);
		btnPanel.add(Box.createHorizontalStrut(5));
		
		JButton btnSkipF = createButton("SKIPFW", "fast_forward.png");
		btnPanel.add(btnSkipF);
		btnPanel.add(Box.createHorizontalStrut(5));
		
		JButton btnEnd = createButton("END", "end.png");
		btnPanel.add(btnEnd);
		btnPanel.add(Box.createHorizontalStrut(5));
		
	}
	
	private JButton createButton(String actionCommand, String imagename) {
		ImageIcon icon = RPUtil.createImageIcon(imagename, ICON_WIDTH, ICON_HEIGHT);
		JButton btn = new JButton("", icon);
		btn.setPreferredSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));
		btn.setActionCommand(actionCommand);
		btn.addActionListener(this);
		return btn;
	}
		
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "PLAY") {
			this.controller.play();
		} else if (e.getActionCommand() == "SKIPFW") {
			this.controller.skipForward();
		} else if (e.getActionCommand() == "SKIPBK") {
			this.controller.skipBack();
		} else if (e.getActionCommand() == "PAUSE") {
			this.controller.pause();
		} else if (e.getActionCommand() == "BEGIN") {
			this.controller.goToBeginning();
		} else if (e.getActionCommand() == "END") {
			this.controller.goToEnd();
		}	
	}
	public void update() {
		// Nothing to do		
	}

	@Override
	public JPanel getPanel() {
		return mainPanel;
	}
}
