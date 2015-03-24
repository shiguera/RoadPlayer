package com.mlab.roadplayer.map.linearref;

import junit.framework.Assert;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.roadplayer.linearref.ReferencedLineString;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class TestReferencedLineString {

	private static GeometryFactory factory;
	private static LineString ls;
	private static ReferencedLineString rls;
	
	@BeforeClass
	public static void before() {
		factory = JTSFactoryFinder.getGeometryFactory();
		ls = factory.createLineString(new Coordinate[]{
				new Coordinate(0.0,0.0), new Coordinate(1.0,0.0), new Coordinate(2.0, 0.0)
		});
		rls = new ReferencedLineString(ls);
		Assert.assertNotNull(rls);		
	}
	@Test
	public void testExtractLine() {
		System.out.println("TestReferencedLineString.testExtractLine()");
		Coordinate P = new Coordinate(0.5,0.5);
		double l1 = rls.getLength(P);
		LineString subline = (LineString)rls.extractLine(l1, rls.getLengthIndexedLine().getEndIndex());
		Assert.assertEquals(3, subline.getNumPoints());
		Coordinate P0 = subline.getCoordinateN(0);
		Assert.assertEquals(0.5, P0.x);
		Assert.assertEquals(0.0, P0.y);
		Coordinate P1 = subline.getCoordinateN(1);
		Assert.assertEquals(1.0, P1.x);
		Assert.assertEquals(0.0, P1.y);
		Coordinate P2 = subline.getCoordinateN(2);
		Assert.assertEquals(2.0, P2.x);
		Assert.assertEquals(0.0, P2.y);
		
		System.out.println("OK");
	}
	@Test
	public void testGetLength() {
		System.out.println("TestReferencedLineString.testGetLength()");
		Coordinate P = new Coordinate(0.5,0.5);
		double l = rls.getLength(P);
		Assert.assertEquals(0.5, l);
		Coordinate Pprim = rls.extractPoint(l);
		Assert.assertEquals(0.5, Pprim.x);
		Assert.assertEquals(0.0, Pprim.y);
		System.out.println("OK");
	}

}
