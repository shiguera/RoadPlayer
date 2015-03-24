package com.mlab.roadplayer.linearref;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.linearref.LengthIndexedLine;
import com.vividsolutions.jts.linearref.LengthLocationMap;
import com.vividsolutions.jts.linearref.LinearLocation;
import com.vividsolutions.jts.linearref.LocationIndexedLine;

/**
 * Una LineString con Referenciación lineal. Utiliza una doble 
 * referenciación lineal: por longitud al origen y por posición
 * respecto del último punto de la linestring.
 * 
 * @author shiguera
 *
 */
public class ReferencedLineString {
	protected LineString lineString;
	protected LocationIndexedLine locationIndexedLine;
	protected LengthIndexedLine lengthIndexedLine;
	LengthLocationMap lengthLocationMap;
	
	/**
	 * Constructor
	 * 
	 * @param ls LineString para referenciar
	 */
	public ReferencedLineString(LineString ls) {
		this.lineString = ls;
		this.locationIndexedLine = new LocationIndexedLine(lineString);
		this.lengthIndexedLine = new LengthIndexedLine(lineString);
		this.lengthLocationMap = new LengthLocationMap(lineString);
	}

	/**
	 * Calcula la longitud desde el inicio para una LinearLocation dada
	 * @param linearloc
	 * @return
	 */
	public double getLength(LinearLocation linearloc) {
		return this.lengthLocationMap.getLength(linearloc);
	}
	/**
	 * Calcula la distancia al origen del punto proyectado sobre
	 * la polilinea de un punto dado
	 *  
	 * @param coords Coordenadas de un punto P
	 * 
	 * @return Longitud al origen del punto P', proyección
	 * del punto P en la polilinea
	 */
	public double getLength(Coordinate coords) {
		return this.lengthIndexedLine.project(coords);
	}
	/**
	 * Calcula la linear location correspondiente a una determinada distancia
	 * al origen
	 * 
	 * @param length Distancia al origen
	 * 
	 * @return LinearLocation correspondiente a esa distancia al origen
	 */
	public LinearLocation getLinearLocation(double length) {
		return this.lengthLocationMap.getLocation(length);
	}
	/**
	 * Calcula la LinearLocation correspondiente al punto P' 
	 * proyección de un punto P sobre la polilinea
	 * 
	 * @param coords Coordenadas del punto P
	 * 
	 * @return LinearLocation del punto P' proyectado del P
	 */
	public LinearLocation getLinearLocation(Coordinate coords) {
		return this.locationIndexedLine.project(coords);
	}
	/**
	 * Devuelve las coordenadas del punto de la polilinea 
	 * correspondiente a una LinearLocation
	 * 
	 * @param location LinearLocation 
	 * 
	 * @return Coordenadas del punto correspondiente
	 */
	public Coordinate extractPoint(LinearLocation location) {
		return this.locationIndexedLine.extractPoint(location);
	}
	public Coordinate extractPoint(double length) {
		return this.lengthIndexedLine.extractPoint(length);
	}
	public Geometry extractLine(double length1, double length2) {
		return this.lengthIndexedLine.extractLine(length1, length2);
	}
	// Getter
	public LineString getLineString() {
		return lineString;
	}

	public LocationIndexedLine getLocationIndexedLine() {
		return locationIndexedLine;
	}

	public LengthIndexedLine getLengthIndexedLine() {
		return lengthIndexedLine;
	}

	public LengthLocationMap getLengthLocationMap() {
		return lengthLocationMap;
	}
}
