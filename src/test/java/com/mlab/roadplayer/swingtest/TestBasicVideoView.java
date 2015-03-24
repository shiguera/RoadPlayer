package com.mlab.roadplayer.swingtest;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.mlab.roadplayer.util.RPUtil;
import com.mlab.roadplayer.util.TrackSegmentFile;
import com.mlab.roadplayer.video.BasicVideoView;
import com.mlab.roadplayer.video.VideoController;
import com.mlab.roadplayer.video.VideoControllerImpl;
import com.mlab.roadplayer.video.VideoFile;
import com.mlab.roadplayer.video.VideoModel;
import com.mlab.roadplayer.video.VideoTrack;
import com.mlab.roadplayer.video.VideoTrackImpl;
import com.mlab.roadplayer.video.VideoView;

public class TestBasicVideoView {

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
		new TestBasicVideoView();
	}
	JFrame frame;
	public TestBasicVideoView() {
		frame = new JFrame("Test BasicVideoView");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(DEFAULT_FRAME_WITH, DEFAULT_FRAME_HEIGHT);
		frame.setLocation(DEFAULT_LOCATION_X, DEFAULT_LOCATION_Y);
		frame.setIconImage(RPUtil.createImageIcon("roadicon.png", ICON_WIDTH, ICON_HEIGHT).getImage());

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		videoModel = new VideoModel();

		videoController = new VideoControllerImpl(videoModel);
		VideoView videoView = new BasicVideoView(videoModel, videoController);
		videoController.setVideoView(videoView);
		videoController.setVideoPanelSize(600, 600);
		
		mainPanel.add(videoView.getPanel());

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LOG.info("TestBasicVideoView.windowClosing()");	
				if(videoController!=null) {
					videoController.release();
				}
			}
			@Override
			public void windowClosed(WindowEvent e) {
				LOG.info("TestBasicVideoView.windowClosed()");			
			}		
		});
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);

		
		VideoTrack vt = getDefaultVideoTrack();
		
		boolean result = videoController.setVideoFile(vt.getVideoFile());
		LOG.info(String.format("App.App(): trackMap.setVideoFile() result = %b", result));
		
		videoController.play();
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			LOG.warning("TestBasicVideoTrack.TestBasicVideoTrack() ERRROR in Thread");
		}
		videoController.pause();
		close();
	}
	private void close() {
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(frame,WindowEvent.WINDOW_CLOSING));		
	}
	private VideoTrack getDefaultVideoTrack() {
		URL urlvideo = ClassLoader.getSystemResource("20130318_125729.mp4");
		File filev = new File(urlvideo.getPath());
		if (filev.exists() == false) {
			LOG.warning("App.getDefaultVideoTrack() error: video file doesn't exist");
			return null;
		}
		VideoFile vf = new VideoFile(filev);
		URL urltrack = ClassLoader.getSystemResource("20130318_125729.gpx");
		TrackSegmentFile tf = new TrackSegmentFile(new File(urltrack.getPath()));
		return new VideoTrackImpl(vf,tf);
	}
	
	//@Test
	public void test() {
		System.out.println("TestBasicVideoView.test()");
		//Assert.assertTrue(true);
	}


}
