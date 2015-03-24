package com.mlab.roadplayer.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import org.junit.Test;

import com.mlab.gpx.impl.util.XmlFactory;

public class TestTrackSegmentFile {

	private final Logger LOG = Logger.getLogger(TestTrackSegmentFile.class.getName());
	
	@Test
	public void test() {
		URL url = ClassLoader.getSystemResource("20130318_125729.gpx");
		
		assertNotNull(url);
		TrackSegmentFile gf = new TrackSegmentFile(new File(url.getPath()));
		assertNotNull(gf);
		LOG.info("TestTrackSegmentFile.test(): GPX file read");
		assertTrue(gf.exists());
		LOG.info("TestTrackSegmentFile.test(): GPX file exists");
		assertNotNull(gf.getSegment());
		LOG.info("TestTrackSegmentFile.test(): GPX segment not null");
		LOG.info(gf.getFile().getPath());
		LOG.info("\n"+XmlFactory.format(gf.toXml()));
		LOG.info(String.format("Track %d points; canonicLength=%f m.", 
				gf.getSegment().size(),gf.getSegment().length()));
	}

}
