package com.mlab.roadplayer.swing;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.mlab.roadplayer.MainWindow;
import com.mlab.roadplayer.swing.action.MainAction;

/**
 * ToolBar de la ventana principal.Tiene implementado
 * un sistema de 'estado' en el campo 'state'. Existen clases Video...ToolBarState 
 * que controlan el estado de activación de los botones 
 * 
 * @author shiguera
 *
 */
public class ToolBar {
	
	private final int ICON_WIDTH = 24;
	private final int ICON_HEIGHT = 24;
	protected MainWindow mainWindow;
	
	protected JToolBar toolBar;
	
	protected JButton btnOpen, 
		btnFullScreen, btnSnapshot,	btnEquiframes, 
		btnSetBasemap, btnAddShape, btnManageLayers, btnExportLineShp,
		btnExportPointShp, btnExportKml,
		btnTrackStyle, btnMobileStyle, 
		btnTrackReport, btnVerticalProfile, btnSlopeProfile, btnSpeedProfile,
		btnHelp, btnAbout;
	
	/**
	 * Regula el estado de activación de los botones a través de las
	 * clases Video...ToolBarState
	 */
	protected ToolBarState state;
	
	
	public ToolBar(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		createButtons();
		int n = toolBar.getComponentCount();
		int width = n*ICON_WIDTH+(n+1)*5;
		toolBar.setPreferredSize(new Dimension(width,ICON_HEIGHT+11));
		
	}
	private void createButtons() {
		
		// MenuFile
		// creado por mainController a través de un action
		btnOpen = createButton(mainWindow.getOpenVideoTrackAction());
		toolBar.add(btnOpen);
		toolBar.addSeparator();
		
		// Menu Video		
		btnSnapshot = createButton(mainWindow.getSnapshotAction());
		toolBar.add(btnSnapshot);
		btnEquiframes = createButton(mainWindow.getExtractFramesAction());
		toolBar.add(btnEquiframes);
		toolBar.addSeparator();		

		// Menu Map
		btnSetBasemap = createButton(mainWindow.getSetBaseMapAction());
		toolBar.add(btnSetBasemap);
		toolBar.addSeparator();		
		
		btnAddShape = createButton(mainWindow.getAddShapefileAction());
		toolBar.add(btnAddShape);

		btnManageLayers = createButton(mainWindow.getManageVectorLayersAction());
		toolBar.add(btnManageLayers);
		toolBar.addSeparator();		
		
		// Track
		btnTrackReport = createButton(mainWindow.getTrackReportAction());
		toolBar.add(btnTrackReport);
		
		btnVerticalProfile = createButton(mainWindow.getVerticalProfileReportAction());
		toolBar.add(btnVerticalProfile);
		
		btnSlopeProfile = createButton(mainWindow.getSlopesReportAction());
		toolBar.add(btnSlopeProfile);

		btnSpeedProfile = createButton(mainWindow.getSpeedReportAction());
		toolBar.add(btnSpeedProfile);

		toolBar.addSeparator();
		
		btnExportLineShp = createButton(mainWindow.getExportAsLineShapefileAction());
		toolBar.add(btnExportLineShp);
		
		btnExportPointShp = createButton(mainWindow.getExportAsPointShapefileAction());
		toolBar.add(btnExportPointShp);

		btnExportKml = createButton(mainWindow.getExportToKmlAction());
		toolBar.add(btnExportKml);

		toolBar.addSeparator();
		
		btnTrackStyle = createButton(mainWindow.getTrackStyleAction());
		toolBar.add(btnTrackStyle);
		
		btnMobileStyle = createButton(mainWindow.getMobileStyleAction());
		toolBar.add(btnMobileStyle);
		toolBar.addSeparator();
		
		// Menu help
		btnHelp = createButton(mainWindow.getHelpAction());
		toolBar.add(btnHelp);
		
		btnAbout = createButton(mainWindow.getAboutAction());
		toolBar.add(btnAbout);

	}
	public void addButton(JButton button, int index) {
		toolBar.add(button, index);
	}
	protected JButton createButton(MainAction action) {
		JButton button = new JButton(action);
		button.setHideActionText(true);
		return button;
	}
	// Getters
	public JToolBar getToolBar() {
		return toolBar;
	}
	public JButton getBtnOpen() {
		return btnOpen;
	}
	public JButton getBtnFullScreen() {
		return btnFullScreen;
	}
	public JButton getBtnSnapshot() {
		return btnSnapshot;
	}
	public JButton getBtnEquiframes() {
		return btnEquiframes;
	}
	public JButton getBtnSetBasemap() {
		return btnSetBasemap;
	}
	public JButton getBtnAddShape() {
		return btnAddShape;
	}
	public JButton getBtnExportLineShp() {
		return btnExportLineShp;
	}
	public JButton getBtnExportPointShp() {
		return btnExportPointShp;
	}
	public JButton getBtnExportKml() {
		return btnExportKml;
	}

	public JButton getBtnTrackStyle() {
		return btnTrackStyle;
	}
	public JButton getBtnMobileStyle() {
		return btnMobileStyle;
	}
	public JButton getBtnTrackReport() {
		return btnTrackReport;
	}
	public JButton getBtnHelp() {
		return btnHelp;
	}
	public JButton getBtnAbout() {
		return btnAbout;
	}
	public ToolBarState getState() {
		return state;
	}
	public void setState(ToolBarState state) {
		this.state = state;
	}
	public JButton getBtnVerticalProfile() {
		return btnVerticalProfile;
	}
	public JButton getBtnSlopeProfile() {
		return btnSlopeProfile;
	}
	public JButton getBtnSpeedProfile() {
		return btnSpeedProfile;
	}
}
