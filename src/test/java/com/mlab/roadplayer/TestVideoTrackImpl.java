package com.mlab.roadplayer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import org.junit.Test;

import com.mlab.gpx.impl.util.XmlFactory;
import com.mlab.roadplayer.util.TrackSegmentFile;
import com.mlab.roadplayer.video.VideoFile;
import com.mlab.roadplayer.video.VideoTrack;
import com.mlab.roadplayer.video.VideoTrackImpl;

public class TestVideoTrackImpl {

	private final Logger LOG = Logger.getLogger(TestVideoTrackImpl.class.getName());
	
	@Test
	public void test() {
		
		VideoTrack vt = createVideoTrack();
		LOG.info("\n"+XmlFactory.format(vt.toXml()));

		assertNotNull(vt);
		assertNotNull(vt.getVideoFile());
		assertNotNull(vt.getTrackSegmentFile());
		assertNotNull(vt.getVideoFile().getFile());
		assertNotNull(vt.getTrackSegmentFile().getFile());
		assertTrue(vt.isValid());
		assertTrue(vt.isValidVideoFile());
		assertTrue(vt.isValidTrackSegmentFile());		
				
	}
	private VideoTrack createVideoTrack() {
		URL url = ClassLoader.getSystemResource("20130318_125729.mp4");
		File vfile = new File(url.getPath());
		VideoFile videofile = new VideoFile(vfile);
		assertNotNull(videofile);
		
		url = ClassLoader.getSystemResource("20130318_125729.gpx");
		File gfile = new File(url.getPath());
		TrackSegmentFile tsfile = new TrackSegmentFile(gfile);
		assertNotNull(videofile);
		
		VideoTrack vt = new VideoTrackImpl(videofile, tsfile);
		assertNotNull(vt);
		
		return vt;
	}

}
