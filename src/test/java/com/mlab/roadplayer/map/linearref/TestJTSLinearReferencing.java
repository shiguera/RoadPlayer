package com.mlab.roadplayer.map.linearref;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.linearref.LinearLocation;
import com.vividsolutions.jts.linearref.LocationIndexedLine;

public class TestJTSLinearReferencing {

	@Test
	public void test() {
		System.out.println("TestJTSLinearReferencing");
		GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();
		Coordinate p0 = new Coordinate(0.0,0.0);
		Coordinate p1 = new Coordinate(1.0,1.0);
		Coordinate[] coord = {p0, p1};
		LineString ls = factory.createLineString(coord);
		System.out.println(ls.toText());
		
		LocationIndexedLine ref = new LocationIndexedLine(ls);
		
		LinearLocation loc = ref.project(p0); 
		System.out.print("P0 ");
		this.reportLoc(loc, ls);

		loc = ref.project(p1); 
		System.out.print("P1 ");
		this.reportLoc(loc, ls);
		
		Coordinate p = new Coordinate(0.50, 0.0);
		loc = ref.project(p); 
		System.out.print("P  ");
		this.reportLoc(loc, ls);
		
		
		System.out.println("OK");
	}
	private void reportLoc(LinearLocation loc, Geometry ls) {
		System.out.print(loc);
		System.out.print(" isVertex: "+loc.isVertex());
		System.out.println(" coordinates: "+loc.getCoordinate(ls).toString());
		
		
		
	}

}
