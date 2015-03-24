package com.mlab.roadplayer.video;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.SimpleWayPoint;
import com.mlab.gpx.impl.TrackSegment;
import com.mlab.gpx.impl.util.Util;
import com.mlab.roadplayer.MainController;
import com.mlab.roadplayer.linearref.EquiPointExtractor;

public class FrameExtractor {
	
	private final Logger LOG = Logger.getLogger(FrameExtractor.class);
	private final String PHOTO_NAME_PREFIX = "Frame-";
	protected MainController mainController;
	protected VideoController videoController;
	protected VideoTrack videoTrack;
	protected TrackSegment segment;
	protected double trackLength, startLength, endLength, interval;
	protected long videoStartDate, videoLength;
	protected List<WayPoint> wpoints;
	protected HashMap<Integer, Boolean> mapPhotoSaved; 
	protected int photoCounter;
	protected File destinationDirectory;
	
	protected boolean isPrepared;

	public FrameExtractor(MainController maincontroller, VideoTrack videotrack) {
		this.mainController = maincontroller;
		this.videoController = mainController.getVideoController();
		this.videoTrack = videotrack;
		
		boolean result = videoController.setVideoFile(videoTrack.getVideoFile());
		if(!result) {
			LOG.error("FrameExtractor() ERROR: Can't set VideoTrack");
			isPrepared = false;
			return;
		}
		//LOG.info("FrameExtractor() setVideoFile " + this.videoTrack.getName() + " result=" + result);

		
		prepareVideoPlaying();	
		
		prepareReferencedSegment();

	}

	private void prepareVideoPlaying() {
		long t = new Date().getTime();
		videoController.play();
		while(new Date().getTime()<t+250l) {
			
		}
		videoLength = videoController.getVideoLength();
		//LOG.info("startVideoPlaying() videoLength=" + videoLength);
		videoController.pause();		
	}

	private void prepareReferencedSegment() {
		videoStartDate = videoTrack.getStartDate();
		//LOG.info("prepareReferencedSegment(): trackStartDate=" + videoStartDate);
		segment = videoTrack.getSegment();
		//referencedSegment = new ReferencedSegment(segment);		
	}

	/**
	 * Extrae una serie de fotogramas equiespaciados 'interval' metros de los
	 * puntos situados desde 'startLength' metros al punto 'endLength' metros
	 * 
	 * @param startLength longitud inicial del segmento
	 * @param endLength longitud final del segmento
	 * @param interval espacio en metros entre fotogramas
	 * @param destinationDrectory Directorio para la salida de fotogramas
	 * @return true si todo va bien, false en caso contrario
	 */

	public boolean extract(double startLength, double endLength, double interval, File destinationDrectory) {
		if(!isValidVideoTrack()) {
			return false;
		}
		this.startLength = startLength;
		this.endLength = endLength;
		this.interval = interval;
		this.destinationDirectory = destinationDrectory;
		
		//String cad =  String.format("(startLength,  endLength, interval)=(%f, %f, %f)",startLength, endLength, interval);
		//LOG.info("extract() before validating: " + cad);
		validateFromToInterval();
		//LOG.info("extract() after validating:  " + cad);

		EquiPointExtractor extractor = new EquiPointExtractor(segment);
		wpoints = extractor.extract(startLength, endLength, interval);
		if(wpoints == null || wpoints.size() == 0) {
			return false;
		}
		//StringBuffer csv = new StringBuffer();
		photoCounter = -1;
		mapPhotoSaved = new HashMap<Integer, Boolean>();
		extractNext();
				
		return false;
	}
	
	private void extractNext() {
		photoCounter++;
		if(photoCounter>=wpoints.size()) {
			LOG.debug("FrameExtractor.extractNext() finished, last=" + (photoCounter-1));
			this.saveCsvFile();
			return;
		}
		long t = wpoints.get(photoCounter).getTime();

		long videotime =  t - videoStartDate;
		if(videotime>=0l && videotime <= videoLength) {
			//LOG.debug("videoTime="+videotime);
			SnapShooter snapshooter = new SnapShooter(photoCounter, videotime,destinationDirectory);
			snapshooter.execute();
		}
		mainController.updateMobilePosition();		
	}
	class SnapShooter extends SwingWorker<Void, Void> {

		protected int photoNumber;
		protected long photoTime;
		protected File destinationDirectory;
		protected boolean isSaved;
		
		SnapShooter(int photonumber, long photoTime, File destinationdirectory) {
			this.photoNumber = photonumber;
			this.photoTime = photoTime;
			this.destinationDirectory = destinationdirectory;
			LOG.debug("SnapShooter() photoNumber= " + photoNumber + ", photoTime= " + (photoTime/1000));
		}
		
		
		@Override
		protected Void doInBackground() throws Exception {
			isSaved = false;
			BufferedImage image = takeSnapshot(photoTime);
			if(image==null) {
				// Segundo intento
				LOG.debug("SnapShooter.doInBackground() Segundo intento...");
				image = takeSnapshot(photoTime+1l);	
			}
			if (image != null) {
				String filename = PHOTO_NAME_PREFIX + Integer.toString(photoNumber) + ".jpg";
				if(saveImage(image, destinationDirectory, filename)) {
					isSaved = true;
					mapPhotoSaved.put(new Integer(photoNumber), new Boolean(true));
					LOG.info("SnapShooter.doInBackground() frame " + photoNumber + " saved, time=" + (photoTime));					
				} else {
					mapPhotoSaved.put(new Integer(photoNumber), new Boolean(false));					
					LOG.info("SnapShooter.doInBackground() ERROR saving frame " + photoNumber + ", time=" + (photoTime));
				}
			} else {
				LOG.error("SnapShooter.doInBackground() ERROR: saving frame " + photoNumber + ", is null; time=" + (photoTime));
			}
			return null;
		}
		private BufferedImage takeSnapshot(long t) {
			if(t<0l || t>videoLength) {
				return null;
			}
			//LOG.debug("SnapShooter.takeSnapshoot() timeRequired=" + t);
			//LOG.debug("isPlaying=" + videoController.isPlaying());
			if(videoController.isPlaying()) {
				videoController.pause();
			}
			videoController.go(t);
			if(videoController.getTime() != t) {
				LOG.debug("SnapShooter.takeSnapshot() requiredTime=" + t + ", realTime="+videoController.getTime());				
			}
			//videoController.pause();
			BufferedImage image = videoController.takeSnapshot();
			return image;
		}
		private boolean saveImage(BufferedImage image, File destDirectory, String filename) {
			//LOG.debug("SnapShooter.saveImage()");
			try {
				ImageIO.write(image, "JPG", new File(
						destDirectory, filename));
				return true;
			} catch (IOException e) {
				LOG.error("SnapShooter.saveImage() ERROR: Can't save frame " + filename);
				return false;
			}		
		}
		@Override
		protected void done() {
			extractNext();
			super.done();
		}

		public boolean isSaved() {
			return isSaved;
		}
		
	}
	

	private void saveCsvFile() {
		if(mapPhotoSaved != null && mapPhotoSaved.size()>0) {
			StringBuffer buffer = new StringBuffer();
			for(int i=0; i<wpoints.size(); i++) {
				Integer key = new Integer(i);
				if(mapPhotoSaved.containsKey(key) && mapPhotoSaved.get(key) ) {
					buffer.append(Integer.toString(i)+",");
					//LOG.debug(((SimpleWayPoint)wpoints.get(i)).asCsv(true));
					buffer.append(((SimpleWayPoint)wpoints.get(i)).asCsv(true));
					buffer.append("\n");	
				}
			}
			if(buffer.length()>0) {
				File file = new File(destinationDirectory, "memo.txt");
				Util.write(file.getPath(), buffer.toString());
			}
		}
		
	}



	private boolean isValidVideoTrack() {
		if (videoTrack == null || !videoTrack.isValid()) {
			return false;
		}
		return true;
	}


	private void validateFromToInterval() {
		double length = videoTrack.getSegment().length();
		if (startLength < 0.0) {
			startLength = 0.0;
		}
		if (endLength > length) {
			endLength = length;
		}
		if (interval <= 0) {
			interval = endLength - startLength;
		}
	}

	public boolean isPrepared() {
		return isPrepared;
	}
}
