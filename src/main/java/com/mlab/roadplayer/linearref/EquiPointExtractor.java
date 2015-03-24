package com.mlab.roadplayer.linearref;

import java.util.ArrayList;
import java.util.List;

import org.jfree.util.Log;

import com.mlab.gpx.api.GpxFactory;
import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.api.GpxFactory.Type;
import com.mlab.gpx.impl.TrackSegment;
import com.vividsolutions.jts.linearref.LinearLocation;

public class EquiPointExtractor {
	
	protected TrackSegment segment;
	protected ReferencedSegment refseg;
	protected double segmentLength;
	protected double startLength, endLength, equidistance;
	
	public EquiPointExtractor(TrackSegment segment) {
		this.segment = segment;
	}
	
	public List<WayPoint> extract(double startLength, double endLength, double equidistance) {
		this.startLength = startLength;
		this.endLength = endLength;
		this.equidistance = equidistance;
		List<WayPoint> list = null;
		
		if(!buildRefseg()) {
			return null;
		}
		
		list = extractWayPoints();
		
		return list;
	}
	
	private boolean buildRefseg() {
		refseg = new ReferencedSegment(segment);
		if(refseg.isValid()) {
			segmentLength = refseg.getLengthIndexedLine().getEndIndex();
			if(segmentLength > 0) {
				if(!refseg.isValidLength(startLength)) {
					Log.debug("EquiPointExtractor.buildRefseg() not valid startLength " + startLength);
					startLength = 0.0;
				}
				if(!refseg.isValidLength(endLength)) {
					Log.debug("EquiPointExtractor.buildRefseg() not valid endLength " + endLength);
					endLength = segmentLength;
				}
				if(equidistance <=0 || equidistance > segmentLength) {
					Log.debug("EquiPointExtractor.buildRefseg() not valid equidistance " + equidistance);
					equidistance = segmentLength;
			
				}
//				Log.debug("EquiPointExtractor.buildRefseg() startLength=" + startLength + 
//						", endLength="+ endLength + ", equidistance=" + equidistance);
//				System.out.println("EquiPointExtractor.buildRefseg() startLength=" + startLength + 
//				", endLength="+ endLength + ", equidistance=" + equidistance);
				return true;

			}
		}
		return false;
	}
	private List<WayPoint> extractWayPoints() {
		List<WayPoint> list = new ArrayList<WayPoint>();
		WayPoint wp = null;
		double currentLength = startLength;
		while(currentLength < endLength) {
			LinearLocation currentLoc = refseg.getLocation(currentLength);
			wp = refseg.extractWayPoint(currentLoc);
			if(wp != null) {
				list.add(wp);	
			}
			currentLength += equidistance;
		}
		return list;
	}
}