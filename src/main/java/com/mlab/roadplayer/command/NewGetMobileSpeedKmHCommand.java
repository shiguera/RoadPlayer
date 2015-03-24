package com.mlab.roadplayer.command;

import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.AndroidWayPoint;
import com.mlab.gpx.impl.util.Util;
import com.mlab.map.TrackMapModel;

public class NewGetMobileSpeedKmHCommand extends GetValueCommand {

	public NewGetMobileSpeedKmHCommand(TrackMapModel model) {
		super(model);
	}

	public String getValue() {
		String result = "";
		WayPoint wp = ((TrackMapModel)model).getLastPosition();
		if(wp!=null && AndroidWayPoint.class.isAssignableFrom(wp.getClass())) {
			double speed = ((AndroidWayPoint)wp).getSpeed()*3.6;
			StringBuilder builder = new StringBuilder();
			builder.append(Util.doubleToString(speed, 10, 1));
			result = builder.toString();
		} else {
			result = " - ";
		}
		return result;				
	}
}
