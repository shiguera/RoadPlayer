package com.mlab.roadplayer.map.linearref;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.mlab.gpx.api.GpxDocument;
import com.mlab.gpx.api.GpxFactory;
import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.SimpleWayPoint;
import com.mlab.gpx.impl.Track;
import com.mlab.gpx.impl.TrackSegment;
import com.mlab.roadplayer.linearref.ReferencedSegment;
import com.vividsolutions.jts.linearref.LinearLocation;

public class TestReferencedSegment {

	@Test
	public void test() {
		System.out.println("TestReferencedSegment.test()... ");
		TrackSegment segment = getSegment();
		//System.out.println("Segment size: "+segment.size());
		//System.out.println("Segment canonicLength: "+segment.length());
		
		ReferencedSegment ref = new ReferencedSegment(segment);
		Assert.assertNotNull(ref);
		
		LinearLocation loc = ref.getEndLocation();
		//System.out.println("Ref canonicLength: "+l);
		Assert.assertEquals(segment.length(), ref.getLength(loc), 0.001);
		
		System.out.println("OK");
		
	}
	
	@Test
	public void testGetWayPoint() {
		System.out.println("TestReferencedSegment.testGetWayPoint()... ");
		long t = 1000l;
		List<Double> values = new ArrayList<Double>();
		values.add(-3.5);
		values.add(40.0);
		values.add(900.0);
		WayPoint wp1 = new SimpleWayPoint("","",t,values);
		t = 10000l;
		values.clear();
		values.add(-4.5);
		values.add(41.0);
		values.add(1000.0);
		WayPoint wp2 = new SimpleWayPoint("","",t,values);
		
		TrackSegment segment = new TrackSegment();
		segment.addWayPoint(wp1);
		segment.addWayPoint(wp2);
		Assert.assertNotNull(segment);
		Assert.assertEquals(2,segment.size());
		
		ReferencedSegment refseg = new ReferencedSegment(segment);
		Assert.assertTrue(refseg.isValid());
		LinearLocation loc = new LinearLocation(0,0.5);
		WayPoint wp = refseg.extractWayPoint(loc);
		System.out.println(wp.asGpx());
		Assert.assertEquals(5500l, wp.getTime());
		Assert.assertEquals(40.5, wp.getLatitude());
		Assert.assertEquals(-4.0, wp.getLongitude());
		Assert.assertEquals(950.0, wp.getAltitude());
		
		
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
