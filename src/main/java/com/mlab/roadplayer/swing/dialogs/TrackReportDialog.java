package com.mlab.roadplayer.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.geotools.map.Layer;

import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.TrackSegment;
import com.mlab.gpx.impl.util.Util;
import com.mlab.map.TrackMap;
import com.mlab.map.TrackMapModel;
import com.mlab.map.factory.GeoToolsFactory;
import com.mlab.map.factory.WMSFactory;
import com.mlab.map.layer.GpxLayer;
import com.mlab.map.swing.MapView;
import com.mlab.map.swing.SingleMapView;
import com.mlab.roadplayer.util.TrackSegmentFile;

public class TrackReportDialog extends JDialog {
	private final Logger LOG = Logger.getLogger(getClass().getName());
	
	private static final long serialVersionUID = 1L;
	private final static String DIALOG_TITLE = "Información del Track";
	
	TrackMap controller;
	
	private TrackSegmentFile trackSegmentFile;
	private TrackSegment segment;
	private double segmentLength;
	
	public TrackReportDialog(JFrame frame, TrackSegmentFile tracksfile) {
		super(frame, DIALOG_TITLE + " " + tracksfile.getFile().getName());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setPreferredSize(new Dimension(600,400));		
		this.setBackground(Color.WHITE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LOG.debug("TrackReportDialog.windowClosing()");	
				if(controller != null) {
					controller.release();
				}
			}
			@Override
			public void windowClosed(WindowEvent e) {
				LOG.debug("TrackReportDialog.windowClosed()");			
			}		
		});
		
		this.trackSegmentFile = tracksfile;
		this.segment = this.trackSegmentFile.getSegment();
		this.segmentLength = this.segment.length();
		
		getContentPane().add(createReport());
		
		pack();
		this.setLocationRelativeTo(frame);
		//setVisible(true);
		
	}
	private JPanel createReport() {
		JPanel main = new JPanel(new BorderLayout());
		main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		main.add(createPanelSup(), BorderLayout.NORTH);
		main.add(Box.createRigidArea(new Dimension(0,10)),BorderLayout.CENTER);
		main.add(createPanelInf(), BorderLayout.SOUTH);
		return main;
		
	}
	private JPanel createPanelSup() {
		JPanel psup = new JPanel();
		psup.setLayout(new BoxLayout(psup,BoxLayout.LINE_AXIS));
		//psup.setPreferredSize(new Dimension(600,200));
		psup.setBackground(Color.white);
		psup.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(getForeground()),
				BorderFactory.createEmptyBorder(9, 9, 9, 9)));
		//psup.add(Box.createRigidArea(new Dimension(10,200)));
		JPanel mapPanel = createMapPanel();
		psup.add(mapPanel);
		psup.add(Box.createHorizontalStrut(10));
		JPanel rightPanel = createRightSupPanel();
		psup.add(rightPanel);
		return psup;
	}
	private JPanel createRightSupPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setAlignmentX(LEFT_ALIGNMENT);
		//p.setPreferredSize(new Dimension(350,180));
		p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		p.add(Box.createVerticalStrut(6));
		p.add(createTitle("Geometría"));
		p.add(createSubTitle("Longitud de la proyección en planta:"));
		p.add(this.createLabel("Puntos (ud) : ", Util.doubleToString((double)segment.size(),6,0)));
		p.add(this.createLabel("Longitud (metros) : ", Util.doubleToString(this.segmentLength,10,1)));
		p.add(Box.createVerticalStrut(5));
		p.add(createSubTitle("Datos del BoundingBox:"));
		p.add(this.createLabel("MinLat : ", this.segment.getEnvelope().getMinLat()));
		p.add(this.createLabel("MinLon : ", this.segment.getEnvelope().getMinLon()));
		p.add(this.createLabel("MaxLat : ", this.segment.getEnvelope().getMaxLat()));
		p.add(this.createLabel("MaxLon : ", this.segment.getEnvelope().getMaxLon()));
		p.add(Box.createVerticalStrut(5));
		p.add(createSubTitle("Datos de altitud:"));
		double[] minmax = Util.minmaxAltitude(segment);
		if (minmax != null) {
			p.add(this.createLabel("Altitud mínima : ", Util.doubleToString(minmax[0], 7, 1)));
			p.add(this.createLabel("Altitud máxima : ", Util.doubleToString(minmax[1], 7, 1)));
			p.add(this.createLabel("Altitud media  : ", Util.doubleToString(minmax[2], 7, 1)));			
		}
		p.add(Box.createVerticalGlue());
		return p;
	}
	private JPanel createMapPanel() {		
		TrackMapModel model = new TrackMapModel();
		MapView view = new SingleMapView(model);
		controller = new TrackMap(model, view);
		
		Layer baselayer = WMSFactory.getProxyLayer(2);
		//trackMap.setBaseMapState(new BaseMapStateBing("AerialWithLabels"));
		
		GpxLayer layer = GeoToolsFactory.createGpxLayer(trackSegmentFile.getGpxDocument());
		if(layer != null && layer.getTrackLayer(0) != null) {
			System.out.println(layer.getTrackLayer(0).getBounds());
			//trackMap.setTrackLayer(layer);
			//trackMap.zoomTrack();
		} else {
			LOG.warn("TrackReport, create trackMap: layer = null");
		}
		return controller.getView().getMainPanel();
	}
	private JPanel createPanelInf() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setAlignmentX(LEFT_ALIGNMENT);
		//p.setPreferredSize(new Dimension(600,180));
		p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		p.add(Box.createVerticalStrut(5));
		p.add(createSubTitle("Fichero :"));
		p.add(this.createLabel("Nombre : ", this.trackSegmentFile.getFile().getName()));
		p.add(this.createLabel("Tamaño (bytes): ", String.format("%d",trackSegmentFile.getFile().length())));
		p.add(this.createLabel("Fecha (UTC): ", Util.dateToString(segment.getStartTime(),true)));
		p.add(this.createLabel("Hora  (UTC): ", Util.timeToString(segment.getStartTime(),true)));
		p.add(Box.createVerticalStrut(5));
		p.add(createSubTitle("Inicio y final :"));
		p.add(this.createLabel("Inicio (lon, lat, alt) : ", wpToString(segment.getStartWayPoint())));
		p.add(this.createLabel("Final  (lon, lat, alt) : ", wpToString(segment.getEndWayPoint())));		
		p.add(createSubTitle("Tiempos :"));
		p.add(this.createLabel("Duración : ", 
			Util.secondsToHMSString(durationSeconds())));
		double[] minmaxv = Util.minmaxSpeed(segment);
		if(minmaxv != null) {
			p.add(this.createLabel("Velocidad mínima (m/s) - (Km/h) : ", 
					Util.doubleToString(minmaxv[0],12,1) + " - " + Util.doubleToString(minmaxv[0]*3.6,12,1)));		
			p.add(this.createLabel("Velocidad máxima (m/s) - (Km/h) : ", 
					Util.doubleToString(minmaxv[1],12,1) + " - " + Util.doubleToString(minmaxv[1]*3.6,12,1)));		
			p.add(this.createLabel("Velocidad media (m/s) - (Km/h) : ", 
					Util.doubleToString(minmaxv[2],12,1) + " - " + Util.doubleToString(minmaxv[2]*3.6,12,1)));		
		}
		p.add(Box.createVerticalGlue());
		p.add(Box.createVerticalStrut(5));
		return p;
	}
	private long durationSeconds() {
		return segment.getEndTime()/1000l-segment.getStartTime()/1000l;
	}
	private double vMetersSecond() {
		double v = segmentLength / (double)durationSeconds();
		return v;
	}
	private double vKmHour() {
		double v = vMetersSecond()*3.6;
		return v;
	}

	private String wpToString(WayPoint wp) {
		String s = String.format("[%12.6f,%12.6f,%12.1f ]", 
			wp.getLatitude(), wp.getLongitude(), wp.getAltitude());
		return s;
	}
	private JPanel createTitle(String title) {
		JPanel pl = new JPanel();
		pl.setLayout(new BoxLayout(pl,BoxLayout.LINE_AXIS));
		pl.setMaximumSize(new Dimension(350,20));
		pl.setAlignmentX(LEFT_ALIGNMENT);
		pl.add(Box.createHorizontalStrut(10));
		JLabel ltitle = new JLabel(title);
		ltitle.setFont(Font.decode("Sans-Bold-14"));
		ltitle.setForeground(Color.DARK_GRAY);
		pl.add(ltitle);
		pl.add(Box.createHorizontalGlue());
		return pl;
	}
	private JPanel createSubTitle(String title) {
		JPanel pl = new JPanel();
		pl.setLayout(new BoxLayout(pl,BoxLayout.LINE_AXIS));
		pl.setMaximumSize(new Dimension(350,20));
		pl.setAlignmentX(LEFT_ALIGNMENT);
		pl.add(Box.createHorizontalStrut(10));
		JLabel ltitle = new JLabel(title);
		ltitle.setFont(Font.decode("Sans-Bold-12"));
		ltitle.setForeground(Color.DARK_GRAY);
		pl.add(ltitle);
		pl.add(Box.createHorizontalGlue());
		return pl;
	}
	private JPanel createLabel(String title, String value) {
		JPanel pl = new JPanel();
		pl.setLayout(new BoxLayout(pl,BoxLayout.LINE_AXIS));
		pl.setPreferredSize(new Dimension(350,20));
		pl.setAlignmentX(LEFT_ALIGNMENT);
		pl.add(Box.createHorizontalStrut(10));
		JLabel ltitle = new JLabel(title);
		ltitle.setFont(Font.decode("Sans-Normal-12"));
		ltitle.setForeground(Color.BLACK);
		pl.add(ltitle);
		pl.add(Box.createHorizontalGlue());
		JLabel lvalue = new JLabel("");
		lvalue.setFont(Font.decode("Sans-Normal-12"));
		lvalue.setForeground(Color.BLACK);
		lvalue.setText(value);
		pl.add(lvalue);
		pl.add(Box.createHorizontalStrut(10));
		return pl;
	}
	private JPanel createLabel(String title, double value) {
		String s = String.format("%12.6f", value);
		return createLabel(title,s);
	}

}
