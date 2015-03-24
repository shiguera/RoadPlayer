package com.mlab.roadplayer.util;

import java.io.File;

import org.jfree.util.Log;

import com.mlab.gpx.api.GpxDocument;
import com.mlab.gpx.api.GpxFactory;
import com.mlab.gpx.impl.Track;
import com.mlab.gpx.impl.TrackSegment;
import com.mlab.roadplayer.xml.AbstractDiskFile;

public class TrackSegmentFile extends AbstractDiskFile {

	private final String XMLNODENAME = "gpxfile";

	protected GpxDocument gpxDocument;
	protected TrackSegment segment;
	
	public TrackSegmentFile(File parent, String gpxfilename) {
		super(parent, gpxfilename);
		buildSegment(this.getFile());
	}

	public TrackSegmentFile(File gpxfile) {
		super(gpxfile);
		buildSegment(this.getFile());
	}

	private void buildSegment(File gpxfile) {
		if(gpxfile == null || !gpxfile.exists()) {
			Log.warn("buildSegment() Error: gpxfile null or doesn't exist");
			return;
		}
		gpxDocument = GpxFactory.readGpxDocument(gpxfile);
		segment = extractFirstTrackSegmentFromGpxDocument(gpxDocument);
	}
	private TrackSegment extractFirstTrackSegmentFromGpxDocument(GpxDocument doc) {
		TrackSegment tracksegment = null;
		if(doc!=null && doc.hasTracks()) {
			Track track = doc.getTracks().get(0);
			if(track.hasSegments()) {
				tracksegment = track.getTrackSegment(0);	
			}
		}
		return tracksegment;
	}
	public TrackSegment getSegment() {
		return segment;
	}

	@Override
	public String getXmlNodename() {
		return this.XMLNODENAME;
	}

	public boolean isValid() {
		boolean result = this.file.exists();
		result = result && this.gpxDocument != null;
		if(result) {
			result = result && gpxDocument.hasTracks();
			result = result && gpxDocument.getTrack(0).hasSegments();			
		}
		return result;
	}
	public GpxDocument getGpxDocument() {
		return this.gpxDocument;
	}
	

}
