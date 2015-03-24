package com.mlab.roadplayer.xml;

import java.io.File;

public interface DiskFile extends XmlSerializable {

	public File getFile();
	public boolean exists();
	boolean isValid();
}
