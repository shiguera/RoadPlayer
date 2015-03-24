package com.mlab.roadplayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.map.Layer;
import org.geotools.map.StyleLayer;
import org.geotools.styling.Style;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.operation.MathTransform;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

import com.mlab.exifutil.ExifUtil;
import com.mlab.gpx.api.GpxFactory;
import com.mlab.gpx.api.GpxFactory.Type;
import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.TrackSegment;
import com.mlab.gpx.impl.kml.Gpx2KmlFactory;
import com.mlab.gpx.impl.util.Util;
import com.mlab.map.ReferencedSegment;
import com.mlab.map.TrackMap;
import com.mlab.map.TrackMapModel;
import com.mlab.map.factory.GeoToolsFactory;
import com.mlab.map.factory.StyleFac;
import com.mlab.map.layer.ShpLayer;
import com.mlab.map.swing.MapToolBar;
import com.mlab.map.swing.MapToolBarImpl;
import com.mlab.map.swing.MapView;
import com.mlab.map.swing.SingleMapView;
import com.mlab.patterns.Observable;
import com.mlab.roadplayer.swing.AltitudePanel;
import com.mlab.roadplayer.swing.Controller;
import com.mlab.roadplayer.swing.InfoPanel;
import com.mlab.roadplayer.swing.MenuState;
import com.mlab.roadplayer.swing.MessageLevel;
import com.mlab.roadplayer.swing.ToolBarState;
import com.mlab.roadplayer.swing.VideoOffMenuState;
import com.mlab.roadplayer.swing.VideoOffToolBarState;
import com.mlab.roadplayer.swing.VideoOnMenuState;
import com.mlab.roadplayer.swing.VideoOnToolBarState;
import com.mlab.roadplayer.swing.VideoPlayingMenuState;
import com.mlab.roadplayer.swing.VideoPlayingToolBarState;
import com.mlab.roadplayer.swing.dialogs.AboutDialog;
import com.mlab.roadplayer.swing.dialogs.ConfigDialog;
import com.mlab.roadplayer.swing.dialogs.FrameExtractorDialog;
import com.mlab.roadplayer.swing.dialogs.HelpDialog;
import com.mlab.roadplayer.swing.dialogs.ManageLayersDialog2;
import com.mlab.roadplayer.swing.dialogs.SlopesDialog;
import com.mlab.roadplayer.swing.dialogs.SpeedsDialog;
import com.mlab.roadplayer.swing.dialogs.TrackReportDialog;
import com.mlab.roadplayer.swing.dialogs.VerticalProfileDialog;
import com.mlab.roadplayer.synchrovideo.MapUpdater;
import com.mlab.roadplayer.synchrovideo.SynchroVideoAction;
import com.mlab.roadplayer.synchrovideo.SynchroVideoToolListener;
import com.mlab.roadplayer.util.RPUtil;
import com.mlab.roadplayer.util.TrackSegmentFile;
import com.mlab.roadplayer.video.MediaPlayerEventListenerImpl;
import com.mlab.roadplayer.video.RPVideoView;
import com.mlab.roadplayer.video.VideoController;
import com.mlab.roadplayer.video.VideoControllerImpl;
import com.mlab.roadplayer.video.VideoFile;
import com.mlab.roadplayer.video.VideoModel;
import com.mlab.roadplayer.video.VideoTrack;
import com.mlab.roadplayer.video.VideoTrackImpl;
import com.mlab.roadplayer.video.VideoView;
import com.vividsolutions.jts.linearref.LinearLocation;

import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

public class MainController implements SynchroVideoToolListener {
	private final Logger LOG = Logger.getLogger(getClass().getName());

	private final String DEFAULT_CARTO_DIRECTORY = "carto";
	private final String DEFAULT_VIDEO_DIRECTORY = "video";
	//private final String DEFAULT_VIDEO_DIRECTORY = "/home/shiguera/Documents/tesis/data";
	private final double DEFAULT_ASPECT_RATIO = 1.333;


	protected MainWindow mainWindow;
	protected MainModel model;
	protected TrackMap trackMap;
	protected VideoController videoController;
	/**
	 * MapUpdater es la clase encargada de actualizar en el mapa
	 * la posición del móvil en base a la evolución del vídeo
	 */
	protected MapUpdater mapUpdater;
	
	/**
	 * Sobreescribe los métodos playing(), paused() y finished() que 
	 * utiliza para arrancar y parar el MapUpdater;
	 */
	protected MediaPlayerEventListener innerMediaPlayerEventListener;
	
	protected GpxFactory gpxFactory;
		
	protected boolean isEnabled;
	
	private JPanel contentPanel, rightPanel, leftPanel;
	private InfoPanel infoPanel;
	private AltitudePanel altitudePanel;
	
	// Constructor
	public MainController() {
		
		isEnabled = false;
		LOG.debug("MainController(): isEnabled=" + isEnabled);
		
		LOG.debug("MainController(): Creating MainModel...");
		model = new MainModel();
		
		// GpxFactory
		LOG.debug("MainController(): Creating GpxFactory...");
		gpxFactory = GpxFactory.getFactory(Type.ExtendedGpxFactory);
		
		// Video
//		VideoContextCreator vcreator = new VideoContextCreator();
//		vcreator.execute();
		try {
			//vcreator.get();
			//LOG.debug("vcreator.get()");
			LOG.debug("MainController(): creating video context...");
			initVideo();
		} catch(Exception e) {
			LOG.error("MainController() Error in VideoContextCreator "+e.getMessage());
			isEnabled = false;
			return;
		}
		
		// Map
		LOG.debug("MainController(): creating trackMap context");
		MapContextCreator mcreator = new MapContextCreator(this);
		mcreator.execute();
		try {
			LOG.debug("MainController(): creating trackMap context");
			mcreator.get();			
		} catch(Exception e) {
			LOG.error("MainController() Error in MapContextCreator "+e.getMessage());
			isEnabled = false;
			return;
		}
		
		// MainWindow
		LOG.debug("MainController(): creating MainWindow...");
		mainWindow = new MainWindow(model, this);
		
		LOG.debug("MainController(): creating InfoPanel...");
		infoPanel = new InfoPanel(this);
		
		LOG.debug("MainController(): creating AltitudePanel...");
		altitudePanel = new AltitudePanel();
		
		LOG.debug("MainController(): creating ContentPanel...");
		createContentPanel();	
		
		/**
		 * Primera prueba de implementación de actions
		 */
//		OpenVideoTrackAction action = new OpenVideoTrackAction(this); 
//		JMenuItem mi = new JMenuItem(action);
//		mainWindow.getMainMenu().getMenuFile().add(mi,0);
//		JButton button = new JButton(action);
//		button.setText("");
//		mainWindow.getToolBar().addButton(button, 0);
		
		
		isEnabled = true;
		LOG.debug("MainController(): isEnabled=" + isEnabled);
	}
	public boolean isEnabled() {
		return this.isEnabled;
	}
	private void createContentPanel() {
		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));
		//contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		leftPanel = new JPanel();
		if(videoController!=null) {
			leftPanel.add(videoController.getVideoView().getPanel());
		}
		contentPanel.add(leftPanel);
		
		contentPanel.add(Box.createRigidArea(new Dimension(5,3)));
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		rightPanel.add(Box.createRigidArea(new Dimension(5,5)));
		if(trackMap!=null) {
			rightPanel.add(trackMap.getView().getMainPanel());
		}
		rightPanel.add(Box.createRigidArea(new Dimension(5,5)));
		rightPanel.add(altitudePanel.getPanel());
		rightPanel.add(Box.createRigidArea(new Dimension(5,5)));
		rightPanel.add(infoPanel.getPanel());
		rightPanel.add(Box.createVerticalGlue());
		contentPanel.add(rightPanel);
	}

//	class VideoContextCreator extends SwingWorker<String, String> {
//		@Override
//		protected String doInBackground() throws Exception {
//			LOG.info("VideoContextCreator.doInBackground()");
//			initVideo();
//			return "";
//		}
//	}
	private void initVideo() {
		VideoModel videoModel = new VideoModel();
		innerMediaPlayerEventListener = new InnerMediaPlayerEventListener();
		videoModel.addMediaPlayerEventListener(innerMediaPlayerEventListener);
		videoController = new VideoControllerImpl(videoModel);
		VideoView view = new RPVideoView(videoModel, videoController);
		videoController.setVideoView(view);
	}
	class MapContextCreator extends SwingWorker<Void, Void> {
		SynchroVideoToolListener controller;
		MapContextCreator(SynchroVideoToolListener controller) {
			this.controller = controller;
		}
		@Override
		protected Void doInBackground() throws Exception {
			LOG.info("MapContextCreator.doInBackground() :" + controller);

			TrackMapModel mapmodel = new TrackMapModel();
			MapView mapview = new SingleMapView(mapmodel);
			trackMap = new TrackMap(mapmodel, mapview);
			MapToolBar toolbar = new MapToolBarImpl();
			toolbar.setDefaultButtons(trackMap, mapview.getJMapPane());
			JButton	videoButton = new JButton(new SynchroVideoAction(trackMap, MainController.this));
			toolbar.addButton(videoButton);		
			mapview.setMapToolBar(toolbar);
			return null;
		}
		@Override
		protected void done() {
			trackMap.zoomSpain();
			super.done();
		}
	}
	public void postInit() {
		LOG.info("postInit()");
		setMainWindowVisible(true, true);
		mainWindow.getContentPanel().removeAll();
		mainWindow.getContentPanel().add(contentPanel);
		setVideoOffState();	
		//setDefaultVectorLayers();
	}
	// State
	private void setVideoOffState() {
		MenuState menuState = new VideoOffMenuState(mainWindow.getMainMenu());
		menuState.doAction();
		
		ToolBarState toolBarState = new VideoOffToolBarState(mainWindow.getToolBar());
		toolBarState.doAction();

		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
			"VideoTrack "+ getVideoFileName(), " Detenido");
	}
	private void setVideoOnState() {
		MenuState menuState = new VideoOnMenuState(mainWindow.getMainMenu());
		menuState.doAction();

		ToolBarState toolBarState = new VideoOnToolBarState(mainWindow.getToolBar());
		toolBarState.doAction();

		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
			"VideoTrack "+ getVideoFileName(), "Preparado");

	}
	private void setVideoPlayingState() {
		MenuState menuState = new VideoPlayingMenuState(mainWindow.getMainMenu());
		menuState.doAction();

		ToolBarState toolBarState = new VideoPlayingToolBarState(mainWindow.getToolBar());
		toolBarState.doAction();
		
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
			"VideoTrack "+ getVideoFileName(), "Reproduciendo");

	}
	
	public void release() {
		this.videoController.release();
		this.trackMap.release();
	}
	
	public String getCartoDirectory() {
		return DEFAULT_CARTO_DIRECTORY;
	}
	public String getVideoDirectory() {
		return DEFAULT_VIDEO_DIRECTORY;
	}
	private boolean existsCartoDirectory() {
		LOG.info("Constants.cartoDirectory: "+ Constants.cartoDirectory);
		
		File cartodir = new File(Constants.cartoDirectory);
		if( !cartodir.exists() || !cartodir.isDirectory()) {
			LOG.error("Can't locate carto directory "+cartodir.getName());
			return false;		
		} 
		LOG.debug("existsCartoDirectory: "+ cartodir.getPath());
		return true;
	}
	private void setDefaultVectorLayers() {
		LOG.debug("Setting default vector layer");
		if(!existsCartoDirectory()) {
			LOG.error("No se encuentra el directorio de recursos: cartografía ");
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, "Setting default layers",
				"No se encuentra el directorio de recursos la cartografía ", true);
			return;
		}
		File shpfile = new File("carto/world_borders_80lat_polygon.shp");
		if(shpfile == null || !shpfile.isFile() || !shpfile.exists()) {
			LOG.error("No se encuentra recurso: capa de países del mundo " + shpfile.getPath());
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, "Setting default layers",
				"No se encuentra recurso: capa de países del mundo " + shpfile.getPath(), true);
			return;
		}		
		Style style = StyleFac.createPolygonStyle(Color.GRAY, 1);
		
		ShpLayer shplayer = GeoToolsFactory.createShpLayer(shpfile,style);
		if(shplayer != null) {
			if(!trackMap.addVectorLayer(shplayer)) {
				LOG.error("Can't create resource shape layer");
			}						
		} else {
			LOG.error("Can't create resource shape layer");
		}
	}
	// MainWindow management
	public void setMainWindowVisible(boolean visible, boolean maximize) {
		this.mainWindow.getFrame().setVisible(visible);
		if(maximize) {
			mainWindow.maximizeWindow();
		}
	}
	void windowResized() {
		int width = mainWindow.getContentPanel().getWidth();
		int height = mainWindow.getContentPanel().getHeight();
		int margin = 5;
		int leftColumnWidth = (int)((width - 3*margin)*0.6);
		int videoPanelWidth = leftColumnWidth;
		int videoPanelHeight = (int)( leftColumnWidth / this.DEFAULT_ASPECT_RATIO);
		int videoButtonsHeight = 60;
		if(videoPanelHeight + videoButtonsHeight + margin > height) {
			videoPanelHeight = height - videoButtonsHeight -margin;
			videoPanelWidth = (int)(videoPanelHeight * this.DEFAULT_ASPECT_RATIO);
		}
		if(videoController != null) {
			videoController.setVideoPanelSize(videoPanelWidth, videoPanelHeight);
		}
		int rightColumnWidth = width - 3*margin - leftColumnWidth;
		int mapPanelWidth = rightColumnWidth-10;
		int mapPanelHeight = (int)(mapPanelWidth / this.DEFAULT_ASPECT_RATIO);
		if(trackMap != null) {
			trackMap.getView().setMapPanelSize(mapPanelWidth, mapPanelHeight);
		}
		altitudePanel.getPanel().setPreferredSize(new Dimension(mapPanelWidth, 200));
		//int infoPanelHeight = height - mapPanelHeight;
	}
	
	public void showStatusBarMessage(int messageLevel, String title, String message) {
		mainWindow.showStatusBarMessage(messageLevel, title, message);
	}
	public void showAlertDialog(int messageLevel, String title, String message, boolean showInStatusBar) {
		mainWindow.showAlertDialogMessage(messageLevel, title, message, showInStatusBar);
	}
	public File openFileDialog(FileNameExtensionFilter filter, String dialogTitle, String btnOkText) {
		return mainWindow.showOpenFileDialog(filter, dialogTitle, btnOkText);
	}
	
	// Menu options
	public void openVideoTrack(File file) {
		VideoFile videofile = new VideoFile(file);
		if(!videofile.isValid()) {
			LOG.info("MainController.setVideoTrack() : ERROR not valid videofile");
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_WARNING, 
				"Abrir VideoTrack", "Fichero VideoFile no valido",true);			
			return;
		}
		
		VideoTrack vt = createVideoTrack(videofile);
		if(vt == null || !vt.isValid()) {
			LOG.info("MainController.setVideoTrack() : ERROR not valid videotrack");
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_WARNING, 
				"Abrir VideoTrack", "Fichero VideoTrack no valido",true);			
			return;
		}
		boolean result = model.setVideoTrack(vt);
		if(result) {
			result = videoController.setVideoFile(vt.getVideoFile());
			if(result) {
				result = trackMap.setTrackLayer(vt.getTrackSegmentFile().getFile());
				if(result) {
					altitudePanel.setTrackSegment(vt.getSegment());
					showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
						"Abrir VideoTrack", "Abierto "+videofile.getFile().getName());
					trackMap.zoomTrack();
					updateMobilePosition();
					return;					
				}
			}
		}
		LOG.info("MainController.setVideoTrack() : ERROR not valid videotrack");
		showAlertDialog(MessageLevel.MESSAGE_LEVEL_WARNING, 
				"Abrir VideoTrack", "Fichero VideoTrack no valido",true);			
		return;
	}
	public void addShapefile(File file) {
		LOG.debug("Start addShapefile()");
		String title = "Añadir capa shapefile shp";
		ShpLayer layer = new ShpLayer(file);
		boolean result = false;
		if(layer.getLayer() != null) {
			result = trackMap.addVectorLayer(layer);
			if(result) {
				showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO, 
						title, "Añadida la capa shapefile "+file.getName());
			} else {
				showAlertDialog(MessageLevel.MESSAGE_LEVEL_WARNING, 
					title, "No se pudo añadir la capa shapefile "+file.getName(), true);
				LOG.warn("Can't add vector layer "+file.getName());				
			} 
		} else {
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_WARNING, title, 
				"Layer = null", true);
			LOG.warn("ShpLayer is not valid, layer = null");
		}
	}
	public void manageVectorLayers() {
		System.out.println("MainController.manageVectorLayers()");
		// ManageLayersDialog2 utiliza el MapLayerTable de Geotools
		new ManageLayersDialog2(trackMap, mainWindow.getFrame());		
		
		// ManageLayersDialog utiliza el diálogo hecho por mí
		// new ManageLayersDialog2(trackMap, mainWindow.getFrame());		
	}
	public void videoSnapshot(File destImageFile) {
		BufferedImage image = videoController.takeSnapshot();
		LOG.debug("Snapshot image size:"+image.getWidth()+", "+image.getHeight() );
		try {
			//File tmpfile = File.createTempFile("tmp", ".jpg");
			File directory = destImageFile.getParentFile();
			if(!directory.canWrite()) {
				LOG.error("MainController.videoSnapshot() ERROR: Can't write in directory");
				showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_ERROR,
					    "Guardar snapshot  (VideoSnapshot)","Error, no se puede escribir en el directorio");
				return;
			}
			// Save image without metadata
			String imagefilename = destImageFile.getName();
			boolean result = saveImageWithoutMetadata(image, directory, imagefilename);
			if(!result) {
				LOG.error("MainController.videoSnapshot() ERROR: can't save image without metadata");
		    	showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_ERROR,
					    "Guardar snapshot  (VideoSnapshot)","Se produjeron errores al guardar los ficheros");
		    	return;
		    } 

			WayPoint currentwp = getWayPoint(currentVideoPlayingTime());
		    if(currentwp == null) {
		    	// Save kml file
		    	LOG.error("MainController.videoSnapshot() ERROR: Can't save georreferenced files, wp null");
		    	showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_ERROR,
				    "Guardar snapshot  (VideoSnapshot)","No se pudo guardar los ficheros georreferenciados, wp=null");
		    	return;
		    }
	    
		    // Save kml file
		    result = saveSnapshotKmlFile(currentwp, destImageFile);
		    if(result) {
		    	LOG.debug("MainController.videoSnapshot(): Se guardó el fichero KML ");
		    	showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
				    "Guardar snapshot  (VideoSnapshot)","Se guardó el fichero KML");
		    } else {
		    	LOG.error("MainController.videoSnapshot() ERROR : can't save KML file");			    	
		    	showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_ERROR,
				    "Guardar snapshot  (VideoSnapshot)","No se pudo guardar el fichero KML");		    				    	
		    }
	    
		    
		    // Save image with metadata
		    if(currentwp != null) {
		    	imagefilename = Util.fileNameWithoutExtension(destImageFile)+"-tagged.jpg";
		    	File destinationfile = new File(directory, imagefilename);
			    result = saveImageWithMetadata(destImageFile, destinationfile, currentwp);
			    if(result) {
				    LOG.debug("MainController.videoSnapshot() saved image with metadata "+destinationfile.getPath());
			    } else {
			    	LOG.error("MainController.videoSnapshot() ERROR: Can't save image with metadata "+destinationfile.getPath());
			    	showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_ERROR,
			    	"Guardar snapshot  (VideoSnapshot)","No se pudo guardar el fichero "+destinationfile.getName());		    	
			    }
		    
		    }
		} catch (IOException e) {
		   LOG.error("MainController.videoSnapshot()");
		   showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, 
				   "Guardar snapshot (VideoSnapshot)","Se produjeron errores al guardar los ficheros",true);
		}	
		
	}
	private boolean saveImageWithoutMetadata(BufferedImage image, File directory, String imagefilename) throws IOException {
		File tmpfile = new File(directory, imagefilename);
		//tmpfile.setWritable(true);
		LOG.debug("saveImageWithoutMetadata() tmpfile="+tmpfile.getPath());
		boolean result = ImageIO.write(image, "jpg", tmpfile);
		return result;
	}
	private boolean saveSnapshotKmlFile(WayPoint currentwp, File destImageFile) {
		Placemark placemark = Gpx2KmlFactory.createPlacemark(currentwp.getName(), 
    			currentwp.getDescription(), currentwp.getLongitude(), 
    			currentwp.getLatitude(), currentwp.getAltitude()); 
	    Kml kml = Gpx2KmlFactory.createPlaceMarkWithImage(placemark	, destImageFile);
	    File kmlfile = new File(destImageFile.getParentFile(), 
	    		Util.fileNameWithoutExtension(destImageFile)+".kml");
	    LOG.debug("videoSnapshot() kmlfile="+kmlfile.getPath());
	    boolean result = Gpx2KmlFactory.writeToFile(kml, kmlfile);
	    return result;
	}
	private boolean saveImageWithMetadata(File jpgfile, File outfile, WayPoint wp) {
		boolean result = false;
		if(jpgfile != null && outfile != null && wp != null) {
			try {
				String date = Util.dateTimeToString(wp.getTime(), true).replace("T", " ");
				LOG.debug("MainController.setExifTag() : date="+date);
				ExifUtil.setExifGPSTagExtended(jpgfile, outfile, date, wp.getLongitude(), 
						wp.getLatitude(), wp.getAltitude());
				result = true;
			} catch (Exception e) {
				LOG.error("setExifTag() ERROR");
			}
		}
		return result;
	}
	public void extractFrames() {
		LOG.debug("extractFrames()");
		
		FrameExtractorLoader executor = new FrameExtractorLoader();
		executor.execute();
		try {
			executor.get();
		} catch (Exception e) {
			LOG.debug("extractFrames() ERROR Failed get FrameExtractorLoader");
		}

		
	}
	public void mapSnapshot(File destImageFile) {
		BufferedImage image = RPUtil.createImage(trackMap.getView().getJMapPane());
		try {
		    ImageIO.write(image, "jpg", destImageFile);
		    showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
		    	"Guardar imagen de mapa (MapSnapshot)","Se guardó el fichero "+destImageFile.getName());
		} catch (IOException e) {
		   LOG.warn("Couldn't save image "+destImageFile.getPath());
		   showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, 
				   "Guardar imagen de mapa (MapSnapshot)","No se pudo grabar el fichero de imagen",true);
		}		
	}
	// Track menu
	public void trackReport() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
				"Información del track",model.getVideoTrack().getVideoFile().getFile().getName());
		if(model.getVideoTrack() !=null && model.getVideoTrack().isValid()) {
			new TrackReportLoader().execute();
		} else {
			LOG.error("No se debería haber llegado aquí");
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, 
					"Información del track", "No hay ningún VideoTrack abierto",true);
		}
	}
	public void trackVerticalProfile() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
				"Perfil longitudinal", model.getVideoTrack().getVideoFile().getFile().getName());
		if(model.getVideoTrack() !=null && model.getVideoTrack().isValid()) {
			new VerticalProfileLoader().execute();			
		} else {
			LOG.error("No se debería haber llegado aquí");
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, 
					"Perfil longitudinal del track", "No hay ningún VideoTrack abierto",true);
		}
	}
	public void trackSlopes() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
				"Gráfico de Pendientes", model.getVideoTrack().getVideoFile().getFile().getName());
		if(model.getVideoTrack() !=null && model.getVideoTrack().isValid()) {
			new SlopesLoader().execute();			
		} else {
			LOG.error("No se debería haber llegado aquí");
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, 
					"Gráfico de pendientes del track", "No hay ningún VideoTrack abierto",true);
		}
	}
	public void trackVelocityProfile() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
				"Gráfico de velocidades", model.getVideoTrack().getVideoFile().getFile().getName());
		if(model.getVideoTrack() !=null && model.getVideoTrack().isValid()) {
			new SpeedsLoader().execute();			
		} else {
			LOG.error("No se debería haber llegado aquí");
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, 
					"Gráfico de velocidades del track", "No hay ningún VideoTrack abierto",true);
		}
	}
	public void trackStyle() {
		if(getTrackLayer() != null) {
			StyleLayer layer = (StyleLayer)getTrackLayer();
			SimpleFeatureType type = (SimpleFeatureType) layer.getFeatureSource().getSchema();
			JSimpleStyleDialog d= this.createStyleDialog(type, layer.getStyle());
			d.setVisible(true);
			if(d.completed()) {
				Constants.trackLineColor = d.getLineColor().getRGB();
				Constants.trackLineWidth = (int) d.getLineWidth();
				Style style = StyleFac.createLineStyle(d.getLineColor(), (int) d.getLineWidth());
				trackMap.setTrackLayerStyle(style);				
			}
		} else {
			// No se debería poder llegar aquí
			LOG.error("No se debería haber llegado aquí");
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, 
				"Estilo de la línea del track", "No hay cargado ningún track", true);
		}

	}
	public void mobileStyle() {
		//System.out.println("RPController.mobileStyle()");
		if(getMobileLayer()!=null) {
			StyleLayer layer = (StyleLayer)getMobileLayer();
			SimpleFeatureType type = (SimpleFeatureType) layer.getFeatureSource().getSchema();
			JSimpleStyleDialog d = this.createStyleDialog(type, layer.getStyle());
			//System.out.println(d.toString());
			d.setVisible(true);
			if(d.completed()) {
				Constants.mobilePointLineColor = d.getLineColor().getRGB();
				Constants.mobilePointLineWidth = (int) d.getLineWidth();
				Constants.mobilePointFillColor = d.getFillColor().getRGB();
				Constants.mobilePointSize = (int) d.getPointSize();
				
				Style style = StyleFac.createPointStyle(d.getLineColor(), (int)d.getLineWidth(),
						d.getFillColor(), (int) d.getPointSize());
				trackMap.setMobileLayerStyle(style);
			}
		} else {
			// No se debería poder llegar aquí
			LOG.error("No se debería haber llegado aquí");
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, 
				"Estilo del punto móvil", "No hay cargado ningún track", true);
		}
	}
	public void exportTrackAsKml(File kmlfile) {
		TrackSegment segment = model.getTrackSegment();
		Kml kml = Gpx2KmlFactory.trackSegmentToKml(segment, getVideoFileName(), true, true);
		if(kml == null) {
			LOG.error("Error al convertir track a kml: kml=null");
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR,
				"Exportar track a KML", "Error al convertir el fichero de track", true);
			return;
		}
		boolean result = Gpx2KmlFactory.writeToFile(kml, kmlfile);
		if(!result) {
			LOG.error("Error al escribir en disco el fichero kml "+kmlfile.getPath());
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, "Exportar track a KML", 
					"Error, no se pudo grabar el fichero "+kmlfile.getPath(), true);
			return;
		}
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
			"Exportar track a KML", "Se guardó el fichero "+kmlfile.getName());
	}
	public void exportTrackAsLineShapefile(File file) {
		TrackSegment segment = model.getTrackSegment();
		boolean result = GeoToolsFactory.saveSegmentAsLineShapefile(file, segment);
		if(!result) {
			LOG.error("Error al escribir en disco el fichero shp "+ file.getPath());
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, "Exportar track a SHP", 
					"Error, no se pudo grabar el fichero " + file.getPath(), true);
			return;			
		}
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
			"Exportar track a SHP", "Se guardó el fichero " + file.getName());
	}
	public void exportTrackAsPointsShapefile(File file) {
		TrackSegment segment = model.getTrackSegment();
		boolean result = GeoToolsFactory.saveSegmentAsPointsShapefile(file, segment);
		if(!result) {
			LOG.error("Error al escribir en disco el fichero shp "+ file.getPath());
			showAlertDialog(MessageLevel.MESSAGE_LEVEL_ERROR, "Exportar track a SHP", 
					"Error, no se pudo grabar el fichero " + file.getPath(), true);
			return;			
		}
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
			"Exportar track a SHP", "Se guardó el fichero " + file.getName());
	}
	// 
	public void config() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
				"Configuración de RoadPlayer", "");
		new ConfigLoader().execute();	
	}
	public void help() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
				"Ayuda de RoadPlayer", "");
		new HelpLoader().execute();	
	}
	public void about() {
		showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_INFO,
				"Acerca de RoadPlayer", "");
		new AboutLoader().execute();	
	}
	
	// Utility methods
	private JSimpleStyleDialog createStyleDialog(SimpleFeatureType type, Style style) {
		JSimpleStyleDialog d = new JSimpleStyleDialog(mainWindow.getFrame(), type, style);
		int w = mainWindow.getFrame().getWidth();
		int h = mainWindow.getFrame().getHeight();
		d.setBounds(w/-500, h/2-400, 500,400);
		d.setLocationRelativeTo(mainWindow.getFrame());
		return d;
	}
	private VideoTrack createVideoTrack(VideoFile videofile) {
		if(!validateVideoFile(videofile)) {
			LOG.warn("MainController.createVideoTrack() : ERROR no valid videofile");
			//showAlertDialog("Abrir VideoTrack", "Fichero de vídeo no valido");
			return null;
		}
		String gpxFilename = Util.fileNameWithoutExtension(videofile.getFile())+".gpx";
		File directory = new File(videofile.getFile().getParentFile().getPath());
		TrackSegmentFile tsfile = new TrackSegmentFile(directory,gpxFilename);
		VideoTrack vt = new VideoTrackImpl(videofile, tsfile);
		return vt;
	}
	private boolean validateVideoFile(VideoFile videofile) {
		if(videofile==null || videofile.exists()==false || !videofile.isValid()) {
			return false;
		}		
		return true;
	}
	private Layer getMobileLayer() {
		return trackMap.getMapModel().getMobileLayer().getLayer();
	}
	private Layer getTrackLayer() {
		return trackMap.getMapModel().getTrackLayer().getTrackLayer(0);
	}
		
	// Getters
	public String getVideoFileName() {
		return ((VideoModel)videoController.getModel()).getVideoFilename();
	}
	public TrackMap getMapController() {
		return trackMap;
	}

	public VideoController getVideoController() {
		return videoController;
	}

	// MapUpdater management
	private long currentVideoPlayingTime() {
		long t = model.getStartTime() + ((VideoModel)videoController.getModel()).getVideoTime();
		return t;
	}
	public WayPoint getWayPoint(long t) {
		TrackSegment segment = model.getTrackSegment();
		double[] values = segment.getValues(t);
		WayPoint wp = gpxFactory.createWayPoint("P", "Point", t, Util.arrayDoublesToList(values));
		return wp;
	}

	public void updateMobilePosition() {
		//System.out.println("RPController.updateMobilePosition()");
		long t = this.currentVideoPlayingTime();		
		WayPoint wp = getWayPoint(t);
		if(!trackMap.getView().isRendering() && wp != null) {
			trackMap.setMobilePositionLatLon(wp);
			//System.out.println("RPController.updateMobilePosition() "+wp.toString());
		} else {
			LOG.debug("RPController.updateMobilePosition() Rendering, no updated");			
		}
		altitudePanel.setPoint(wp);
	}
	private void startMapUpdater() {
		mapUpdater = new MapUpdater(MainController.this);
		mapUpdater.start();
	}
	private void stopMapUpdater() {
		if(mapUpdater!=null) {
			mapUpdater.stop();
		}
	}
	
	// Inner MediaPlayerEventListener
	class InnerMediaPlayerEventListener extends MediaPlayerEventListenerImpl {
		@Override
		public void playing(MediaPlayer mediaPlayer) {
			LOG.debug("RPController listener: Starting MapUpdater");
			setVideoPlayingState();
			startMapUpdater();
		}
		@Override
		public void paused(MediaPlayer mediaPlayer) {
			LOG.debug("RPController listener: Stopping MapUpdater");
			setVideoOnState();
			mapUpdater.stop();
		}
		@Override
		public void stopped(MediaPlayer mediaPlayer) {
			LOG.debug("RPController listener: Stopping MapUpdater");
			setVideoOffState();
			mapUpdater.stop();
		}
		@Override
		public void finished(MediaPlayer mediaPlayer) {
			LOG.debug("RPController listener: Stopping MapUpdater");
			setVideoOnState();
			mapUpdater.stop();
		}
		@Override
		public void backward(MediaPlayer mediaPlayer) {
			//LOG.info("InnerMediaPlayerEventListener.backward()");
			updateMobilePosition();
		}
		@Override
		public void forward(MediaPlayer mediaPlayer) {
			//LOG.info("InnerMediaPlayerEventListener.forward()");
			updateMobilePosition();
		}
		@Override
		public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
			//LOG.info("InnerMediaPlayerEventListener.positionChanged()");
			super.positionChanged(mediaPlayer, newPosition);
		}
	}
	class TrackReportLoader extends SwingWorker<TrackReportDialog, Object> {
		
		TrackReportDialog dialog;
		@Override
		protected TrackReportDialog doInBackground() throws Exception {
			dialog = new TrackReportDialog(mainWindow.getFrame(), model.getVideoTrack().getTrackSegmentFile());
			return dialog;
		}
		@Override
		protected void done() {
			dialog.setVisible(true);
			super.done();
		}
	}
	class VerticalProfileLoader extends SwingWorker<VerticalProfileDialog, Object> {
		
		VerticalProfileDialog dialog;
		@Override
		protected VerticalProfileDialog doInBackground() throws Exception {
			dialog = new VerticalProfileDialog(mainWindow.getFrame(), 
				model.getVideoTrack().getTrackSegmentFile());
			return dialog;
		}
		@Override
		protected void done() {
			dialog.setVisible(true);
			super.done();
		}
	}
	class SlopesLoader extends SwingWorker<SlopesDialog, Object> {
		
		SlopesDialog dialog;
		@Override
		protected SlopesDialog doInBackground() throws Exception {
			dialog = new SlopesDialog(mainWindow.getFrame(), 
				model.getVideoTrack().getTrackSegmentFile());
			return dialog;
		}
		@Override
		protected void done() {
			dialog.setVisible(true);
			super.done();
		}
	}
	class SpeedsLoader extends SwingWorker<SpeedsDialog, Object> {
		
		SpeedsDialog dialog;
		@Override
		protected SpeedsDialog doInBackground() throws Exception {
			dialog = new SpeedsDialog(mainWindow.getFrame(), 
				model.getVideoTrack().getTrackSegmentFile());
			return dialog;
		}
		@Override
		protected void done() {
			dialog.setVisible(true);
			super.done();
		}
	}
	class HelpLoader extends SwingWorker<HelpDialog, Object> {
		
		HelpDialog dialog;
		@Override
		protected HelpDialog doInBackground() throws Exception {
			dialog = new HelpDialog(mainWindow.getFrame());
			return dialog;
		}
		@Override
		protected void done() {
			dialog.setVisible(true);
			super.done();
		}
	}

	class AboutLoader extends SwingWorker<AboutDialog, Object> {
		
		AboutDialog dialog;
		@Override
		protected AboutDialog doInBackground() throws Exception {
			dialog = new AboutDialog(mainWindow.getFrame());
			return dialog;
		}
		@Override
		protected void done() {
			dialog.setVisible(true);
			super.done();
		}
	}
	
	class ConfigLoader extends SwingWorker<ConfigDialog, Object> {
		
		ConfigDialog dialog;
		@Override
		protected ConfigDialog doInBackground() throws Exception {
			dialog = new ConfigDialog(mainWindow.getFrame());
			return dialog;
		}
		@Override
		protected void done() {
			dialog.setVisible(true);
			super.done();
		}
	}
	class FrameExtractorLoader extends SwingWorker<FrameExtractorDialog, Object> {

		FrameExtractorDialog dialog;
		
		@Override
		protected FrameExtractorDialog doInBackground() throws Exception {
			dialog = new FrameExtractorDialog(mainWindow.getFrame(), MainController.this);
			return dialog;
		}
		@Override
		protected void done() {
			dialog.setVisible(true);
			super.done();
		}		
		public FrameExtractorDialog getDialog() {
			return dialog;
		}

	}
	// Interface SynchroVideoToolListener
	@Override
	public void setPosition(DirectPosition2D proypos) {
		//LOG.info("MainController.setPosition(): " + proypos);
		if(model.getTrackSegment() == null) {
			return;
		}
		DirectPosition2D geopos = new DirectPosition2D();
		try {
			MathTransform tr = GeoToolsFactory.generateTransformToWGS84(trackMap.getMapModel().getCoordinateReferenceSystem());
			tr.transform(proypos, geopos);
		} catch (Exception e) {
			this.showStatusBarMessage(MessageLevel.MESSAGE_LEVEL_WARNING, "SynchroVideoTool", "Error al transformar coordenadas");
			return;
		} 
		ReferencedSegment rs = new ReferencedSegment(model.getTrackSegment());
		LinearLocation ll = rs.project(geopos.x, geopos.y);
		WayPoint wp = model.getTrackSegment().getWayPoint(ll.getSegmentIndex());
		long t = wp.getTime() - model.getStartTime();
		//LOG.info("MainController.setPosition(): " + t);
		videoController.go(t);
		updateMobilePosition();
	}
	public MainModel getModel() {
		return model;
	}
	
		
}
