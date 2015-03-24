package com.mlab.roadplayer.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mlab.roadplayer.Constants;

public class AboutDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private final static String DIALOG_TITLE = "Acerca de RoadPlayer";
	
	
	public AboutDialog(JFrame frame) {
		super(frame, DIALOG_TITLE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(600,550));		
		this.setBackground(Color.WHITE);
		
		getContentPane().add(createContentPanel());
		
		pack();
		this.setLocationRelativeTo(frame);
		setVisible(true);
		
	}
	private JPanel createContentPanel() {
		JPanel main = new JPanel(new BorderLayout());
		main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		//main.setPreferredSize(new Dimension(400,400));
		main.add(createPanelSup(), BorderLayout.NORTH);
		main.add(Box.createRigidArea(new Dimension(0,10)),BorderLayout.CENTER);
		main.add(createPanelInf(), BorderLayout.SOUTH);
		return main;		
	}
	private final String lbl1 = "<html>" + 
			"<p style='font-weight:normal'>RoadPlayer es un software " + 
			"desarrollado por <strong>MercatorLab S.L.</strong></p>"+
			"<center><p><a href='http://mercatorlab.com'>http://mercatorlab.com</a></p></center>" +
			"<p style='font-weight:normal'>y distribuido por <strong>MP Scia Ingeniería</strong></p>" +
			"<center><p><a href='http://softwaredeingenieria.es'>http://softwaredeingenieria.es</a></p></center>" +
			"<br/><br/><p style='font-weight:normal'>El lenguaje de programación principal es " + 
			"<strong>Java 1.6</strong> de Oracle, si bien se utilizan una serie de librerías " +
			"de software libre sin las cuales no habría sido posible la realización del programa:" + 
			"</p><br/><p style='font-weight:normal'><strong>Geotools</strong> " + 
			"(http://www.geotools.org): GeoTools is an open source (LGPL) Java code" + 
			" library which provides standards compliant methods for the manipulation" + 
			" of geospatial data, for example to implement Geographic Information " + 
			"Systems (GIS). The GeoTools library implements Open Geospatial Consortium " + 
			"(OGC) specifications as they are developed.</p><br/>" +
			"<p style='font-weight:normal'><strong>VideoLAN</strong>" + 
			" (http://www.capricasoftware.co.uk): The vlcj project is an " + 
			"Open Source project that provides Java bindings for the excellent" + 
			" vlc media player from VideoLAN.</p><br/>" +
			"<p style='font-weight:normal'><strong>JFreeChart</strong> (http://www.jfree.org/jfreechart/): JFreeChart is a free 100% Java chart library that makes it easy for developers to display professional quality charts in their applications.</p><br/>" +
			"<p style='font-weight:normal'><strong>GPXParser</strong> (https://github.com/shiguera/gpxparser): GpxParser proporciona clases para poder crear, manipular, leer y escribir documentos GPX compuestos de WayPoints, Routes y Tracks.</p><br/>" +
			"<p style='font-weight:normal'><strong></strong></p>" +
			"<p style='font-weight:normal'><strong></strong></p>" +
			
			"</html>";
	private JPanel createPanelSup() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		//panel.setPreferredSize(new Dimension(400,400));
		panel.setBackground(Color.white);
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(createTitle("RoadPlayer Versión " + Constants.version));
		panel.add(createSubTitle("(c) 2014 Mercatorlab S.L."));
		panel.add(Box.createVerticalStrut(10));
		
		JLabel aboutText = new JLabel();
		aboutText.setText(lbl1);
		panel.add(aboutText);
		return panel;
	}
	private JPanel createPanelInf() {
		return new JPanel();
	}
	private JPanel createTitle(String title) {
		JPanel pl = new JPanel();
		pl.setLayout(new BoxLayout(pl,BoxLayout.LINE_AXIS));

		//pl.setMaximumSize(new Dimension(350,20));
		pl.setAlignmentX(LEFT_ALIGNMENT);
		pl.add(Box.createHorizontalStrut(10));
		JLabel ltitle = new JLabel(title);
		ltitle.setFont(Font.decode("Sans-Bold-14"));
		ltitle.setForeground(Color.BLACK);
		pl.add(ltitle);
		pl.add(Box.createHorizontalGlue());
		return pl;
	}
	private JPanel createSubTitle(String title) {
		JPanel pl = new JPanel();
		pl.setLayout(new BoxLayout(pl,BoxLayout.LINE_AXIS));
		//pl.setMaximumSize(new Dimension(350,20));
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
