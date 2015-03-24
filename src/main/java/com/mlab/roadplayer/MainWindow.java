package com.mlab.roadplayer;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import com.mlab.roadplayer.swing.MainMenu;
import com.mlab.roadplayer.swing.MessageLevel;
import com.mlab.roadplayer.swing.StatusBar;
import com.mlab.roadplayer.swing.SwingView;
import com.mlab.roadplayer.swing.ToolBar;
import com.mlab.roadplayer.swing.action.AboutAction;
import com.mlab.roadplayer.swing.action.AddShapefileAction;
import com.mlab.roadplayer.swing.action.ConfigAction;
import com.mlab.roadplayer.swing.action.ExitAction;
import com.mlab.roadplayer.swing.action.ExportAsLineShapefileAction;
import com.mlab.roadplayer.swing.action.ExportAsPointShapefileAction;
import com.mlab.roadplayer.swing.action.ExportToKmlAction;
import com.mlab.roadplayer.swing.action.ExtractFramesAction;
import com.mlab.roadplayer.swing.action.HelpAction;
import com.mlab.roadplayer.swing.action.MainAction;
import com.mlab.roadplayer.swing.action.ManageVectorLayersAction;
import com.mlab.roadplayer.swing.action.MapSnapshotAction;
import com.mlab.roadplayer.swing.action.MobileStyleAction;
import com.mlab.roadplayer.swing.action.OpenVideoTrackAction;
import com.mlab.roadplayer.swing.action.SetBaseMapAction;
import com.mlab.roadplayer.swing.action.SlopesReportAction;
import com.mlab.roadplayer.swing.action.SnapshotAction;
import com.mlab.roadplayer.swing.action.SpeedReportAction;
import com.mlab.roadplayer.swing.action.TrackReportAction;
import com.mlab.roadplayer.swing.action.TrackStyleAction;
import com.mlab.roadplayer.swing.action.VerticalProfileReportAction;
import com.mlab.roadplayer.util.RPUtil;

public class MainWindow extends SwingView implements ActionListener {
	private final Logger LOG = Logger.getLogger(getClass().getName());
	private final int DEFAULT_FRAME_WITH = 1250;
	private final int DEFAULT_FRAME_HEIGHT = 700;
	private final int DEFAULT_LOCATION_X = 50;
	private final int DEFAULT_LOCATION_Y = 50;
	private final int MARGIN = 5; // Margen en pixels entre paneles
	//private final double DEFAULT_ASPECT_RATIO = 1.333;
		
	protected MainController controller;
	protected MainAction openVideoTrackAction, exitAction, snapshotAction, 
		extractFramesAction, setBaseMapAction, addShapefileAction,
		manageVectorLayersAction, mapSnapshotAction, trackReportAction,
		verticalProfileReportAction, slopesReportAction, speedReportAction,
		trackStyleAction, mobileStyleAction, exportAsLineShapefileAction,
		exportAsPointShapefileAction, exportToKmlAction, configAction,
		helpAction, aboutAction;
	
	protected JFrame frame;
	protected JPanel mainPanel, contentPanel, leftPanel, rightPanel;
	protected MainMenu mainMenu;
	protected ToolBar toolBar;
	protected StatusBar statusBar;
		
	
	// Constructor
	public MainWindow(MainModel model, MainController controller) {
		super(model);
		LOG.info("Creating main window");
		this.controller = controller;
		createWindow();	
		
		LOG.debug(String.format("Frame width: %d",frame.getWidth()));
	}
	private void createWindow() {
		LOG.debug("MainWindow.createWindow(): creating Frame...");
		createFrame();
		LOG.debug("MainWindow.createWindow(): creating Actions...");
		createActions();
		LOG.debug("MainWindow.createWindow(): creating Menu...");
		createMenu();
		LOG.debug("MainWindow.createWindow(): creating MainPanel...");
		createMainPanel();
		LOG.debug("MainWindow.createWindow(): creating ToolBar...");
		createToolBar();
		LOG.debug("MainWindow.createWindow(): creating ContentPanel...");
		createContentPanel();
		LOG.debug("MainWindow.createWindow(): creating StatusBar...");
		createStatusBar();		

		frame.getContentPane().add(mainPanel);
		
		//frame.invalidate();
		//frame.repaint();
		//frame.pack();
		
	}
	public void maximizeWindow() {
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	private void createFrame() {
		frame = new JFrame("Road Player");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setResizable(false);
		frame.setSize(DEFAULT_FRAME_WITH, DEFAULT_FRAME_HEIGHT);
		frame.setLocation(DEFAULT_LOCATION_X, DEFAULT_LOCATION_Y);
		frame.setIconImage(RPUtil.createImageIcon("roadicon.png", 48, 48).getImage());
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LOG.info("RPMainWindow.windowClosing()");	
				if(controller != null) {
					controller.release();
				}
				Main.savePreferences();
			}
			@Override
			public void windowClosed(WindowEvent e) {
				LOG.info("RPMainWindow.windowClosed()");			
			}		
		});
		frame.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				//LOG.info("RPMainWindow.componentShow()");	
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				//LOG.info("RPMainWindow.componentResized()");	
				if(controller != null) {
					controller.windowResized();
				}
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				//LOG.info("RPMainWindow.componentMoved()");	
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				//LOG.info("RPMainWindow.componentHiden()");	
				
			}
		});

	}
	private void createMainPanel() {
		mainPanel = new JPanel(new BorderLayout());	
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}
	private void createContentPanel() {
		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));
		leftPanel = new JPanel();
		contentPanel.add(leftPanel);		
		rightPanel=new JPanel();
		contentPanel.add(rightPanel);		
		
		mainPanel.add(contentPanel, BorderLayout.CENTER);		
	}
	private void createActions() {
		exitAction = new ExitAction(this);
		openVideoTrackAction = new OpenVideoTrackAction(this);
		snapshotAction = new SnapshotAction(this);
		extractFramesAction = new ExtractFramesAction(this);
		setBaseMapAction = new SetBaseMapAction(this);
		addShapefileAction = new AddShapefileAction(this);
		manageVectorLayersAction = new ManageVectorLayersAction(this);
		mapSnapshotAction = new MapSnapshotAction(this);
		trackReportAction = new TrackReportAction(this);
		verticalProfileReportAction = new VerticalProfileReportAction(this);
		slopesReportAction = new SlopesReportAction(this);
		speedReportAction = new SpeedReportAction(this);
		trackStyleAction = new TrackStyleAction(this);
		mobileStyleAction = new MobileStyleAction(this);
		exportAsLineShapefileAction = new ExportAsLineShapefileAction(this);
		exportAsPointShapefileAction = new ExportAsPointShapefileAction(this);
		exportToKmlAction = new ExportToKmlAction(this);
		helpAction = new HelpAction(this);
		aboutAction = new AboutAction(this);
		configAction = new ConfigAction(this);
	}
	private void createMenu() {
		mainMenu = new MainMenu(this);
		frame.setJMenuBar(mainMenu.getMenuBar());
	}
	private void createToolBar() {
		toolBar = new ToolBar(this);
		mainPanel.add(toolBar.getToolBar(), BorderLayout.NORTH);
	}
	private void createStatusBar() {
		statusBar = new StatusBar(controller);
		mainPanel.add(statusBar.getStatusPanel(), BorderLayout.SOUTH);
	}
	
	// Utility methods
	public int getColumnWidth() {
		// margin - videoPanel - margin - jmapPane - margin
		int columnWidth = (frame.getWidth() - MARGIN)/2;
		return columnWidth;
	}
	public int getContentWidth() {
		return frame.getWidth();
	}
	public int getContentHeight() {
		return frame.getHeight();
	}
	private int calculateCentralSpace() {
		int framewidth = frame.getWidth();
		int centralSpace = framewidth-2*getColumnWidth();
		return centralSpace;
	}
	

	// Interface SwingView
	@Override
	public JPanel getPanel() {
		// TODO Auto-generated method stub
		return contentPanel;
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	// Alerts and notifications
	public void showStatusBarMessage(int messageLevel, String title, String message) {
		this.statusBar.showMessage(messageLevel, title, message);
	}
	public void showAlertDialogMessage(int messageLevel, String title, String message) {
		JOptionPane.showMessageDialog(frame, message, title, messageLevel);
	}
	public void showAlertDialogMessage(int messageLevel, String title, String message, boolean showInStatusBar) {
		showAlertDialogMessage(messageLevel,title, message);
		if(showInStatusBar) {
			
		}
	}
	public void notImplemented(String boxTitle) {
		JOptionPane.showMessageDialog(frame, "Opción en desarrollo", boxTitle,
				JOptionPane.INFORMATION_MESSAGE);
	}
	public File showOpenFileDialog(FileNameExtensionFilter filter, String dialogTitle, String btnOkText) {
		return RPUtil.showOpenFileDialog(frame,filter, dialogTitle, btnOkText);
	}
	// Menu Options
	private void exit() {
		WindowEvent closingEvent = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);	
	}
	private void openVideoTrack() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, "Abrir VideoTrack","");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Ficheros de vídeo mp4","mp4");
		String dialogTitle = "Abrir VideoTrack";
		String btnOkText = "Abrir";
		File result = RPUtil.showOpenFileDialog(frame,filter, dialogTitle, btnOkText);		
		if(result != null) {
			controller.openVideoTrack(result);
		} else {
			showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, "Abrir VideoTrack",": acción cancelada");
		}		
	}
	
	private void videoSnapshot() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, "Guardar fotograma (Snapshot)","");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Ficheros de imagen jpg","jpg");
		String dialogTitle = "Guardar fotograma (Snapshot)";
		String btnOkText = "Grabar";
		File result = RPUtil.showOpenFileDialog(frame,filter, dialogTitle, btnOkText);		
		if(result != null) {
			controller.videoSnapshot(result);
		} else {
			showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, "Guardar fotograma (Snapshot)",": acción cancelada");
		}
	}
	private void extractFrames() {
		LOG.debug("extractFrames()");
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, "Extraer fotogramas de puntos equiespaciados","");
		controller.extractFrames();
	}
	private void addShapefile() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, "Añadir capa shapefile shp", "");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Ficheros shapefile shp","shp");
		String dialogTitle = "Añadir capa shapefile shp";
		String btnOkText = "Abrir";
		File selectedFile = RPUtil.showOpenFileDialog(frame,filter, dialogTitle, btnOkText);		
		if(selectedFile == null) {
			showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, dialogTitle,": cancelado");
			return;
		}
		controller.addShapefile(selectedFile);
	}
	
	private void manageVectorLayers() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, "Configurar capas vectoriales","");
		controller.manageVectorLayers();
	}
	private void mapSnapshot() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, "Guardar imagen del mapa (Map Snapshot)","");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Ficheros de imagen jpg","jpg");
		String dialogTitle = "Guardar imagen del mapa (Map Snapshot)";
		String btnOkText = "Grabar";
		File result = RPUtil.showOpenFileDialog(frame,filter, dialogTitle, btnOkText);		
		if(result != null) {
			controller.mapSnapshot(result);
		} else {
			showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, "Guardar imagen del mapa (Map Snapshot)",": acción cancelada");
		}
	}
	private void exportTrackAsKml() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,"Guardar track en formato KML","");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Ficheros KML","kml");
		String dialogTitle = "Guardar track en formato KML";
		String btnOkText = "Grabar";
		File result = RPUtil.showOpenFileDialog(frame,filter, dialogTitle, btnOkText);		
		if(result != null) {
			controller.exportTrackAsKml(result);
		} else {
			showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, 
				"Guardar track en formato KML","cancelado");
		}
	}
	private void exportTrackAsLineShapefile() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,"Guardar track en formato SHP de línea","");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Ficheros SHP","shp");
		String dialogTitle = "Guardar track en formato SHP de línea";
		String btnOkText = "Grabar";
		File result = RPUtil.showOpenFileDialog(frame,filter, dialogTitle, btnOkText);		
		if(result != null) {
			controller.exportTrackAsLineShapefile(result);
		} else {
			showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, dialogTitle,"cancelado");
		}
	}
	private void exportTrackAsPointsShapefile() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,"Guardar track en formato SHP de puntos","");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Ficheros SHP","shp");
		String dialogTitle = "Guardar track en formato SHP de puntos";
		String btnOkText = "Grabar";
		File result = RPUtil.showOpenFileDialog(frame,filter, dialogTitle, btnOkText);		
		if(result != null) {
			controller.exportTrackAsPointsShapefile(result);
		} else {
			showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, dialogTitle,"cancelado");
		}
	}
	private void trackReport() {
		controller.trackReport();
	}
	private void trackVerticalProfile() {
		controller.trackVerticalProfile();
	}
	private void trackSlopes() {
		controller.trackSlopes();
	}
	private void trackVelocityProfile() {
		controller.trackVelocityProfile();
	}
	private void trackStyle() {
		controller.trackStyle();
	}
	private void mobileStyle() {
		controller.mobileStyle();
	}
	// 
	private void help() {		
		controller.help();
	}
	private void about() {		
		controller.about();
	}
	private void config() {		
		controller.config();
	}

	// Getters
	public JPanel getContentPanel() {
		return contentPanel;
	}
	public JFrame getFrame() {
		return frame;
	}
	public JPanel getLeftPanel() {
		return leftPanel;
	}
	public JPanel getRightPanel() {
		return rightPanel;
	}
	public void setPanels(JPanel newLeftPanel, JPanel newRightPanel) {
		this.contentPanel.removeAll();
		this.contentPanel.add(newLeftPanel);
		contentPanel.add(Box.createHorizontalStrut(calculateCentralSpace()));
		this.contentPanel.add(newRightPanel);

		this.frame.invalidate();
		this.frame.repaint();
		this.frame.pack();		
		
	}
	public void setOnePanel(JPanel newPanel) {
		this.contentPanel.removeAll();
		this.contentPanel.add(newPanel);

		this.frame.invalidate();
		this.frame.repaint();
		//this.frame.pack();		
		
	}
	public MainMenu getMainMenu() {
		return mainMenu;
	}
	public ToolBar getToolBar() {
		return toolBar;
	}

	// Interface ActionListener
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "OPEN_VIDEOTRACK") {
			openVideoTrack();			
		} else if (e.getActionCommand() == "EXIT") {
			exit();
		} else if (e.getActionCommand() == "SNAPSHOT") {
			this.videoSnapshot();
		} else if (e.getActionCommand() == "EXTRACT_FRAMES") {
			this.extractFrames();
		} else if (e.getActionCommand() == "VIDEO_REPORT") {
			this.notImplemented(e.getActionCommand());
		} else if (e.getActionCommand()=="SET_BASEMAP") {
			this.notImplemented(e.getActionCommand());
		} else if (e.getActionCommand()=="ADD_SHAPEFILE") {
			this.addShapefile();
		} else if (e.getActionCommand()=="MANAGE_VECTOR_LAYERS") {
			this.manageVectorLayers();
		} else if (e.getActionCommand() == "MAP_SNAPSHOT") {
			this.mapSnapshot();
		} else if (e.getActionCommand() == "RESET_MAP") {
			//this.controller.resetMap();
		} else if (e.getActionCommand()== "TRACK_STYLE") {
			trackStyle();
		} else if (e.getActionCommand()== "MOBILE_STYLE") {
			mobileStyle();
		} else if (e.getActionCommand()== "TRACK_REPORT") {
			trackReport();
		} else if (e.getActionCommand()== "TRACK_VERTICALPROFILE") {
			trackVerticalProfile();
		} else if (e.getActionCommand()== "TRACK_SLOPES") {
			trackSlopes();
		} else if (e.getActionCommand()== "TRACK_VELOCITYPROFILE") {
			trackVelocityProfile();			
		} else if (e.getActionCommand()== "EXPORT_TRACK_TO_LINE_SHAPEFILE") {
			this.exportTrackAsLineShapefile();
		} else if (e.getActionCommand()== "EXPORT_TRACK_TO_POINTS_SHAPEFILE") {
			this.exportTrackAsPointsShapefile();
		} else if (e.getActionCommand()== "EXPORT_TRACK_TO_KML") {
			this.exportTrackAsKml();
		} else if (e.getActionCommand() == "CONFIG") {
			config();			
		} else if (e.getActionCommand() == "HELP") {
			help();			
		} else if (e.getActionCommand() == "ABOUT") {
			about();
		} 
	}
	public MainAction getExitAction() {
		return exitAction;
	}
	public MainAction getOpenVideoTrackAction() {
		return openVideoTrackAction;
	}
	public MainAction getSnapshotAction() {
		return snapshotAction;
	}
	public MainAction getExtractFramesAction() {
		return extractFramesAction;
	}
	public MainAction getSetBaseMapAction() {
		return setBaseMapAction;
	}
	public MainAction getAddShapefileAction() {
		return addShapefileAction;
	}
	public MainAction getManageVectorLayersAction() {
		return manageVectorLayersAction;
	}
	public MainAction getMapSnapshotAction() {
		return mapSnapshotAction;
	}
	public MainAction getTrackReportAction() {
		return trackReportAction;
	}
	public MainAction getVerticalProfileReportAction() {
		return verticalProfileReportAction;
	}
	public MainAction getSlopesReportAction() {
		return slopesReportAction;
	}
	public MainAction getSpeedReportAction() {
		return speedReportAction;
	}
	public MainAction getTrackStyleAction() {
		return trackStyleAction;
	}
	public MainAction getMobileStyleAction() {
		return mobileStyleAction;
	}
	public MainAction getExportAsLineShapefileAction() {
		return exportAsLineShapefileAction;
	}
	public MainAction getExportAsPointShapefileAction() {
		return exportAsPointShapefileAction;
	}
	public MainAction getExportToKmlAction() {
		return exportToKmlAction;
	}
	public MainAction getConfigAction() {
		return configAction;
	}
	public MainAction getHelpAction() {
		return helpAction;
	}
	public MainAction getAboutAction() {
		return aboutAction;
	}

	
}
