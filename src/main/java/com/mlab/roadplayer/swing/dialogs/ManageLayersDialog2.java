package com.mlab.roadplayer.swing.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.geotools.map.FeatureLayer;
import org.geotools.styling.Style;
import org.geotools.swing.MapLayerTable;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.opengis.feature.simple.SimpleFeatureType;

import com.mlab.map.TrackMap;
import com.mlab.map.TrackMapModel;
import com.mlab.map.factory.StyleFac;
import com.mlab.roadplayer.swing.LayerEditor;
import com.mlab.roadplayer.util.RPUtil;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class ManageLayersDialog2 extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final Logger LOG = Logger.getLogger(getClass().getName());
	private final static String DIALOG_TITLE = "Configurar las capas vectoriales";

	protected TrackMap controller;
	protected TrackMapModel model;
	
	protected List<LayerEditor> layerEditors;
	
	public ManageLayersDialog2(TrackMap controller, JFrame frame) {
		super(frame, DIALOG_TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModal(true);
		//setPreferredSize(new Dimension(600,400));		
		this.controller = controller;
		this.model = controller.getMapModel();
	
		getContentPane().add(createMainPanel());
		pack();
		this.setLocationRelativeTo(frame);
		this.setLocation(100, 100);
		setVisible(true);

	}
	
	private JPanel createMainPanel() {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		main.setAlignmentX(Component.LEFT_ALIGNMENT);
		main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));		
		main.setPreferredSize(new Dimension(600,400));
		
		main.add(createPanelSup());
		
		// main.add(Box.createVerticalStrut(10));
		main.add(Box.createVerticalGlue());
		
		main.add(createPanelInf());
		
		return main;
	}
	private JPanel createPanelSup() {
		return new MapLayerTable(controller.getView().getJMapPane());
	}
	private JPanel createPanelInf() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		
		JButton btn = new JButton("Salir");
		btn.setActionCommand("EXIT");
		btn.addActionListener(this);
		panel.add(Box.createHorizontalGlue());
		panel.add(btn);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getActionCommand()== "EXIT") {
			RPUtil.closeWindow(this);
			
		}
	}

	public void upLayer(FeatureLayer layer) {
		int index = model.getLayerIndex(layer);
		//System.out.println("up layer "+index+" "+layer.getTitle());
	}

	public void downLayer(FeatureLayer layer) {
		int index = model.getLayerIndex(layer);
		//System.out.println("down layer "+index+" "+layer.getTitle());
	}

	public void setLayerVisible(FeatureLayer layer, boolean visible) {
		System.out.println("Set layer visible: "+layer.getTitle()+" "+visible);
		layer.setVisible(visible);
		//model.refreshMap();
	}

	public void symbology(FeatureLayer layer) {
		System.out.println("Symbology: "+layer.getTitle());
		this.layerStyle(layer);
	}
	public void delete(FeatureLayer layer) {
		System.out.println("Delete: "+layer.getTitle());
		//model.removeVectorLayer(layer);
	}

	public void zoom(FeatureLayer layer) {
		//System.out.println("Zoom layer: "+layer.getTitle());
		controller.zoomLayer(layer);
	}
	public void layerStyle(FeatureLayer layer) {
		SimpleFeatureType type = (SimpleFeatureType) layer.getFeatureSource().getSchema();
		JSimpleStyleDialog d= createStyleDialog(type, layer.getStyle());
		d.setVisible(true);
		if(d.completed()) {
			Class geomType = type.getGeometryDescriptor().getType().getBinding();
			Style style = null;
	        if (Polygon.class.isAssignableFrom(geomType)
	                || MultiPolygon.class.isAssignableFrom(geomType)) {
	            style = StyleFac.createPolygonStyle(d.getLineColor(), (int)d.getLineWidth());
	        } else if (LineString.class.isAssignableFrom(geomType)
	                || MultiLineString.class.isAssignableFrom(geomType)) {
	        	style = StyleFac.createLineStyle(d.getLineColor(), (int) d.getLineWidth());
	        } else {
	            style = StyleFac.createPointStyle(d.getLineColor(), (int)d.getLineWidth(), 
	            		d.getFillColor(), (int)d.getPointSize());
	        }
			if (style != null) {
			     (layer).setStyle( style );
			}
		}
	}
	
	private JSimpleStyleDialog createStyleDialog(SimpleFeatureType type, Style style) {
		JSimpleStyleDialog d = new JSimpleStyleDialog(this, type, style);
//		int w = mainWindow.getFrame().getWidth();
//		int h = mainWindow.getFrame().getHeight();
		d.setBounds(100,100, 500,400);
		d.setLocationRelativeTo(this);
		return d;
	}
}
