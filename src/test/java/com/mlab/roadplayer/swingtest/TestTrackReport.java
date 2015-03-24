package com.mlab.roadplayer.swingtest;

import java.awt.Dimension;
import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

import com.mlab.roadplayer.swing.dialogs.TrackReportDialog;
import com.mlab.roadplayer.util.TrackSegmentFile;


public class TestTrackReport {
	public static void main(String[] args) {
		new TestTrackReport();
	}
	
	private TestTrackReport() {
		JFrame frame = new JFrame("Test TrackReport");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800,600));
				
		frame.pack();
		frame.setLocation(150, 50);
		frame.setVisible(true);

		TrackSegmentFile tsfile = getTrackSegmentFile();
		
		TrackReportDialog trd = new TrackReportDialog(frame, tsfile);
		//Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
		//		new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	private TrackSegmentFile getTrackSegmentFile() {
		URL url = ClassLoader.getSystemResource("20130318_125729.gpx");
		TrackSegmentFile tsf = new TrackSegmentFile(new File(url.getPath()));		
		return tsf;
	}
	
}
