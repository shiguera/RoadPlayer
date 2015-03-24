package com.mlab.roadplayer.command;

import com.mlab.gpx.impl.util.Util;
import com.mlab.map.TrackMapModel;

public class GetAccumulateDistanceCommand extends GetValueCommand {

	public GetAccumulateDistanceCommand(TrackMapModel model) {
		super(model);
	}

	public String getValue() {
		String result = "";
		double accdist = ((TrackMapModel)model).getDistanceToOrigin();
		StringBuilder builder = new StringBuilder();
		builder.append(Util.doubleToString(accdist, 10, 1));
		result = builder.toString();
		return result;				
	}
}
