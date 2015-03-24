package com.mlab.roadplayer.video;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

import com.mlab.patterns.AbstractObservable;

/**
 * Un <em>VideoModel<em> encapsula el reproductor de vídeo y el fichero de vídeo a 
 * reproducir.<br/>
 * Un <em>VideoModel</em> consta de un 
 * {@link uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent} que encapsula 
 * un reproductor de vídeo {@link uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer}
 * y un {@link com.mlab.roadplayer.video.VideoFile} que contiene el fichero de 
 * vídeo a reproducir. <em/>
 * El {@link uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent} 
 * es un <em>Panel</em> de Java, por lo que para añadirlo a una ventana se utiliza
 * el método <em>add(Component)</em> del contenedor, por ejemplo:<br/>
 * <em>frame.add(embeddedMediaPlayerComponent);</em><br/>
 * El {@link uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent} se obtiene
 * con el método <em>getMediaPlayerComponent()</em><br/>
 * Se puede acceder al {@link uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer}
 * con el método <em>getMediaPlayer()</em><br/>
 * 
 * 
 * @author shiguera
 *
 */
public class VideoModel extends AbstractObservable {
	private final Logger LOG = Logger.getLogger(getClass().getName());
	private final Color CANVAS_BACKGROUND_COLOR = Color.WHITE;		
	private final long SKIP = 10l; // tanto por ciento del salto sobre la longitud del vídeo
	
	/**
	 * VideoFile a reproducir. Será null tras la inicialización. 
	 * Para reproducir un vídeo hay que llamar primero al método
	 * 'setVideoFile()'
	 */
	protected VideoFile videoFile;
	
	/**
	 * Es el EmbeddedComponet utilizado como reproductor. Tiene embebido
	 * un MediaPlayer y el JComponent con el Canvas donde se proyecta la
	 * imagen. 
	 */
	protected EmbeddedMediaPlayerComponent mediaPlayerComponent;
	/**
	 * Es la inner class utilizada como escuchante del MediaPlayer
	 * por parte del propio VideoModel.
	 */
	protected MediaPlayerEventListener innerMediaPlayerListener;
	/**
	 * Lista de mediaPlayerEventListeners registrados en el 
	 * EmbeddedMediaPlayer. Se lleva la lista aparte para poder
	 * reconstruirla tras reinicializar el EmbeddedComponent.
	 */
	protected List<MediaPlayerEventListener> mediaPlayerListeners;
	
	// Constructor
	public VideoModel() {
		LOG.info("Creating video model");
		
		videoFile = null;
		
		try {
			MediaPlayerLoader loader = new MediaPlayerLoader();
			loader.execute();
			mediaPlayerComponent = loader.get();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mediaPlayerListeners = new ArrayList<MediaPlayerEventListener>();

		innerMediaPlayerListener = new InnerMediaPlayerEventListener();
		addMediaPlayerEventListener(innerMediaPlayerListener);
	}
	class MediaPlayerLoader extends SwingWorker<EmbeddedMediaPlayerComponent, Object> {

		@Override
		protected EmbeddedMediaPlayerComponent doInBackground() throws Exception {
			EmbeddedMediaPlayerComponent component = new EmbeddedMediaPlayerComponent();
			return component;
		}
		
	}
	private EmbeddedMediaPlayerComponent createMediaPlayerComponent() {
		LOG.info("Creating MediaPlayerComponent");
		EmbeddedMediaPlayerComponent m = new EmbeddedMediaPlayerComponent();
		m.getVideoSurface().setBackground(CANVAS_BACKGROUND_COLOR);
		return m;
	}
	// Add MediaPlayer listeners
	public void addMediaPlayerEventListener(MediaPlayerEventListener listener) {
		mediaPlayerListeners.add(listener);
		getMediaPlayer().addMediaPlayerEventListener(listener);
	}
	
	/**
	 * Hace null el videoFile y reinicia el EmbeddedComponent, 
	 * restaurando la lista de MediaPlayerListaners que hubiera. 
	 */
	public void resetMediaPlayer() {
		LOG.debug("Resetting MediaPlayer");
		stop();
		videoFile = null;
		notifyObservers();
	}
	private void restoreListeners() {
		LOG.debug("Restoring mediaPlayerListeners");
		for(MediaPlayerEventListener l: mediaPlayerListeners) {
			getMediaPlayer().addMediaPlayerEventListener(l);
		}
	}
	/**
	 * Libera recursos y deja el MediaPlayer inutilizable. 
	 */
	public void release() {
		LOG.debug("Releasing mediaPlayerComponent");
		mediaPlayerComponent.release();
	}
	
	// Getters
	public VideoFile getVideoFile() {
		if(this.videoFile != null) {
			return this.videoFile;
		}
		return null;
	}
	private MediaPlayer getMediaPlayer() {
		return mediaPlayerComponent.getMediaPlayer();			
	}
	public EmbeddedMediaPlayerComponent getMediaPlayerComponent() {
		return mediaPlayerComponent;
	}
	
	// Set VideoFile
	public boolean setVideoFile(VideoFile videofile) {
		//LOG.info("VTModel.setVideoFile()");
		videoFile = videofile;
		if(videoFile != null) {
			boolean result = getMediaPlayer().prepareMedia(videoFile.getFile().getPath(), "");
			getMediaPlayer().parseMedia();
			if(result) {
				return true;		
			} else {
				LOG.error("Can't prepare video");		
			}
		}
		LOG.error("videoFile = null");
		return false;
	}

	// Status del reproductor
	public boolean isPlaying() {
		return getMediaPlayer().isPlaying();
	}
	public void setVideoPanelSize(int width, int height) {
		this.mediaPlayerComponent.setPreferredSize(new Dimension(width, height));
	}
	
	// Manejo del reproductor
	public void play() {
		if(this.videoFile!=null) {
			getMediaPlayer().play();
			notifyObservers();
		}
	}
	public void stop() {
		getMediaPlayer().stop();
		notifyObservers();
	}
	public void pause() {
		getMediaPlayer().pause();
		notifyObservers();
	}
	public void setTime(long time) {
		getMediaPlayer().setTime(time);
		notifyObservers();
	}
	public void goToBeginning() {
		getMediaPlayer().setPosition(0.0f);
		notifyObservers();
	}
	public void goToEnd() {
		getMediaPlayer().setPosition(0.95f);
		notifyObservers();
	}
	public void skipForward() {
		getMediaPlayer().skip(getSkipMilliseconds());
		notifyObservers();
	}
	public void skipBack() {
		getMediaPlayer().skip(-getSkipMilliseconds());
		notifyObservers();
	}
	private long getSkipMilliseconds() {
		long skip = (long)(getMediaPlayer().getLength()/SKIP);		
		return skip;
	}
	
	// Snapshot
	public BufferedImage getSnapshot() {
		BufferedImage image = getMediaPlayer().getSnapshot();
		return image;
	}
	// Metadatos del fichero de vídeo
	public String getVideoFilename() {
		if(this.videoFile != null) {
			File file = videoFile.getFile();
			if(file != null ) {
				return file.getName();
			}
		}
		return "";
	}
	public long getVideoLength() {
		return getMediaPlayer().getLength();
	}
	public long getVideoTime() {
		return getMediaPlayer().getTime();
	}
	public double getVideoFps() {
		return getMediaPlayer().getFps();
	}
	public Dimension getVideoDimension() {
		return getMediaPlayer().getVideoDimension();
	}
	
	/**
	 * InnerMediaPayerEventListener deriva de MediaPlayerEventListener, 
	 * sobreescribe algunos métodos y es el escuchador utilizado por
	 * la clase VideoModel.
	 * @author shiguera
	 *
	 */
	protected class InnerMediaPlayerEventListener implements MediaPlayerEventListener {
	public void backward(MediaPlayer arg0) {
		//LOG.info("VideoPlayerEventListener: backward");
	}
	public void buffering(MediaPlayer arg0, float arg1) {
		//LOG.info("VideoPlayerEventListener: buffering");
	}
	public void endOfSubItems(MediaPlayer arg0) {
		//LOG.info("VideoPlayerEventListener: endOfSubitems");
	}
	public void error(MediaPlayer arg0) {
		LOG.warn("VideoPlayerEventListener: error");
	}
	public void finished(MediaPlayer arg0) {
		LOG.info("VideoPlayerEventListener: finished");
		mediaPlayerComponent.getMediaPlayer().stop();
		notifyObservers();
	}
	public void forward(MediaPlayer arg0) {
		//LOG.info("VideoPlayerEventListener: forward");
	}
	public void lengthChanged(MediaPlayer arg0, long arg1) {
		//LOG.info("VideoPlayerEventListener: lengthChanged");
	}
	public void mediaChanged(MediaPlayer arg0, libvlc_media_t arg1,
			String arg2) {
		//LOG.debug("VideoModel listener: "+arg2);
		notifyObservers();
	}
	public void mediaDurationChanged(MediaPlayer arg0, long arg1) {
		//LOG.info("VideoPlayerEventListener: mediaDurationChanged");
	}
	public void mediaFreed(MediaPlayer arg0) {
		//LOG.info("VideoPlayerEventListener: mediaFreed");
	}
	public void mediaMetaChanged(MediaPlayer arg0, int arg1) {
		//LOG.info("VideoPlayerEventListener: mediaMetaChanged");
	}
	public void mediaParsedChanged(MediaPlayer arg0, int arg1) {
		//LOG.info("VideoPlayerEventListener: mediaParsedChanged");
	}
	public void mediaStateChanged(MediaPlayer arg0, int arg1) {
		//LOG.debug("VideoModel listener: "+arg1);
	}
	public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) {
		//LOG.info("VideoPlayerEventListener: mediaSubItemAdded");
	}
	public void newMedia(MediaPlayer arg0) {
		//LOG.info("VideoPlayerEventListener: newMedia");
	}
	public void opening(MediaPlayer arg0) {
		//LOG.info("VideoPlayerEventListener: opening");
	}
	public void pausableChanged(MediaPlayer arg0, int arg1) {
		//LOG.info("VideoPlayerEventListener: pausableChanged");
	}
	public void paused(MediaPlayer arg0) {
		//LOG.info("VideoPlayerEventListener: paused");
		notifyObservers();
	}
	public void playing(MediaPlayer arg0) {
		//LOG.info("VideoPlayerEventListener: playing");
		notifyObservers();
	}
	public void positionChanged(MediaPlayer arg0, float arg1) {
		//LOG.info("VideoPlayerEventListener: positionChanged");
		notifyObservers();
	}
	public void seekableChanged(MediaPlayer arg0, int arg1) {
		//LOG.info("VideoPlayerEventListener: seekableChanged");
	}
	public void snapshotTaken(MediaPlayer arg0, String arg1) {
		//LOG.info("VideoPlayerEventListener: snapshotTaken");
	}
	public void stopped(MediaPlayer arg0) {
		//LOG.debug("VideoModel listener: MediaPlayer stopped");
	}
	public void subItemFinished(MediaPlayer arg0, int arg1) {
		//LOG.info("VideoPlayerEventListener: subItemFinished");
	}
	public void subItemPlayed(MediaPlayer arg0, int arg1) {
		//LOG.info("VideoPlayerEventListener: subItemPlayed");
	}
	public void timeChanged(MediaPlayer arg0, long arg1) {
		//LOG.info("VideoPlayerEventListener: timeChanged "+arg1);			
	}
	public void titleChanged(MediaPlayer arg0, int arg1) {
		//LOG.info("VideoPlayerEventListener: titleChanged");
	}
	public void videoOutput(MediaPlayer arg0, int arg1) {
		//LOG.info("VideoPlayerEventListener: videoOutput: "+String.format("%d", arg1));
	}
	
}


}
