package com.mlab.roadplayer.linearref;

import java.util.Iterator;
import java.util.TreeSet;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.linearref.LinearLocation;

/**
 * Implementa una referenciación lineal basada en hitos kilométricos
 * en una LineString
 * 
 * @author shiguera
 *
 */
public class PKReferencedLineString {

	protected ReferencedLineString refLineString;
	protected TreeSet<PK> pks;
	
	public PKReferencedLineString(LineString ls) {
		this.refLineString = new ReferencedLineString(ls);
		this.pks = new TreeSet<PK>();
	}
	
	public LinearLocation getLinearLocation(PKLocation pkloc) {
		if(!this.pks.contains(pkloc.getPk())) {
			return null;
		}
		double length = refLineString.getLength(pkloc.getPk().getLocation())+
				pkloc.getOffset();
		return this.refLineString.getLinearLocation(length);
	}
	public LinearLocation getLinearLocation(Coordinate coords) {
		return this.getLinearLocation(coords);
	}
	public LinearLocation getLinearLocation(double length) {
		return this.refLineString.getLinearLocation(length);
	}
	public PKLocation getPKLocation(Coordinate coords) {
		LinearLocation loc = this.refLineString.getLinearLocation(coords);
		return this.getPKLocation(loc);
	}
	public PKLocation getPKLocation(double length) {
		LinearLocation loc = this.refLineString.getLinearLocation(length);
		return this.getPKLocation(loc);
	}
	public PKLocation getPKLocation(LinearLocation linearloc) {
		PK pk = new PK("temp", 0.0, linearloc);
		PK lower = this.pks.lower(pk);
		double length = this.refLineString.getLength(linearloc);
		double offset = length - this.refLineString.getLength(lower.getLocation());
		return new PKLocation(lower, offset);	
	}
	public double getLength(Coordinate coords) {
		return this.refLineString.getLength(coords);
	}
	public double getLength(LinearLocation linearloc) {
		return this.refLineString.getLength(linearloc);
	}
	public double getLength(PKLocation pkloc) {
		double pklength = this.refLineString.getLength(pkloc.getPk().getLocation());
		return pklength + pkloc.getOffset();
	}
	
	public ReferencedLineString getReferencedLineString() {
		return this.refLineString;
	}
	public LineString getLineString() {
		return this.refLineString.getLineString();
	}
	
	public boolean addPK(PK pk) {
		if(pk.getCoordinate() != null) {
			pk.setLocation(this.refLineString.getLinearLocation(pk.getCoordinate()));
		} else if (pk.getLocation() != null) {
			pk.setCoordinate(this.refLineString.extractPoint(pk.getLocation()));
		} else {
			return false;
		}
		return this.pks.add(pk);		
	}
	public boolean removePK(PK pk) {
		return this.pks.remove(pk);
	}
	public Iterator<PK> getIterator() {
		return this.pks.iterator();
	}
	public int getPKCount() {
		return this.pks.size();
	}
	
	
}
