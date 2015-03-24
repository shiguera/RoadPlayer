package com.mlab.roadplayer.video;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import org.junit.Test;

import com.mlab.gpx.impl.util.XmlFactory;

public class TestVideoFile {

	private final static Logger LOG = Logger.getLogger(TestVideoFile.class.getName());
			

	@Test
	public void testVideoFile() {
		URL url = ClassLoader.getSystemResource("20130318_125729.mp4");
		VideoFile vf = new VideoFile(new File(url.getPath()));
		assertNotNull(vf);
		assertNotNull(vf.getFile());
		assertTrue(vf.getFile().exists());
		
		LOG.info("path: "+vf.getFile().getPath());
		LOG.info("absolutePath: "+vf.getFile().getAbsolutePath());
	}
	@Test
	public void testToXml() {
		VideoFile vf = new VideoFile(new File("src/test/java/com/mlab/roadplayer/video"),"TestVideoFile.java");
		LOG.info("\n"+XmlFactory.format(vf.toXml()));
	}
}
