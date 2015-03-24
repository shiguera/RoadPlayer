package com.mlab.roadplayer.swingtest;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.mlab.roadplayer.util.RPUtil;
import com.mlab.roadplayer.video.RPVideoView;
import com.mlab.roadplayer.video.VideoController;
import com.mlab.roadplayer.video.VideoControllerImpl;
import com.mlab.roadplayer.video.VideoFile;
import com.mlab.roadplayer.video.VideoModel;
import com.mlab.roadplayer.video.VideoView;

public class TestComplexVideoView {

	private final Logger LOG = Logger.getLogger(getClass().getName());
	private final int DEFAULT_FRAME_WITH = 1250;
	private final int DEFAULT_FRAME_HEIGHT = 700;
	private final int DEFAULT_LOCATION_X = 50;
	private final int DEFAULT_LOCATION_Y = 50;
	private final int ICON_WIDTH = 72;
	private final int ICON_HEIGHT = 72;
	

	protected VideoModel videoModel;
	protected VideoController videoController;
	JPanel mainPanel;
	
	public static void main(String[] args) {
		new TestComplexVideoView();
	}
	
	public TestComplexVideoView() {
		JFrame frame = new JFrame("Test ComplexVideoView");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(DEFAULT_FRAME_WITH, DEFAULT_FRAME_HEIGHT);
		frame.setLocation(DEFAULT_LOCATION_X, DEFAULT_LOCATION_Y);
		frame.setIconImage(RPUtil.createImageIcon("roadicon.png", ICON_WIDTH, ICON_HEIGHT).getImage());

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		videoModel = new VideoModel();
		videoController = new VideoControllerImpl(videoModel);

		createVideoView();


		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LOG.info("TestComplexVideoView.windowClosing()");	
				videoController.release();
			}
			@Override
			public void windowClosed(WindowEvent e) {
				LOG.info("TestComplexVideoView.windowClosed()");			
			}		
		});
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);

		VideoFile vf = getDefaultVideoFile();
		
		boolean result = videoController.setVideoFile(vf);
		LOG.info(String.format("App.App(): trackMap.setVideoFile() result = %b", result));
		
		videoController.play();

		RPUtil.pause(5000);
		RPUtil.closeWindow(frame);
	}
	private void createVideoView() {
		VideoView videoView = new RPVideoView(videoModel, videoController);
		videoController.setVideoView(videoView);
		videoController.setVideoPanelSize(440, 280);
		mainPanel.add(videoController.getVideoView().getPanel());
	}
	private VideoFile getDefaultVideoFile() {
		URL urlvideo = ClassLoader.getSystemResource("20130318_125729.mp4");
		File filev = new File(urlvideo.getPath());
		if (filev.exists() == false) {
			LOG.warning("App.getDefaultVideoTrack() error: video file doesn't exist");
			return null;
		}
		VideoFile vf = new VideoFile(filev);
		return vf;
	}


}
