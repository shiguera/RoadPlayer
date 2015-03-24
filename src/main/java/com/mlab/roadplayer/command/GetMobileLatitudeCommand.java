package com.mlab.roadplayer.command;

import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.util.Util;
import com.mlab.map.TrackMapModel;

public class GetMobileLatitudeCommand extends GetValueCommand {

	public GetMobileLatitudeCommand(TrackMapModel model) {
		super(model);
	}

	public String getValue() {
		String result = "";
		WayPoint wp = ((TrackMapModel)model).getLastPosition();
		if(wp!=null) {
			StringBuilder builder = new StringBuilder();
			builder.append(Util.doubleToString(wp.getLatitude(), 10, 6));
			result = builder.toString();
		}
		return result;				
	}
}
