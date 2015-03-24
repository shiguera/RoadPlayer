package com.mlab.roadplayer.xml;

import java.io.File;
import java.net.URL;

import com.mlab.gpx.impl.util.XmlFactory;

public abstract class AbstractDiskFile implements DiskFile {

	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 630108891723296618L;
	
	protected File file;
	
	public AbstractDiskFile(File parent, String name) {
		this.file = new File(parent, name);
	}
	public AbstractDiskFile(URL url) {
		this.file = new File(url.getPath());
	}

	public AbstractDiskFile(File file) {
		if(file != null) {
			this.file = new File(file.getPath());			
		} else {
			this.file = null;
		}
	}

	public File getFile() {
		return this.file;
	}
	
	/**
	 * Returns an xml file's description as:<br/>
	 * &lt;nodename&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&lt;parent&gt;path-to-parent-directory&lt;/parent&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&lt;name&gt;name-of-file&lt;name&gt;<br/>
	 * &lt;nodename&gt;<br/>
	 * <em>nodename</em> is specific of each derived class. Derived classes 
	 * must override the getXmlNodename() or the NODENAME variable
	 */
	public String toXml() {
		StringBuilder builder = new StringBuilder();
		builder.append(XmlFactory.createOpenTag(getXmlNodename()));
		builder.append(XmlFactory.createTag("", "directory", true));
		builder.append(file.getParent());
		builder.append(XmlFactory.createTag("", "directory", false));

		builder.append(XmlFactory.createTag("", "name", true));
		builder.append(file.getName());
		builder.append(XmlFactory.createTag("", "name", false));

		builder.append(XmlFactory.createCloseTag(getXmlNodename()));
		return builder.toString();
	}

	public boolean fromXml() {
		// TODO Complete fromXml() method
		return false;
	}

	public boolean exists() {
		if(this.getFile() == null) {
			return false;
		}
		return this.getFile().exists();
	}
	abstract public String getXmlNodename();

}
