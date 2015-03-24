package com.mlab.roadplayer.command;

import com.mlab.gpx.impl.util.Util;
import com.mlab.roadplayer.video.VideoModel;

public class GetVideoPositionCommand extends GetValueCommand {

	public GetVideoPositionCommand(VideoModel model) {
		super(model);
	}

	public String getValue() {
		long vpos = this.getVideoPosition();		
		//System.out.println(vpos.getTime());
		return Util.secondsToHMSString(vpos/1000l);
	}
	private long getVideoPosition() {
		return ((VideoModel)model).getVideoTime();
	}
}
