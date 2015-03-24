package com.mlab.roadplayer.swing.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.mlab.roadplayer.MainController;
import com.mlab.roadplayer.util.RPUtil;
import com.mlab.roadplayer.video.FrameExtractor;


public class FrameExtractorDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final static String DIALOG_TITLE = "Extraer fotogramas de puntos equiespaciados";
	private final Logger LOG = Logger.getLogger(FrameExtractorDialog.class);
	
	private MainController controller;
	private JFrame frame;
	private JTextField l1TextField, l2TextField, intervalTextField, directoryTextField;
	private double startLength, endLength, interval;
	private File destinationDirectory;
	
	private boolean dialogResult;
	
	public FrameExtractorDialog(JFrame frame, MainController controller) {
		super(frame, DIALOG_TITLE);
		this.frame = frame;
		this.controller = controller;
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setPreferredSize(new Dimension(600,400));		
		this.setBackground(Color.WHITE);
		
		getContentPane().add(createContentPane());
		
		pack();
		this.setLocationRelativeTo(frame);
		setVisible(true);

	}


	private JPanel createContentPane() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	
		JPanel panelDirectory = createPanelDirectory();
		panel.add(panelDirectory);
		
		panel.add(Box.createVerticalStrut(5));
		
		JPanel panelL1 = createPanelL1();
		panel.add(panelL1);

		panel.add(Box.createVerticalStrut(5));
		
		JPanel panelL2 = createPanelL2();
		panel.add(panelL2);
		
		panel.add(Box.createVerticalStrut(5));
		
		JPanel panelInterval = createPanelInterval();
		panel.add(panelInterval);
		
		panel.add(Box.createVerticalStrut(5));
		
		JPanel panelBtns = createBtnsPanel();
		panel.add(panelBtns);
		
		return panel;
	}
	private JPanel createPanelDirectory() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		//panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel("Directorio para salida de resultados:");
		panel.add(label);
		
		panel.add(Box.createHorizontalStrut(10));
		
		directoryTextField = new JTextField(6);
		panel.add(directoryTextField);
		
		JButton btn = new JButton();
		btn.setIcon(RPUtil.createImageIcon("folder_open.png", 18, 18));
		btn.setActionCommand("OPEN");
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "OPEN") {
					destinationDirectory = RPUtil.showOpenDirectoryDialog(frame, "Directorio de salida", "Seleccionar");
					if(destinationDirectory != null) {
						directoryTextField.setText(destinationDirectory.getPath());
					}
				}
			}
			
		});
		panel.add(btn);
		return panel;
	}

	private JPanel createPanelL1() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		//panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel("Distancia al origen del primer punto del intervalo:");
		panel.add(label);
		
		panel.add(Box.createHorizontalStrut(10));
		
		l1TextField = new JTextField(6);
		panel.add(l1TextField);
		return panel;
	}
	private JPanel createPanelL2() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		//panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel("Distancia al origen del último punto del intervalo:");
		panel.add(label);
		
		panel.add(Box.createHorizontalStrut(10));
		
		l2TextField = new JTextField(6);
		panel.add(l2TextField);
		return panel;
	}
	private JPanel createPanelInterval() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		//panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel("Distancia entre fotogramas:");
		panel.add(label);
		
		panel.add(Box.createHorizontalStrut(10));
	
		intervalTextField = new JTextField(6);
		panel.add(intervalTextField);
		
		
		return panel;
	}
	private JPanel createBtnsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		//panel.setAlignmentX(RIGHT_ALIGNMENT);
		
		panel.add(Box.createHorizontalGlue());
		JButton btn2 = new JButton("Cancelar");
		btn2.setActionCommand("CANCEL");
		btn2.addActionListener(this);
		panel.add(btn2);
		panel.add(Box.createRigidArea(new Dimension(5,5)));
		JButton btn = new JButton("Extraer fotogramas");
		btn.setActionCommand("EXTRACT");
		btn.addActionListener(this);
		panel.add(btn);
		return panel;
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("CANCEL")) {
			//LOG.debug("actionPerformed() CANCEL");
			dialogResult = false;
		} else if (command.equals("EXTRACT")) {
			//LOG.debug("actionPerformed() EXTRACT");		
			readTextFields();
			FrameExtractor extractor = new FrameExtractor(controller, 
					controller.getModel().getVideoTrack());
			dialogResult = extractor.extract(startLength, endLength, interval, destinationDirectory);
			LOG.debug("FrameExtractor.actionPerformed() dialogResult=" + dialogResult);
		}
		RPUtil.closeWindow(this);
	}

	private void readTextFields() {
		try {
			startLength = Double.parseDouble(l1TextField.getText().trim());
		} catch(Exception ex) {
			startLength = 0.0;
		}
		try {
			endLength = Double.parseDouble(l2TextField.getText().trim());
		} catch(Exception ex) {
			endLength = 0.0;
		}
		try {
			interval = Double.parseDouble(intervalTextField.getText().trim());
		} catch(Exception ex) {
			interval = 0.0;
		}
	
	}

	public double getStartLength() {
		return startLength;
	}


	public double getEndLength() {
		return endLength;
	}


	public double getInterval() {
		return interval;
	}


	public boolean isDialogResult() {
		return dialogResult;
	}


	public File getDestinationDirectory() {
		return destinationDirectory;
	}

}
