package com.mlab.roadplayer.command;

import com.mlab.gpx.impl.util.Util;
import com.mlab.map.TrackMapModel;

public class NewGetMobileYCommand extends GetValueCommand {

	public NewGetMobileYCommand(TrackMapModel model) {
		super(model);
	}

	public String getValue() {
		//System.out.println("GetMobilePositionXYCommand().getValue()");
		String result = "";
		double[] pos = ((TrackMapModel)model).getMobilePositionXY();
		if(pos!=null) {
			StringBuilder builder = new StringBuilder();
			builder.append(Util.doubleToString(pos[1], 10, 1));
			result = builder.toString();
		}
		return result;				
	}
}
