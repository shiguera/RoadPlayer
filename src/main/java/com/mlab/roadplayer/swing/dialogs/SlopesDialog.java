package com.mlab.roadplayer.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

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

public class SlopesDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private final static String DIALOG_TITLE = "Pendientes de los tramos del Track";
	private final double DEFAULT_MINSLOPE = 0.0;
	private final double DEFAULT_MAXSLOPE = 45.0;
	
	private TrackSegmentFile trackSegmentFile;
	private TrackSegment segment;
	private List<Double> slopes;
	private double minSlope, maxSlope, avgSlope, avgUpSlope, avgDownSlope, upLength, downLength;
	private ArrayList<Double> distToOrigin;
	XYSeries slope, ycero;
	
	public SlopesDialog(JFrame frame, TrackSegmentFile tracksfile) {
		super(frame, DIALOG_TITLE + " " + tracksfile.getFile().getName());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setPreferredSize(new Dimension(600,400));		
		this.setBackground(Color.WHITE);
		
		this.trackSegmentFile = tracksfile;
		segment = trackSegmentFile.getSegment();
		
		minSlope = DEFAULT_MINSLOPE; 
		maxSlope = DEFAULT_MAXSLOPE; 
		
		slopes = Util.slopesVector(segment, true);
		calculateMinMaxSlope();
				
		getContentPane().add(createReport());
		
		pack();
		this.setLocationRelativeTo(frame);
		setVisible(true);
		
	}
	private void calculateMinMaxSlope() {
		minSlope = slopes.get(0);
		maxSlope = slopes.get(0);
		for(double d: slopes) {
			if(d<minSlope) {
				minSlope = d;
			}
			if(d>maxSlope) {
				maxSlope=d;
			}
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
		slope = new XYSeries("Pendiente en %");	
		ycero = new XYSeries("Pendiente = 0");

		XYSeriesCollection series = getSeries();
		
		JFreeChart chart = ChartFactory.createXYLineChart("", "", 
				"", series, PlotOrientation.VERTICAL, false, false, false);
		XYPlot plot = (XYPlot)chart.getPlot();
		ValueAxis domainRange = plot.getRangeAxis();
		//double gap = (maxAltitude-minAltitude)/20;
		domainRange.setRange(minSlope,maxSlope);
		ChartPanel panel = new ChartPanel(chart);
		return panel;
	}
	private XYSeriesCollection getSeries() {
		XYSeriesCollection coll = new XYSeriesCollection();
		double acumdist = 0.0;
		double acumslope = 0.0;
		avgSlope = 0.0;
		double acumUpSlope = 0.0;
		double acumDownSlope = 0.0;
		upLength = 0.0;
		downLength = 0.0;
		WayPoint previousWp = segment.getStartWayPoint();
		for(int i=0; i< segment.size()-1; i++) {
			WayPoint wp = (WayPoint)segment.get(i);
			double dprevious = Util.dist3D(wp, previousWp);
			acumdist += dprevious;
			acumslope += slopes.get(i)*dprevious;
			if(slopes.get(i)>0) {
				upLength += dprevious;
				acumUpSlope += slopes.get(i) * dprevious;
			} else if (slopes.get(i)<0) {
				downLength += dprevious;
				acumDownSlope += slopes.get(i) * dprevious;
			}
			distToOrigin.add(Double.valueOf(acumdist));
			slope.add(acumdist, slopes.get(i));
			ycero.add(acumdist, 0.0);
			previousWp = wp;
		}
		if(!slope.isEmpty()) {
			coll.addSeries(slope);			
			coll.addSeries(ycero);	
			avgSlope = acumslope / acumdist;
			avgUpSlope = acumUpSlope / upLength;
			avgDownSlope = acumDownSlope / downLength;
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
		p.add(createSubTitle("Gráfico de pendientes del track :"));
		p.add(this.createLabel("Longitud (m) : ", Util.doubleToString(segment.length(),10,1)));
		p.add(createSubTitle("Pendientes máxima, mínima y media :"));
		p.add(this.createLabel("Pendiente mínima (%) : ", Util.doubleToString(minSlope, 6,1)));
		p.add(this.createLabel("Pendiente máxima (%) : ", Util.doubleToString(maxSlope,6 ,1)));
		p.add(this.createLabel("Pendiente media (%) : ", Util.doubleToString(avgSlope,6,1)));	
		p.add(this.createLabel("Pendiente media subiendo (%) : ", Util.doubleToString(avgUpSlope,6 ,1)));
		p.add(this.createLabel("Pendiente media bajando (%) : ", Util.doubleToString(avgDownSlope, 6, 1)));	
		p.add(createSubTitle("Longitudes de subida y bajada :"));
		p.add(this.createLabel("Longitud subiendo (m) : ", Util.doubleToString(upLength,6,1)));
		p.add(this.createLabel("Longitud bajando (m) : ", Util.doubleToString(downLength,6,1)));

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
