package com.mlab.roadplayer.video;

import com.mlab.gpx.impl.TrackSegment;
import com.mlab.gpx.impl.util.Util;
import com.mlab.gpx.impl.util.XmlFactory;
import com.mlab.patterns.NamedDescribed;
import com.mlab.roadplayer.util.TrackSegmentFile;

/**
 * A VideoTrackImpl is a Video plus a Track 
 * @author shiguera
 *
 */
public class VideoTrackImpl implements VideoTrack, NamedDescribed {

	private final String NODE_NAME = "videotrack";
	private String name;
	private String description;
	
	private VideoFile videoFile;
	private TrackSegmentFile segmentFile;
	
	public VideoTrackImpl() {
		this.name = "";
		this.description = "";
		this.setVideoFile(null);
		this.setTrackSegmentFile(null);
	}
	public VideoTrackImpl(VideoFile videofile, TrackSegmentFile gpxfile) {
		this.name = "";
		this.description = "";
		this.videoFile = videofile;
		this.segmentFile = gpxfile;
	}
	public VideoTrackImpl(String name, String descrip, VideoFile videofile, TrackSegmentFile gpxfile) {
		this.name = name;
		this.description = descrip;
		this.videoFile = videofile;
		this.segmentFile = gpxfile;
	}
	@Override
	public String toXml() {
		StringBuilder builder = new StringBuilder();
		builder.append(XmlFactory.createOpenTag(NODE_NAME));
		builder.append(XmlFactory.createOpenTag("name"));
		builder.append(name);
		builder.append(XmlFactory.createCloseTag("name"));

		builder.append(XmlFactory.createOpenTag("description"));
		builder.append(description);
		builder.append(XmlFactory.createCloseTag("description"));

		builder.append(this.videoFile.toXml());
		builder.append(this.segmentFile.toXml());
		builder.append(XmlFactory.createCloseTag(NODE_NAME));
		return builder.toString();
	}
	@Override
	public boolean fromXml() {
		// TODO Complete fromXml method
		return false;
	}
	@Override
	public VideoFile getVideoFile() {
		return this.videoFile;
	}
	@Override
	public TrackSegmentFile getTrackSegmentFile() {
		return this.segmentFile;
	}
	@Override
	public void setVideoFile(VideoFile videoFile) {
		this.videoFile = videoFile;
	}
	@Override
	public void setTrackSegmentFile(TrackSegmentFile gpxFile) {
		this.segmentFile = gpxFile;
	}
	@Override	
	public String getXmlNodename() {
		return this.NODE_NAME;
	}
	@Override
	public String getName() {
		return this.name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getDescription() {
		return this.description;
	}
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public boolean isValid() {
		if(this.videoFile.isValid()==false) {
			return false;
		}
		if(this.segmentFile.isValid()==false) {
			return false;
		}
		return true;
	}
	@Override
	public boolean isValidVideoFile() {
		return this.videoFile.isValid();
	}
	@Override
	public boolean isValidTrackSegmentFile() {
		return this.segmentFile.isValid();
	}
	@Override
	public long getStartDate() {
		return Util.startTimeFromFilename(videoFile.getFile());
	}
	@Override
	public TrackSegment getSegment() {
		TrackSegment segment = null;
		if(this.segmentFile!=null && this.segmentFile.isValid()) {
			return this.segmentFile.getSegment();
		}
		return null;
	}

}
