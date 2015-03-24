package com.mlab.roadplayer.video;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

public class MediaPlayerEventListenerImpl implements MediaPlayerEventListener {

	@Override
	public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media,
			String mrl) {
		//System.out.println("MediaPlayerEventListenerImpl.mediaChanged()");

	}

	@Override
	public void opening(MediaPlayer mediaPlayer) {
		//Syestem.out.println("MediaPlayerEventListenerImpl.opening()");
	}

	@Override
	public void buffering(MediaPlayer mediaPlayer, float newCache) {
		//System.out.println("MediaPlayerEventListenerImpl.buffering()");
	}

	@Override
	public void playing(MediaPlayer mediaPlayer) {
		//System.out.println("MediaPlayerEventListenerImpl.playing()");
	}

	@Override
	public void paused(MediaPlayer mediaPlayer) {
		//System.out.println("MediaPlayerEventListenerImpl.paused()");
	}

	@Override
	public void stopped(MediaPlayer mediaPlayer) {
		//System.out.println("MediaPlayerEventListenerImpl.stopped()");
	}

	@Override
	public void forward(MediaPlayer mediaPlayer) {
		//System.out.println("MediaPlayerEventListenerImpl.forward()");
	}

	@Override
	public void backward(MediaPlayer mediaPlayer) {
		//System.out.println("MediaPlayerEventListenerImpl.backward()");
	}

	@Override
	public void finished(MediaPlayer mediaPlayer) {
		//System.out.println("MediaPlayerEventListenerImpl.finished()");
	}

	@Override
	public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
		//System.out.println("MediaPlayerEventListenerImpl");
	}

	@Override
	public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pausableChanged(MediaPlayer mediaPlayer, int newSeekable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
		// TODO Auto-generated method stub

	}

	@Override
	public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(MediaPlayer mediaPlayer) {
		//System.out.println("MediaPlayerEventListenerImpl.error()");
	}

	@Override
	public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaSubItemAdded(MediaPlayer mediaPlayer,
			libvlc_media_t subItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaFreed(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
		//System.out.println("MediaPlayerEventListenerImpl.mediaStateChanged()");
	}

	@Override
	public void newMedia(MediaPlayer mediaPlayer) {
		//System.out.println("MediaPlayerEventListenerImpl.newMedia()");
	}

	@Override
	public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endOfSubItems(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

}
