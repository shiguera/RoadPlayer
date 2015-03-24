package com.mlab.roadplayer.video;

import java.awt.Dimension;

public class VideoFileMetadata {
	/**
	 * Length of file in bytes
	 */
	protected long fileLength; 
	/**
	 * Length of video in milliseconds
	 */
	protected long videoLength;
	/**
	 * Frames per second
	 */
	protected double fps;
	/**
	 * Dimension widthxheight in pixels
	 */
	protected Dimension dimension;
	
	public VideoFileMetadata() {
		this.fileLength = 0l;
		this.videoLength = 0l;
		this.fps = 0.0;
		this.dimension = new Dimension();
	}

	public long getFileLength() {
		return fileLength;
	}

	public long getVideoLength() {
		return videoLength;
	}

	public double getFps() {
		return fps;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public void setVideoLength(long videoLength) {
		this.videoLength = videoLength;
	}

	public void setFps(double fps) {
		this.fps = fps;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

}
