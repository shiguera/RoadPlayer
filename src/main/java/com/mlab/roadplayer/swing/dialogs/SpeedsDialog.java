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

public class SpeedsDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private final static String DIALOG_TITLE = "Perfil de velocidades de recorrido";
	private final double DEFAULT_MINSPEED = 0.0;
	private final double DEFAULT_MAXSPEED = 150.0;
	
	private TrackSegmentFile trackSegmentFile;
	private TrackSegment segment;
	private List<Double> speeds;
	private double minSpeed, maxSpeed, avgSpeed, avgUpSlope, avgDownSlope, upLength, downLength;
	private ArrayList<Double> distToOrigin;
	XYSeries speed, speedAvg;
	
	public SpeedsDialog(JFrame frame, TrackSegmentFile tracksfile) {
		super(frame, DIALOG_TITLE + " " + tracksfile.getFile().getName());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setPreferredSize(new Dimension(600,400));		
		this.setBackground(Color.WHITE);
		
		this.trackSegmentFile = tracksfile;
		segment = trackSegmentFile.getSegment();
		
		minSpeed = DEFAULT_MINSPEED; 
		maxSpeed = DEFAULT_MAXSPEED; 
		
		speeds = Util.speedsVector(segment);
		calculateMinMaxSpeed();
				
		getContentPane().add(createReport());
		
		pack();
		this.setLocationRelativeTo(frame);
		setVisible(true);
		
	}
	private void calculateMinMaxSpeed() {
		minSpeed = speeds.get(0)*3.6;
		maxSpeed = speeds.get(0)*3.6;
		for(double d: speeds) {
			if(d*3.6<minSpeed) {
				minSpeed = d*3.6;
			}
			if(d*3.6>maxSpeed) {
				maxSpeed=d*3.6;
			}
		}
		System.out.println("minSpeed, maxSpeed="+minSpeed+", "+maxSpeed);
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
		speed = new XYSeries("Velocidad en km/h");	
		speedAvg = new XYSeries("Velocidad media");

		XYSeriesCollection series = getSeries();
		
		JFreeChart chart = ChartFactory.createXYLineChart("", "", 
				"", series, PlotOrientation.VERTICAL, false, false, false);
		XYPlot plot = (XYPlot)chart.getPlot();
		ValueAxis domainRange = plot.getRangeAxis();
		//double gap = (maxAltitude-minAltitude)/20;
		domainRange.setRange(minSpeed,maxSpeed);
		ChartPanel panel = new ChartPanel(chart);
		return panel;
	}
	private XYSeriesCollection getSeries() {
		XYSeriesCollection coll = new XYSeriesCollection();
		double acumdist = 0.0;
		double acumspeed = 0.0;
		avgSpeed = 0.0;
		WayPoint previousWp = segment.getStartWayPoint();
		for(int i=0; i< segment.size()-1; i++) {
			WayPoint wp = (WayPoint)segment.get(i);
			double dprevious = Util.dist3D(wp, previousWp);
			acumdist += dprevious;
			acumspeed += speeds.get(i)*3.6*dprevious/1000;
			distToOrigin.add(Double.valueOf(acumdist));
			speed.add(acumdist, speeds.get(i)*3.6);
			previousWp = wp;
		}
		if(!speed.isEmpty()) {
			coll.addSeries(speed);	
			avgSpeed = acumspeed / acumdist * 1000;
			System.out.println("avgSpeed "+avgSpeed);
			for(int i=0; i< segment.size()-1; i++) {
				speedAvg.add((double)distToOrigin.get(i), avgSpeed);	
			}
			coll.addSeries(speedAvg);	
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
		p.add(createSubTitle("Velocidades máxima, mínima y media :"));
		p.add(this.createLabel("Velocidad mínima (Km/h) : ", Util.doubleToString(minSpeed, 6,1)));
		p.add(this.createLabel("Velocidad máxima (Km/h) : ", Util.doubleToString(maxSpeed,6 ,1)));
		p.add(this.createLabel("Velocidad media (Km/h) : ", Util.doubleToString(avgSpeed,6,1)));	

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
