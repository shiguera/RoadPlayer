package com.mlab.roadplayer.swingtest;

import java.awt.Dimension;
import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.mlab.roadplayer.util.RPUtil;
import com.mlab.roadplayer.video.VideoFile;
import com.mlab.roadplayer.video.VideoModel;


/**
 * This is a Web Map Server "quickstart" doing the minimum required to display
 * something on screen.
 */
public class TestVideoModel {
	private final Logger LOG = Logger.getLogger(getClass().getName());
	
	public static void main(String[] args) throws Exception {
        //System.setProperty("org.geotools.referencing.forceXY", "true");
        new TestVideoModel();
	}
	private TestVideoModel() {
		
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);

		
        frame.setSize(new Dimension(400,300));
        frame.pack();
        frame.setVisible(true);
        
        VideoModel model = new VideoModel();
        model.setVideoFile(getVideoFile());
        frame.getContentPane().add(model.getMediaPlayerComponent());
        frame.getContentPane().setPreferredSize(new Dimension(600,400));
        frame.pack();
        frame.setVisible(true);
        
        model.play();
        RPUtil.pause(3000);
		
        RPUtil.closeWindow(frame);
    }
	VideoFile getVideoFile() {
		URL url = ClassLoader.getSystemResource("20130318_125729.mp4");
		VideoFile vfile = new VideoFile(new File(url.getPath()));
		return vfile;
	}
	void testReset() {
		
	}
	   
    
}