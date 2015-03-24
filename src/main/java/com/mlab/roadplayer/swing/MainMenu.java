package com.mlab.roadplayer.swing;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.mlab.roadplayer.MainWindow;
import com.mlab.roadplayer.swing.action.MainAction;

public class MainMenu {
	
	protected MainWindow mainWindow;
	protected JMenuBar menuBar;
	protected JMenu menuFile;
	protected JMenuItem miOpenVideoTrack, miExit;

	protected JMenu menuVideo;
	protected JMenuItem miSnapshot, miExtractFrames;

	protected JMenu menuMap;
	protected JMenuItem miSetBaseMap, miAddShapefile, miManageVectorLayers, 
		miMapSnapshot, miResetMap;
	
	protected JMenu menuTrack;
	
	protected JMenuItem miTrackReport, miTrackVerticalProfile, miTrackSlopes, 
		miTrackVelocityProfile,	miExportTrackAsLineShapefile, 
		miExportTrackAsPointsShapefile, miExportTrackAsKml, miTrackStyle, 
		miMobileStyle;

	protected JMenu menuHelp;
	protected JMenuItem miHelp, miConfig, miAbout;
	
	protected MenuState state;
	// Constructor
	public MainMenu(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		menuBar = new JMenuBar();
		buildMenuFile();
		buildMenuVideo();
		buildMenuMap();
		buildMenuTrack();
		buildMenuHelp();
	}
	
	private void buildMenuFile() {
		menuFile = new JMenu("Archivo");
		menuFile.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menuFile);

//      creado por mainController a través de un action 
		miOpenVideoTrack = createMenuItem(mainWindow.getOpenVideoTrackAction());
		menuFile.add(miOpenVideoTrack);

//		miCloseVideoTrack = createMenuItem("Cerrar fichero de VideoTrack", "CLOSE_VIDEOTRACK",
//				"close.png");
//		menuFile.add(miCloseVideoTrack);

		menuFile.addSeparator();
		
		miExit = createMenuItem(mainWindow.getExitAction()); 
			//KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuFile.add(miExit);
	}
	private void buildMenuVideo() {
		menuVideo = new JMenu("Vídeo");
		menuVideo.setMnemonic(KeyEvent.VK_V);
		menuBar.add(menuVideo);
		
		miExtractFrames = createMenuItem(mainWindow.getExtractFramesAction());
		menuVideo.add(miExtractFrames);
		
		miSnapshot = createMenuItem(mainWindow.getSnapshotAction());
		menuVideo.add(miSnapshot);
				
	}
	private void buildMenuMap() {
		menuMap = new JMenu("Mapa");
		menuMap.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menuMap);

		miSetBaseMap = createMenuItem(mainWindow.getSetBaseMapAction());
		menuMap.add(miSetBaseMap);

		menuMap.addSeparator();

		miAddShapefile = createMenuItem(mainWindow.getAddShapefileAction());
		menuMap.add(miAddShapefile);
		
		miManageVectorLayers = createMenuItem(mainWindow.getManageVectorLayersAction());
		menuMap.add(miManageVectorLayers);

		menuMap.addSeparator();
		
		miMapSnapshot = createMenuItem(mainWindow.getMapSnapshotAction());
			menuMap.add(miMapSnapshot);
	}
	private void buildMenuTrack() {
		menuTrack = new JMenu("Track");
		menuTrack.setMnemonic(KeyEvent.VK_T);
		menuBar.add(menuTrack);
						
		miTrackReport = createMenuItem(mainWindow.getTrackReportAction());
		menuTrack.add(miTrackReport);
		
		miTrackVerticalProfile = createMenuItem(mainWindow.getVerticalProfileReportAction());
		menuTrack.add(miTrackVerticalProfile);
		
		miTrackSlopes = createMenuItem(mainWindow.getSlopesReportAction());
		menuTrack.add(miTrackSlopes);
		
		miTrackVelocityProfile = createMenuItem(mainWindow.getSpeedReportAction());
		menuTrack.add(miTrackVelocityProfile);
		
		menuTrack.addSeparator();

		miTrackStyle = createMenuItem(mainWindow.getTrackStyleAction());
		menuTrack.add(miTrackStyle);

		miMobileStyle = createMenuItem(mainWindow.getMobileStyleAction());
		menuTrack.add(miMobileStyle);		
	
		menuTrack.addSeparator();
	
		miExportTrackAsLineShapefile = createMenuItem(mainWindow.getExportAsLineShapefileAction());
		menuTrack.add(miExportTrackAsLineShapefile);

		miExportTrackAsPointsShapefile = createMenuItem(mainWindow.getExportAsPointShapefileAction());
		menuTrack.add(miExportTrackAsPointsShapefile);

		miExportTrackAsKml = createMenuItem(mainWindow.getExportToKmlAction());
				menuTrack.add(miExportTrackAsKml);

	}
	private void buildMenuHelp() {
		menuHelp = new JMenu("Ayuda");
		menuHelp.setMnemonic(KeyEvent.VK_Y);
		menuBar.add(menuHelp);

		miConfig = createMenuItem(mainWindow.getConfigAction());
		menuHelp.add(miConfig);

		miHelp = createMenuItem(mainWindow.getHelpAction());
		menuHelp.add(miHelp);

		miAbout = createMenuItem(mainWindow.getAboutAction());
		menuHelp.add(miAbout);

	}

	
	
	private JMenuItem createMenuItem(MainAction action) {
		JMenuItem menuitem = new JMenuItem(action);
		return menuitem;
	}
	// SetState
	public void setState(MenuState state) {
		this.state = state;
	}
	// Getters	
	public MenuState getState() {
		return this.state;
	}
	public JMenuBar getMenuBar() {
		return menuBar;
	}
	public JMenuItem getMiOpenVideoTrack() {
		return miOpenVideoTrack;
	}
	
	public JMenuItem getMiExit() {
		return miExit;
	}
	
	// Video
	public JMenuItem getMiSnapshot() {
		return miSnapshot;
	}
	public JMenuItem getMiExtractFrames() {
		return miExtractFrames;
	}	
	
	// Mapa
	public JMenuItem getMiMapSnapshot() {
		return miMapSnapshot;
	}
	

	public JMenuItem getMiSetBaseMap() {
		return miSetBaseMap;
	}
	public JMenuItem getMiTrackStyle() {
		return miTrackStyle;
	}
	public JMenuItem getMiMobileStyle() {
		return miMobileStyle;
	}
	public JMenuItem getMiTrackReport() {
		return miTrackReport;
	}
	public JMenuItem getMiExporTrackAsLineShapefile() {
		return miExportTrackAsLineShapefile;
	}
	public JMenuItem getMiExporTrackAsPointsShapefile() {
		return miExportTrackAsPointsShapefile;
	}
	public JMenuItem getMiAddShapefile() {
		return miAddShapefile;
	}
	public JMenuItem getMiManageVectorLayers() {
		return this.miManageVectorLayers;
	}
	public JMenuItem getMiHelp() {
		return miHelp;
	}
	public JMenuItem getMiAbout() {
		return miAbout;
	}
	public JMenuItem getMiResetMap() {
		return miResetMap;
	}
	public JMenuItem getMiExportTrackAsKml() {
		return miExportTrackAsKml;
	}

	public JMenuItem getMiTrackVerticalProfile() {
		return miTrackVerticalProfile;
	}

	public JMenuItem getMiTrackVelocityProfile() {
		return miTrackVelocityProfile;
	}

	public JMenuItem getMiTrackSlopes() {
		return miTrackSlopes;
	}

	public JMenuItem getMiConfig() {
		return miConfig;
	}

	public JMenu getMenuFile() {
		return menuFile;
	}

	public JMenu getMenuVideo() {
		return menuVideo;
	}

	public JMenu getMenuMap() {
		return menuMap;
	}

	public JMenu getMenuTrack() {
		return menuTrack;
	}

	public JMenu getMenuHelp() {
		return menuHelp;
	}

	
}
