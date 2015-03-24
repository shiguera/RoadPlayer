package com.mlab.roadplayer.swing;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.TrackSegment;
import com.mlab.gpx.impl.util.Util;
import com.mlab.roadplayer.Constants;
import com.mlab.roadplayer.linearref.ReferencedSegment;


public class AltitudePanel implements SwingPanel {

	private final Logger LOG = Logger.getLogger(AltitudePanel.class);
	private final double DEFAULT_MIN_ALTITUDE = 0.0;
	private final double DEFAULT_MAX_ALTITUDE = 1000.0;
	private final double DEFAULT_MIN_DISTANCE = 0.0;
	private final double DEFAULT_MAX_DISTANCE = 1000.0;

	JPanel mainPanel;
	
	TrackSegment segment;
	ReferencedSegment refseg;
	
	List<Double> distToOrigin;
	double minAltitude, maxAltitude;
	double minDistance, maxDistance;
	
	XYSeriesCollection dataset;
	XYSeries altitudeSeries;
	XYSeries pointSeries;
	NumberAxis domainAxis, altitudeAxis;
	XYPlot xyPlot;
	
	public AltitudePanel() {
		
		minDistance = DEFAULT_MIN_DISTANCE;
		maxDistance = DEFAULT_MAX_DISTANCE;
		minAltitude = DEFAULT_MIN_ALTITUDE;
		maxAltitude = DEFAULT_MAX_ALTITUDE;
		
		createMainPanel();
		createEmptyChart();
	}
	private void createMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	private void createEmptyChart() {
		dataset = new XYSeriesCollection();
		altitudeSeries = new XYSeries("Altitude");
		dataset.addSeries(altitudeSeries);
		domainAxis = createDomainAxis();
		domainAxis.setRange(minDistance, maxDistance);
		altitudeAxis = createAltitudeAxis();
		altitudeAxis.setRange(minAltitude, maxAltitude);
		XYLineAndShapeRenderer render = createRender();
		xyPlot = new XYPlot(dataset,domainAxis, altitudeAxis, render);

		JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, xyPlot, false ); 
		ChartPanel panel = new ChartPanel(chart);
		mainPanel.removeAll();
		mainPanel.add(panel, BorderLayout.CENTER);
	}
	

	private void createChart() {
		dataset = new XYSeriesCollection();
		distToOrigin = new ArrayList<Double>();
		altitudeSeries = createAltitudeSeries();
		if(!altitudeSeries.isEmpty() ) {
			dataset.addSeries(altitudeSeries);			
		}

		pointSeries = new XYSeries("Point");
		pointSeries.add(0.0, segment.getStartWayPoint().getAltitude());
		dataset.addSeries(pointSeries);

		domainAxis = createDomainAxis();		
		domainAxis.setRange(0,distToOrigin.get(distToOrigin.size()-1));
		altitudeAxis = createAltitudeAxis();

		XYLineAndShapeRenderer render = createRender();
		xyPlot = new XYPlot(dataset,domainAxis, altitudeAxis, render);

		JFreeChart chart = new JFreeChart("Combined", JFreeChart.DEFAULT_TITLE_FONT, xyPlot, false ); 
		chart.setTitle("");
		ChartPanel panel = new ChartPanel(chart);
		mainPanel.removeAll();
		mainPanel.add(panel, BorderLayout.CENTER);
	}
	private NumberAxis createDomainAxis() {
		NumberAxis axis = new NumberAxis("DTO");
		axis.setLabel("");
		return axis;
	}
	private NumberAxis createAltitudeAxis() {
		NumberAxis axis = new NumberAxis("Altitude");
		axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		axis.setRange(minAltitude, maxAltitude);
		axis.setLabel("");
		return axis;
	}
	private XYSeries createAltitudeSeries() {
		XYSeries altseries = new XYSeries("altitude");
		double acumdist = 0.0;
		minAltitude = segment.getStartWayPoint().getAltitude();
		maxAltitude=segment.getStartWayPoint().getAltitude();
		WayPoint previousWp = segment.getStartWayPoint();
		for(int i=0; i< segment.size(); i++) {
			WayPoint wp = (WayPoint)segment.get(i);
			double dprevious = Util.dist3D(wp, previousWp);
			acumdist += dprevious;
			distToOrigin.add(Double.valueOf(acumdist));
			double alt = wp.getAltitude();
			if(alt<minAltitude) minAltitude=alt;
			if(alt>maxAltitude) maxAltitude = alt;
			altseries.add(acumdist, wp.getAltitude());
			//System.out.println(acumdist+", "+wp.getAltitude()+", "+((AndroidWayPoint)wp).getSpeed());
			previousWp = wp;
		}
		maxDistance = distToOrigin.get(distToOrigin.size()-1);
		return altseries;
	}
	
	private XYLineAndShapeRenderer createRender() {
		XYLineAndShapeRenderer render = new XYLineAndShapeRenderer();
		render.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
		Shape circle = new Ellipse2D.Double(-5, -5, 10, 10);
		render.setSeriesPaint(0, new Color(Constants.mobilePointLineColor));
		render.setSeriesPaint(1, Color.RED);
		render.setSeriesShape(1, circle);
		render.setSeriesShapesVisible(0, false);
		render.setSeriesShapesVisible(1, true);
		return render;
	}
	
	

	public void setTrackSegment(TrackSegment segment) {
		this.segment = segment;
		this.refseg = new ReferencedSegment(segment);
		this.createChart();
		
	}
	public void setPoint(WayPoint wp) {
		if(wp!=null) {
			double dist = refseg.getLength(refseg.project(wp.getLongitude(), wp.getLatitude()));
			if(dist>=minDistance && dist<=maxDistance) {
				pointSeries.remove(0);
				pointSeries.add(dist, wp.getAltitude());
			}			
		}
	}
	// SwingView
	@Override
	public JPanel getPanel() {
		return mainPanel;
	}

}
