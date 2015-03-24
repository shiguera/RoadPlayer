package com.mlab.roadplayer.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.mlab.roadplayer.Constants;
import com.mlab.roadplayer.util.RPUtil;

public class ConfigDialog extends JDialog implements ActionListener {
	private final Logger LOG = Logger.getLogger(ConfigDialog.class);
	private static final long serialVersionUID = 1L;
	private final static String DIALOG_TITLE = "Configuración de RoadPlayer";
	private JTextField proxyUrl, proxyPort, proxyUser, proxyPassword;
	
	public ConfigDialog(JFrame frame) {
		super(frame, DIALOG_TITLE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setPreferredSize(new Dimension(600,400));		
		this.setBackground(Color.WHITE);
		
		getContentPane().add(createMainPanel());
		
		pack();
		this.setLocationRelativeTo(frame);
		setVisible(true);
		
	}
	private JPanel createMainPanel() {
		JPanel main = new JPanel(new BorderLayout());
		main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		//main.setPreferredSize(new Dimension(400,400));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel panelConfig = createPanelConfig();
		ImageIcon icon = RPUtil.createImageIcon("proxy.jpg", 24, 24);
		tabbedPane.addTab("Proxy", icon, panelConfig);
		
		main.add(tabbedPane, BorderLayout.CENTER);

		return main;		
	}
	private JPanel createPanelConfig() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		//panel.setPreferredSize(new Dimension(400,400));

		panel.add(createTitle("Configuración del acceso a Internet a través de un proxy"));
		panel.add(Box.createRigidArea(new Dimension(6,6)));
		JLabel lbl = new JLabel();
		lbl.setFont(Font.decode("Sans-Normal-11"));
		lbl.setText("<html>Para que tengan efecto los cambios en la configuración<br/>"+
				"del proxy, será necesario salir del programa y volverlo a arrancar</html>");
		panel.add(lbl);
		panel.add(Box.createRigidArea(new Dimension(6,6)));
		
		JPanel panelSup = new JPanel();
		panelSup.setLayout(new BoxLayout(panelSup, BoxLayout.LINE_AXIS));

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		JLabel lbl1 = createLabel("Proxy url: ");
		leftPanel.add(lbl1);
		leftPanel.add(Box.createRigidArea(new Dimension(7,7)));
		JLabel lbl2 = createLabel("Proxy port: ");
		leftPanel.add(lbl2);
		leftPanel.add(Box.createRigidArea(new Dimension(7,7)));
		JLabel lbl3 = createLabel("Proxy user: ");
		leftPanel.add(lbl3);
		leftPanel.add(Box.createRigidArea(new Dimension(7,7)));
		JLabel lbl4 = createLabel("Proxy password: ");
		leftPanel.add(lbl4);
		
		panelSup.add(leftPanel);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));		
		proxyUrl = createTextField(Constants.proxyUrl);
		rightPanel.add(proxyUrl);
		rightPanel.add(Box.createRigidArea(new Dimension(3,3)));
		proxyPort = createTextField(Constants.proxyPort);
		rightPanel.add(proxyPort);
		rightPanel.add(Box.createRigidArea(new Dimension(3,3)));
		proxyUser = createTextField(Constants.proxyUserName);
		rightPanel.add(proxyUser);
		rightPanel.add(Box.createRigidArea(new Dimension(3,3)));
		proxyPassword = createTextField(Constants.proxyPassword);
		rightPanel.add(proxyPassword);
		panelSup.add(rightPanel);
		
		panel.add(panelSup);
		panel.add(Box.createRigidArea(new Dimension(5,5)));
		panel.add(Box.createVerticalGlue());
		
		JPanel panelInf = new JPanel();
		panelInf.setLayout(new BoxLayout(panelInf, BoxLayout.LINE_AXIS));
		panelInf.add(Box.createHorizontalGlue());
		JButton btn2 = new JButton("Cancelar");
		btn2.setActionCommand("CANCEL");
		btn2.addActionListener(this);
		panelInf.add(btn2);
		panelInf.add(Box.createRigidArea(new Dimension(5,5)));
		JButton btn = new JButton("Guardar");
		btn.setActionCommand("SAVE");
		btn.addActionListener(this);
		panelInf.add(btn);
		
		panel.add(panelInf);
		return panel;
	}
	
	private JLabel createLabel(String title) {
		JLabel ltitle = new JLabel(title);
		ltitle.setFont(Font.decode("Sans-Normal-12"));
		ltitle.setForeground(Color.BLACK);
		return ltitle;
	}
	private JTextField createTextField(String defaultValue) {
		JTextField lvalue = new JTextField(40);
		lvalue.setFont(Font.decode("Sans-Normal-12"));
		lvalue.setForeground(Color.BLACK);
		lvalue.setText(defaultValue);
		return lvalue;
	}
	private JPanel createTitle(String title) {
		JPanel pl = new JPanel();
		pl.setLayout(new BoxLayout(pl,BoxLayout.LINE_AXIS));
		pl.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel ltitle = new JLabel(title);
		ltitle.setFont(Font.decode("Sans-Bold-12"));
		ltitle.setForeground(Color.BLUE);
		pl.add(ltitle);
		pl.add(Box.createHorizontalGlue());
		return pl;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "SAVE") {
			Log.info("Setting new proxy parameters");
			Constants.proxyUrl=proxyUrl.getText().trim();
			Constants.proxyPort = proxyPort.getText().trim();
			Constants.proxyUserName = proxyUser.getText().trim();
			Constants.proxyPassword = proxyPassword.getText().trim();
		}
		RPUtil.closeWindow(this);
	}

}
