package com.mlab.roadplayer.linearref;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.linearref.LinearLocation;

/**
 * Representa un hito kilométrico de una carretera.
 * 
 * 
 * @author shiguera
 *
 */
public class PK implements Comparable {
	/**
	 * Nombre asignado al PK, por ejemplo "PK13"
	 */
	protected String name;
	/**
	 * Distancia que representa respecto al PK cero, por
	 * ejemplo el PK13 tendrá 13000
	 */
	protected double canonicLength;
	/**
	 * El LinearLocation de un PK lo establecera la línea
	 * PKReferencedLineString que lo utilice.
	 */
	protected LinearLocation location;
	/**
	 * Coordenadas (XY) del hito kilométrico
	 */
	protected Coordinate coordinate;
	
	// Constructor
	public PK(String name, double length) {
		this.name = name;
		this.canonicLength = length;	
		this.coordinate = null;
		this.location = null;
	}
	public PK(String name, double length, LinearLocation linearloc) {
		this(name, length);
		this.location = new LinearLocation(linearloc);
	}
	public PK(String name, double length, Coordinate coordinate) {
		this(name, length);
		this.coordinate = new Coordinate(coordinate);
	}
	
	// Getters-Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getCanonicLength() {
		return canonicLength;
	}
	public void setCanonicLength(double length) {
		this.canonicLength = length;
	}
	public LinearLocation getLocation() {
		return location;
	}
	public void setLocation(LinearLocation location) {
		this.location = location;
	}
	public Coordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	
	@Override
	public int compareTo(Object o) {
		if(((PK)o).getLocation() == null) {
			throw new NullPointerException();
		}
		return this.location.compareTo((PK)o);
	}
	
	
	
}
