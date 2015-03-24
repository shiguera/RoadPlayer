package com.mlab.roadplayer.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.TrackSegment;
import com.mlab.gpx.impl.util.Util;
import com.mlab.roadplayer.util.TrackSegmentFile;

public class VerticalProfileDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private final static String DIALOG_TITLE = "Perfil longitudinal del Track";
	private final double DEFAULT_MINALTITUDE = 0.0;
	private final double DEFAULT_MAXALTITUDE = 8700.0;
	
	private TrackSegmentFile trackSegmentFile;
	private TrackSegment segment;
	private double minAltitude, maxAltitude;
	private ArrayList<Double> distToOrigin;
	XYSeries altitude;
	
	public VerticalProfileDialog(JFrame frame, TrackSegmentFile tracksfile) {
		super(frame, DIALOG_TITLE + " " + tracksfile.getFile().getName());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setPreferredSize(new Dimension(600,400));		
		this.setBackground(Color.WHITE);
		
		this.trackSegmentFile = tracksfile;
		segment = trackSegmentFile.getSegment();
		
		minAltitude = DEFAULT_MINALTITUDE; 
		maxAltitude = DEFAULT_MAXALTITUDE; 
		calculateMinMaxAltitude();
		
		getContentPane().add(createReport());
		
		pack();
		this.setLocationRelativeTo(frame);
		setVisible(true);
		
	}
	private void calculateMinMaxAltitude() {
		double[] minmax = Util.minmaxAltitude(segment);
		if (minmax != null) {
			minAltitude = minmax[0];
			maxAltitude = minmax[1];
		}
	}
	private JPanel createReport() {
		JPanel main = new JPanel(new BorderLayout());
		main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		main.add(createPanelSup(), BorderLayout.NORTH);
		main.add(Box.createRigidArea(new Dimension(0,10)),BorderLayout.CENTER);
		main.add(createPanelInf(), BorderLayout.SOUTH);
		return main;
		
	}
	private ChartPanel createChart() {
		distToOrigin = new ArrayList<Double>();
		altitude = new XYSeries("Altitude");		

		XYSeriesCollection series = getSeries();
		
		JFreeChart chart = ChartFactory.createXYLineChart("", "", 
				"", series, PlotOrientation.VERTICAL, false, false, false);
		XYPlot plot = (XYPlot)chart.getPlot();
		ValueAxis domainRange = plot.getRangeAxis();
		double gap = (maxAltitude-minAltitude)/20;
		domainRange.setRange(minAltitude,maxAltitude+gap);
		ChartPanel panel = new ChartPanel(chart);
		return panel;
	}
	private XYSeriesCollection getSeries() {
		XYSeriesCollection coll = new XYSeriesCollection();
		double acumdist = 0.0;
		WayPoint previousWp = segment.getStartWayPoint();
		for(int i=0; i< segment.size(); i++) {
			WayPoint wp = (WayPoint)segment.get(i);
			double dprevious = Util.dist3D(wp, previousWp);
			acumdist += dprevious;
			distToOrigin.add(Double.valueOf(acumdist));
			altitude.add(acumdist, wp.getAltitude());
			previousWp = wp;
		}
		if(!altitude.isEmpty()) {
			coll.addSeries(altitude);			
		}
		return coll;
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
		psup.add(this.createChart());
		return psup;
	}
	private JPanel createPanelInf() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setAlignmentX(LEFT_ALIGNMENT);
		//p.setPreferredSize(new Dimension(600,180));
		p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		p.add(Box.createVerticalStrut(5));
		p.add(createSubTitle("Perfil longitudonal del track :"));
		p.add(this.createLabel("Longitud (m) : ", Util.doubleToString(segment.length(),10,1)));
		p.add(createSubTitle("Inicio y final :"));
		p.add(this.createLabel("Altitud inicial (m) : ", Util.doubleToString(segment.getStartWayPoint().getAltitude(),6,1)));
		p.add(this.createLabel("Altitud final (m) : ", Util.doubleToString(segment.getEndWayPoint().getAltitude(),6,1)));
		double[] minmax = Util.minmaxAltitude(segment);
		p.add(createSubTitle("Alturas máxima, mínima y media :"));
		if(minmax != null) {
			p.add(this.createLabel("Altitud mínima (m) : ", Util.doubleToString(minmax[0],6,1)));
			p.add(this.createLabel("Altitud maxima (m) : ", Util.doubleToString(minmax[1],6,1)));
			p.add(this.createLabel("Altitud media (m) : ", Util.doubleToString(minmax[2],6,1)));	
		}
		p.add(Box.createVerticalGlue());
		p.add(Box.createVerticalStrut(5));
		return p;
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
