package com.mlab.roadplayer.video;

import java.io.File;

import com.mlab.roadplayer.xml.AbstractDiskFile;

public class VideoFile extends AbstractDiskFile {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 3880222981350512914L;

	protected String NODENAME = "videofile";
	protected VideoFileMetadata metadata;

	public VideoFile(File file) {
		super(file);
		createEmptyMetadata();
	}
	public VideoFile(File parent, String name) {
		super(parent, name);
		createEmptyMetadata();
	}

	private void createEmptyMetadata() {
		metadata = new VideoFileMetadata();
	}
	@Override
	public String getXmlNodename() {
		return this.NODENAME;
	}
	public boolean isValid() {
		if(this.getFile() == null || this.getFile().exists()==false) {
			return false;			
		}
		return true;
	}
	
	public VideoFileMetadata getMetadata() {
		return metadata;
	}
	public void setMetadata(VideoFileMetadata metadata) {
		this.metadata = metadata;
	}

	
}
