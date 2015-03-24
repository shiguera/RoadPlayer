package com.mlab.roadplayer.linearref;

/**
 * Define la posición a lo largo de una carretera de un elemento de la misma, 
 * utilizando como referenciación la distancia del elemento a un hito kilométrico, 
 * medida a lo largo de la carretera, en el sentido ascendente del kilometraje.
 * 
 * 
 * @author shiguera
 *
 */
public class PKLocation {
	
	/**
	 * PK respecto del cual se mide la distancia del objeto referenciado
	 */
	protected PK pk;
	/**
	 * Distancia en metros a lo largo de la carretera desde el 
	 * hito kilométrico de referencia al objeto referenciado, 
	 * medida en el sentido ascendente del kilometraje 
	 */
	protected double offset;
	
	/**
	 * Constructor
	 * 
	 * @param pk
	 * @param offset
	 */
	public PKLocation(PK pk, double offset) {
		this.pk = pk;
		this.offset = offset;
	}

	public PK getPk() {
		return pk;
	}

	public void setPk(PK pk) {
		this.pk = pk;
	}

	public double getOffset() {
		return offset;
	}

	public void setOffset(double offset) {
		this.offset = offset;
	}
	

}
