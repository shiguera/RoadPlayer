package com.mlab.roadplayer.map.linearref;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.gpx.api.GpxDocument;
import com.mlab.gpx.api.GpxFactory;
import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.Track;
import com.mlab.gpx.impl.TrackSegment;
import com.mlab.roadplayer.linearref.EquiPointExtractor;

public class TestEquiPointExtractor {
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void test() {
		System.out.println("TestEquiPointExtractor.test()... ");
		TrackSegment segment = getSegment();
		System.out.println("Segment size: "+segment.size());
		System.out.println("Segment canonicLength: "+segment.length());
		
		EquiPointExtractor extractor = new EquiPointExtractor(segment);
		Assert.assertNotNull(extractor);
		
		List<WayPoint> list = extractor.extract(0.0, 100.0, 10.0);
		Assert.assertEquals(10, list.size());		
	}
	
		
	private TrackSegment getSegment() {
		URL url = ClassLoader.getSystemResource("20130318_125729.gpx");
		File file = new File(url.getPath());
		assertTrue(file.exists());
		
		GpxDocument gpxdoc = GpxFactory.readGpxDocument(file);
		assertNotNull(gpxdoc);
		assertTrue(gpxdoc.hasTracks());
		Track track = gpxdoc.getTracks().get(0);
		assertTrue(track.hasSegments());
		TrackSegment segment = (TrackSegment)track.get(0);
		return segment;
	}
	
	

}
