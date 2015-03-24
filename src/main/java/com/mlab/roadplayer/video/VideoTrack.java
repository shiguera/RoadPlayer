package com.mlab.roadplayer.video;

import com.mlab.gpx.impl.TrackSegment;
import com.mlab.patterns.NamedDescribed;
import com.mlab.roadplayer.util.TrackSegmentFile;
import com.mlab.roadplayer.xml.XmlSerializable;

/**
 * A VideoTrack is a VideoFile plus a GpxFile<br/>
 * VideoTrack extends XmlSerializable
 * 
 * @author shiguera
 *
 */
public interface VideoTrack extends XmlSerializable, NamedDescribed {
	String getName();
	void setName(String name);
	String getDescription();
	void setDescription(String description);
	VideoFile getVideoFile();
	void setVideoFile(VideoFile videoFile);
	TrackSegmentFile getTrackSegmentFile();
	void setTrackSegmentFile(TrackSegmentFile tsFile);
	boolean isValid();
	boolean isValidVideoFile();
	boolean isValidTrackSegmentFile();
	long getStartDate();
	TrackSegment getSegment();
}
