package com.mlab.roadplayer.synchrovideo;

import java.util.List;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.mlab.roadplayer.MainController;

/**
 * MapUpdater es una clase encargada de actualizar la posición del móvil en
 * el mapa. Dispone de un Thread temporizado que en cada intervalo llama 
 * al método <em>updateMobilePosition()</em> del {@link com.mlab.roadplayer.RPController}.<br/>
 *  
 * El MapUpdater lo gestiona {@link com.mlab.roadplayer.MainController} mediante los
 * métodos <em>playing(), paused()</em> y <em>finished()</em> de su MediaPlayerEventListener.<br/>
 *
 * @author shiguera
 *
 */
public class MapUpdater {
	private final Logger LOG = Logger.getLogger(getClass().getName());
	private final long DEFAULT_INTERVAL_MILLISECONDS = 1000;
	//Thread updater;
	Updater updater;
	MainController controller;
	boolean continueUpdating;
	long intervalMilliseconds;
	
	public MapUpdater(MainController controller) {
		this.controller = controller;
		this.intervalMilliseconds=DEFAULT_INTERVAL_MILLISECONDS;
		continueUpdating = true;
		
		updater = new Updater();
		
	}
	void update() {
		//System.out.println("MapUpdater.update()");
		controller.updateMobilePosition();	
	}
	public void setInterval(long milliseconds) {
		this.intervalMilliseconds = milliseconds;
	}
	public void start() {
		//System.out.println("MapUpdater.start()");
		continueUpdating = true;
		updater.execute();
	}
	public void stop() {
		//System.out.println("MapUpdater.stop()");
		continueUpdating = false;
	}
	

	class Updater extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			LOG.trace("MapUpdater.Udater: UpdaterWorker started");
			while(continueUpdating) {
				try {
					Thread.sleep(intervalMilliseconds);
				} catch (Exception e) {
					LOG.warn("MapUpdater.Updater ERROR \n"+e.getMessage());
				}
				publish();
			}
			LOG.trace("MapUpdater.Udater: Updater stopped");
			return null;
		}
		@Override
		protected void process(List<Void> chunks) {
			update();
			super.process(chunks);
		}
		
	}

}
