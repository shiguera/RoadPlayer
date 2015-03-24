package com.mlab.roadplayer;
import org.apache.log4j.Logger;

import com.mlab.gpx.impl.TrackSegment;
import com.mlab.patterns.AbstractObservable;
import com.mlab.roadplayer.video.VideoTrack;


public class MainModel extends AbstractObservable {
	private static final Logger LOG = Logger.getLogger(MainModel.class.getName());

	VideoTrack videoTrack;
	
	// setVideoTrack
		public boolean setVideoTrack(VideoTrack videotrack) {
			if(videotrack == null || !videotrack.isValid()) {
				LOG.warn("MainModel.setVideoTrack() WARNING no valid videotrack") ;
				return false;
			}
			this.videoTrack = videotrack;
			notifyObservers();
			return true;
		}

		public VideoTrack getVideoTrack() {
			return videoTrack;
		}
		public TrackSegment getTrackSegment() {
			if(videoTrack!=null ) {
				return this.videoTrack.getSegment();
			}
			return null;
		}
		public long getStartTime() {
			if(videoTrack!=null) {
				return this.videoTrack.getStartDate();
			}
			return -1l;
		}		
		
}

