package com.mlab.roadplayer.video;

import java.awt.image.BufferedImage;

import com.mlab.patterns.CompositeObserver;
import com.mlab.roadplayer.swing.Controller;

/**
 * El <em>VideoController</em> se utiliza para manejar un 
 * {@link com.mlab.roadplayer.video.VideoModel}.
 * @author shiguera
 *
 */
public interface VideoController extends Controller, CompositeObserver {
	// VideoFile management
	boolean setVideoFile(VideoFile videofile);
	VideoFile getVideoFile();
	// MediaPlayer management
	void play();
	void stop();
	void pause();
	boolean isPlaying();
	long getTime();
	void go(long time);
	void goToBeginning();
	void goToEnd();
	void skipForward();
	void skipBack();
	long getVideoLength();
	/**
	 * Libera los recursos utilizados, quedando estos
	 * inutilizables salvo reinicialización. 
	 */
	void release();
	void resetVideoPlayer();
	// Snapshot
	BufferedImage takeSnapshot();
	// View management
	VideoView getVideoView();
	void setVideoView(VideoView videoview);
	void setVideoPanelSize(int width, int height);	
}
