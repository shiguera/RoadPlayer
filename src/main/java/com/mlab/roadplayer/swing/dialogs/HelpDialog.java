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

public class HelpDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private final static String DIALOG_TITLE = "Ayuda de RoadPlayer";
	
	
	public HelpDialog(JFrame frame) {
		super(frame, DIALOG_TITLE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(600,300));		
		this.setBackground(Color.WHITE);
		
		getContentPane().add(createContentPane());
		
		pack();
		this.setLocationRelativeTo(frame);
		setVisible(true);
		
	}
	private JPanel createContentPane() {
		JPanel main = new JPanel(new BorderLayout());
		main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		//main.setPreferredSize(new Dimension(400,400));
		main.add(createPanelSup(), BorderLayout.NORTH);
		main.add(Box.createRigidArea(new Dimension(0,10)),BorderLayout.CENTER);
		main.add(createPanelInf(), BorderLayout.SOUTH);
		return main;		
	}
	private final String lbl1 = 
			"<html>"  
				+ "<p style='font-weight:normal'>Con el programa se entregan algunos ficheros de ejemplo que se encuentran en los "
				+ "siguientes subdirectorios del directorio de instalación del programa:</p>"
				+ "<ul>" 
				+ "<li style='font-weight:normal'>Directorio <strong>carto</strong> con algunas " 
				+ "capas de mapas en formato shapefile que podrá añadir a "
				+ "la visualización de la ventana del mapa</li>"
				+ "<li style='font-weight:normal'>Directorio <strong>video</strong> con algunos vídeos de ejemplo " 
				+ "filmados con <strong>RoadRecorder</strong></li>" +
				"<p style='font-weight:normal'>En el portal de RoadPlayer " + 
				"Se pueden encontrar detalles de características y vídeos " + 
				"que explican el funcionamiento del programa</p>" +
				"<center><p><a href='http://softwaredeingenieria.es/roadplayer'>" +
				"http://softwaredeingenieria.es/roadplayer</a></p></center>" +
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
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JLabel ta = new JLabel();
		ta.setText(lbl1);
		panel.add(ta);
		return panel;
	}
	private JPanel createPanelInf() {
		return new JPanel();
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
