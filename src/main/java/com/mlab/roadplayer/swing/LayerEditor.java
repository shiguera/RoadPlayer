package com.mlab.roadplayer.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.geotools.map.FeatureLayer;

import com.mlab.roadplayer.swing.dialogs.ManageLayersDialog;
import com.mlab.roadplayer.util.RPUtil;

/**
 * Gestiona la edición de una de las capas en el ManageLayersDialog
 * 
 * @author shiguera
 *
 */
public class LayerEditor implements SwingPanel, ActionListener, ItemListener {

	protected ManageLayersDialog dialog;
	protected JPanel mainPanel;
	protected FeatureLayer layer;
	protected int layerIndex;
	
	private JButton btnUp, btnDown, btnStyle, btnZoomLayer, btnDelete;
	private JCheckBox chkVisible;

	public LayerEditor(ManageLayersDialog dialog, FeatureLayer layer, int layerindex) {
		this.dialog = dialog;
		this.layer = layer;
		this.layerIndex = layerindex;
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		//mainPanel.setPreferredSize(new Dimension(400,36));

		mainPanel.add(Box.createHorizontalStrut(10));

		mainPanel.add(this.createUpDownButtons());
		mainPanel.add(Box.createHorizontalStrut(10));
		
		mainPanel.add(createIndexLabel(getLayerIndex()));

		mainPanel.add(Box.createHorizontalStrut(10));
		mainPanel.add(createNameLabel());
		
		mainPanel.add(Box.createHorizontalStrut(10));
		mainPanel.add(this.createCheckBoxVisibility());

		mainPanel.add(Box.createHorizontalStrut(10));
		mainPanel.add(this.createButtonSymbol());

		mainPanel.add(Box.createHorizontalStrut(10));
		mainPanel.add(this.createButtonZoomLayer());

		mainPanel.add(Box.createHorizontalStrut(10));
		mainPanel.add(this.createButtonDelete());
		
		mainPanel.add(Box.createHorizontalGlue());
		//mainPanel.add(Box.createHorizontalStrut(10));
		
	}
	private JButton createButtonSymbol() {
		JButton btn = new JButton("Símbolo");
		btn.setAlignmentY(Component.CENTER_ALIGNMENT);
		btn.setAlignmentX(Component.LEFT_ALIGNMENT);
		//btn.setPreferredSize(new Dimension(30,18));
		//btn.setIcon(RPUtil.createImageIcon("up.png", 14, 14));
		btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btn.setToolTipText("Configurar la simbología de la capa");
		btn.setActionCommand("SYMBOL");
		btn.addActionListener(this);
		return btn;	
	}
	private JButton createButtonZoomLayer() {
		JButton btn = new JButton("Zoom Capa");
		btn.setAlignmentY(Component.CENTER_ALIGNMENT);
		btn.setAlignmentX(Component.LEFT_ALIGNMENT);
		//btn.setPreferredSize(new Dimension(30,18));
		//btn.setIcon(RPUtil.createImageIcon("up.png", 14, 14));
		btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btn.setToolTipText("Zoom a la capa");
		btn.setActionCommand("ZOOM_LAYER");
		btn.addActionListener(this);
		return btn;	
	}

	private JButton createButtonDelete() {
		JButton btn = new JButton("Borrar");
		btn.setAlignmentY(Component.CENTER_ALIGNMENT);
		btn.setAlignmentX(Component.LEFT_ALIGNMENT);
		//btn.setPreferredSize(new Dimension(30,18));
		//btn.setIcon(RPUtil.createImageIcon("up.png", 14, 14));
		btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btn.setToolTipText("Borra la capa");
		btn.setActionCommand("DELETE");
		btn.addActionListener(this);
		return btn;	
	}
	private JPanel createUpDownButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		btnUp = new JButton();
		btnUp.setAlignmentY(Component.CENTER_ALIGNMENT);
		btnUp.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnUp.setPreferredSize(new Dimension(18,18));
		btnUp.setIcon(RPUtil.createImageIcon("up.png", 14, 14));
		btnUp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btnUp.setToolTipText("Sube la capa");
		btnUp.setActionCommand("UP");
		btnUp.addActionListener(this);
		panel.add(btnUp);
		
		panel.add(Box.createHorizontalStrut(5));
		
		btnDown = new JButton();
		btnDown.setAlignmentY(Component.CENTER_ALIGNMENT);
		btnUp.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnDown.setPreferredSize(new Dimension(18,18));
		btnDown.setIcon(RPUtil.createImageIcon("down.png", 14, 14));
		btnDown.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btnDown.setToolTipText("Sube la capa");
		btnDown.setActionCommand("DOWN");
		btnDown.addActionListener(this);
		panel.add(btnDown);

		return panel;
	}
	private JLabel createIndexLabel(int index) {
		JLabel indexlabel = new JLabel();
		indexlabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		indexlabel.setText(String.format("%d", index));
		//indexlabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		indexlabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		indexlabel.setPreferredSize(new Dimension(20,20));
		return indexlabel;
	}
	private JLabel createNameLabel() {
		JLabel namelabel = new JLabel();
		namelabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		namelabel.setText(layer.getTitle());
		//namelabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		namelabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		namelabel.setPreferredSize(new Dimension(200,20));
		return namelabel;
	}
	private JCheckBox createCheckBoxVisibility() {
		chkVisible = new JCheckBox("");
		this.chkVisible.setSelected(layer.isVisible());
		this.chkVisible.addItemListener(this);
		return chkVisible;
	}

	public int getLayerIndex() {
		return layerIndex;
	}
	public FeatureLayer getLayer() {
		return layer;
	}
	public JButton getBtnUp() {
		return btnUp;
	}
	public JButton getBtnDown() {
		return btnDown;
	}

	@Override
	public JPanel getPanel() {
		return mainPanel;
	}

	
	public void setSize(int width, int height) {
		mainPanel.setSize(width, height);
	}
	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getActionCommand()=="UP") {
			dialog.upLayer(layer);
		} else if(ev.getActionCommand()=="DOWN") {
			dialog.downLayer(layer);
		} else if(ev.getActionCommand()=="SYMBOL") {
			dialog.symbology(layer);
		} else if(ev.getActionCommand()=="DELETE") {
			dialog.delete(layer);
		} else if(ev.getActionCommand()=="ZOOM_LAYER") {
			dialog.zoom(layer);
		}
	}
	@Override
	public void itemStateChanged(ItemEvent ev) {
		if(ev.getSource()== chkVisible) {
			boolean visible = chkVisible.isSelected();
			dialog.setLayerVisible(layer, visible);
		}
	}

}
