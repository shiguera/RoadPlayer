package com.mlab.roadplayer.swingtest;

import java.awt.Dimension;
import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.JFrame;

import junit.framework.Assert;

import com.mlab.roadplayer.util.RPUtil;
import com.mlab.roadplayer.video.BasicVideoView;
import com.mlab.roadplayer.video.VideoController;
import com.mlab.roadplayer.video.VideoControllerImpl;
import com.mlab.roadplayer.video.VideoFile;
import com.mlab.roadplayer.video.VideoModel;
import com.mlab.roadplayer.video.VideoView;


/**
 * This is a Web Map Server "quickstart" doing the minimum required to display
 * something on screen.
 */
public class TestVideoView {
	private final Logger LOG = Logger.getLogger(getClass().getName());
	
	public static void main(String[] args) throws Exception {
        //System.setProperty("org.geotools.referencing.forceXY", "true");
        new TestVideoView();
	}
	private TestVideoView() {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setPreferredSize(new Dimension(500,300));
        frame.pack();
        frame.setVisible(true);    

        VideoModel model = new VideoModel();
        VideoController controller = new VideoControllerImpl(model);
        VideoView view = new BasicVideoView(model, controller);
        Assert.assertNotNull(view);
        Assert.assertNotNull(view.getPanel());

        model.setVideoPanelSize(250,200);
        frame.getContentPane().add(view.getPanel());
        frame.pack();
        
        controller.setVideoFile(this.getVideoFile());
        controller.play();
        System.out.println("TestVideoView: view.getPanel().getSize()="+view.getPanel().getSize());
        
        RPUtil.pause(2000);

        RPUtil.closeWindow(frame);
    }
	VideoFile getVideoFile() {
		URL url = ClassLoader.getSystemResource("20130318_125729.mp4");
		VideoFile vfile = new VideoFile(new File(url.getPath()));
		return vfile;
	}
	
	
	   
    
}