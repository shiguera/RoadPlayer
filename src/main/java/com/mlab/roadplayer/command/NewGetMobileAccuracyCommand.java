package com.mlab.roadplayer.command;

import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.AndroidWayPoint;
import com.mlab.gpx.impl.util.Util;
import com.mlab.map.TrackMapModel;

public class NewGetMobileAccuracyCommand extends GetValueCommand {

	public NewGetMobileAccuracyCommand(TrackMapModel model) {
		super(model);
	}

	public String getValue() {
		String result = "";
		WayPoint wp = ((TrackMapModel)model).getLastPosition();
		if(wp!=null && AndroidWayPoint.class.isAssignableFrom(wp.getClass())) {
			StringBuilder builder = new StringBuilder();
			builder.append(Util.doubleToString(((AndroidWayPoint)wp).getAccuracy(), 4, 1));
			result = builder.toString();
		} else {
			result = " - ";
		}
		return result;				
	}
}
