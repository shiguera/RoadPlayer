package com.mlab.roadplayer.video;


public class BasicVideoView extends VideoView {

	public BasicVideoView(VideoModel model, VideoController controller) {
		super(model, controller);
	}

	protected void addVideoPanelToMainPanel() {
		//this.mainPanel.setLayout(new BorderLayout());
		//this.mainPanel.add(getVideoPanel(),BorderLayout.CENTER);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
