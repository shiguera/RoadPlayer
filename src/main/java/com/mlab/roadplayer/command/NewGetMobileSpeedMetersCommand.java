package com.mlab.roadplayer.command;

import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.AndroidWayPoint;
import com.mlab.gpx.impl.util.Util;
import com.mlab.map.TrackMapModel;

public class NewGetMobileSpeedMetersCommand extends GetValueCommand {

	public NewGetMobileSpeedMetersCommand(TrackMapModel model) {
		super(model);
	}

	public String getValue() {
		String result = "";
		WayPoint wp = ((TrackMapModel)model).getLastPosition();
		if(wp!=null && AndroidWayPoint.class.isAssignableFrom(wp.getClass())) {
			double speedkmh = ((AndroidWayPoint)wp).getSpeed();
			StringBuilder builder = new StringBuilder();
			builder.append(Util.doubleToString(speedkmh, 10, 1));
			result = builder.toString();
		} else {
			result = " - ";
		}
		return result;				
	}
}
