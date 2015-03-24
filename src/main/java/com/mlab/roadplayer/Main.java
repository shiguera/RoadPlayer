package com.mlab.roadplayer;

import java.io.File;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mlab.gpx.impl.util.Util;

public class Main {

	//public static final String APP_NAME = "RoadPlayer";
	//public static final String APP_VERSION = "2.0-SNAPSHOT";

	private static final Logger LOG = Logger.getLogger(Main.class);
	// private static final Level DEFAULT_LOG_LEVEL = Level.ALL;

	protected MainController controller;
	protected static Preferences preferences;

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						new Main();
					} catch (Exception e) {
						LOG.error("Exception TECACE 2 in App.main() "
								+ e.getMessage());
					}
				}
			});
		} catch (Exception e) {			
			System.out.println("main() Exception 'Te cacé 1'");
		}
	}

	public Main() {
		PropertyConfigurator.configure("log4j.properties");

		loadPreferences();

		LOG.info("-----------------------------------------------------------");
		LOG.info("- RoadPlayer ");
		LOG.info(String.format("- Execution nº %d", Constants.executions));
		LOG.info("- GMT: " + Util.dateTimeToString(new Date().getTime(), true));
		LOG.info("-----------------------------------------------------------");

		Constants.executions++;

		checkProxy();

		initController();

		if(!controller.isEnabled()) {
			LOG.error("Main(): Error inicializando MainController");
			System.exit(1);
		}
		
		controller.postInit();

		// openDefaultVideo();
		// trackMap.getVideoController().play();

	}

	private void loadPreferences() {
		// LOG.debug("loadPreferences()");
		preferences = Preferences
				.userNodeForPackage(com.mlab.roadplayer.Main.class);
		//Constants.version = preferences.get("version", Constants.version);
		Constants.executions = preferences.getInt("executions",
				Constants.executions);
		Constants.cartoDirectory = preferences.get("cartoDirectory",
				Constants.cartoDirectory);
		Constants.lastDirectory = preferences.get("lastDirectory",
				Constants.lastDirectory);
		Constants.mobilePointLineColor = preferences.getInt(
				"mobilePointLineColor", Constants.mobilePointLineColor);
		Constants.mobilePointLineWidth = preferences.getInt(
				"mobilePointLineWidth", Constants.mobilePointLineWidth);
		Constants.mobilePointFillColor = preferences.getInt(
				"mobilePointFillColor", Constants.mobilePointFillColor);
		Constants.mobilePointSize = preferences.getInt("mobilePointSize",
				Constants.mobilePointSize);
		Constants.trackLineColor = preferences.getInt("trackLineColor",
				Constants.trackLineColor);
		// proxy
		Constants.proxyUrl = preferences.get("proxyUrl", Constants.proxyUrl);
		Constants.proxyPort = preferences.get("proxyPort", Constants.proxyPort);
		Constants.proxyUserName = preferences.get("proxyUserName",
				Constants.proxyUserName);
		Constants.proxyPassword = preferences.get("proxyPassword",
				Constants.proxyPassword);
	}
	private void checkProxy() {
		if (Constants.proxyUrl.length() > 0) {
			LOG.debug("checkProxy(): Switching proxy mode");
			LOG.info("Proxy: " + Constants.proxyUrl + ":" + Constants.proxyPort
					+ " " + Constants.proxyUserName + " "
					+ Constants.proxyPassword);
			Authenticator.setDefault(new SimpleAuthenticator(Constants.proxyUserName, Constants.proxyPassword));
			Properties systemProperties = System.getProperties();
			systemProperties.setProperty("http.proxyHost", Constants.proxyUrl);
			systemProperties.setProperty("http.proxyPort", Constants.proxyPort);
		} else {
			LOG.debug("checkProxy(): Switching no proxy mode");
		}
	}
	private void initController() {
		LOG.info("initController()");
		// trackMap = new MainController();
		try {
			ControllerCreator ccreator = new ControllerCreator();
			ccreator.execute();
			controller = ccreator.get();
			LOG.debug("MainController created");
		} catch (Exception e) {
			LOG.fatal("Error creating MainController");
			System.exit(1);
		}
	}
	class ControllerCreator extends SwingWorker<MainController, Object> {
		MainController ctrl;
		@Override
		protected MainController doInBackground() throws Exception {
			LOG.debug("ControllerCreator.doInBackground()");
			ctrl = new MainController();
			return ctrl;
		}
		@Override
		protected void done() {
			LOG.debug("ControllerCreator.done()");
			super.done();
		}
	}

	/**
	 * Guarda las preferencias de usuario. Se llama desde
	 * MainWindow.windowClosing()
	 */
	public static void savePreferences() {
		LOG.debug("savePreferences()");
		//preferences.put("version", Constants.version);
		preferences.putInt("executions", Constants.executions);
		preferences.put("cartoDirectory", Constants.cartoDirectory);
		preferences.put("lastDirectory", Constants.lastDirectory);
		preferences.putInt("mobilePointLineColor",
				Constants.mobilePointLineColor);
		preferences.putInt("mobilePointLineWidth",
				Constants.mobilePointLineWidth);
		preferences.putInt("mobilePointFillColor",
				Constants.mobilePointFillColor);
		preferences.putInt("mobilePointSize", Constants.mobilePointSize);
		preferences.putInt("trackLineColor", Constants.trackLineColor);
		// proxy
		preferences.put("proxyUrl", Constants.proxyUrl);
		preferences.put("proxyPort", Constants.proxyPort);
		preferences.put("proxyUserName", Constants.proxyUserName);
		preferences.put("proxyPassword", Constants.proxyPassword);

	}
	private void openDefaultVideo() {
		URL url = ClassLoader
				.getSystemResource("com/mlab/roadplayer/20130318_125729.mp4");
		File file = new File(url.getPath());
		this.controller.openVideoTrack(file);
	}

	/**
	 * Used by CheckProxy()
	 * @author shiguera
	 *
	 */
	class SimpleAuthenticator extends Authenticator {
		private String username, password;

		public SimpleAuthenticator(String username, String password) {
			this.username = username;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password.toCharArray());
		}
	}

	

}
