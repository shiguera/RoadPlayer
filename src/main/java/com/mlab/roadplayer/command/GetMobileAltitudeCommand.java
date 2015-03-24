package com.mlab.roadplayer.command;

import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.util.Util;
import com.mlab.map.TrackMapModel;

public class GetMobileAltitudeCommand extends GetValueCommand {

	public GetMobileAltitudeCommand(TrackMapModel model) {
		super(model);
	}

	public String getValue() {
		//System.out.println("GetMobileAltitudeCommand().getValue()");
		String result = "";
		WayPoint wp = ((TrackMapModel)model).getLastPosition();
		if(wp!=null) {
			StringBuilder builder = new StringBuilder();
			builder.append(Util.doubleToString(wp.getAltitude(), 10, 1));
			result = builder.toString();
		}
		return result;				
	}
}
