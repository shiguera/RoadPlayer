package com.mlab.roadplayer.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import com.mlab.roadplayer.Constants;

public class RPUtil {
	private final static Logger LOG = Logger.getLogger(RPUtil.class.getName());

	public static ImageIcon createImageIcon(String path, int width, int height) {
		URL imgURL = ClassLoader.getSystemResource("img/"+path);
	    return new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
	}
	public static Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	/**
	 * Realiza un Thread.sleep(). No vale para entorno swing
	 * @param milliseconds
	 */
	public static void pause(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
			LOG.warn("RPUtil.pause() ERROR: "+e.getMessage());
		}
	}
	
	/**
	 * Muestra un dialogo de abrir fichero con el filtro que se pasa 
	 * como argumento, el título y el texto del botón OK personalizados. 
	 * Devuelve el fichero seleccionado o null si se cancela. Si se elige un
	 * fichero se establece 'lastDirectory' en el valor de ese directorio. 
	 * 
	 * @param filter
	 * 			Un FileNameExtensionFilter para filtrar los ficheros que se abren
	 * @param dialogTitle
	 * 			Texto del título del cuadro de diálogo
	 * @param btnOkText
	 * 			Texto del botón OK
	 * 			
	 * @return Devuelve el File del fichero seleccionado o null si se cancela
	 */
	public static File showOpenFileDialog(JFrame frame, FileNameExtensionFilter filter, 
		String dialogTitle, String btnOkText) {
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(Constants.lastDirectory));
		fc.setFileFilter(filter);
		fc.setDialogTitle(dialogTitle);
		fc.setApproveButtonText(btnOkText);
		int returnvalue = fc.showOpenDialog(frame);
		if(returnvalue==JFileChooser.APPROVE_OPTION) {
			File file = RPUtil.getSelectedFileWithExtension(fc); 
			Constants.lastDirectory = file.getParent();
			return file;
		}
		return null;
	}
	public static File showOpenDirectoryDialog(JFrame frame, String dialogTitle, String btnOkText) {
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(Constants.lastDirectory));
			fc.setDialogTitle(dialogTitle);
			fc.setApproveButtonText(btnOkText);
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnvalue = fc.showOpenDialog(frame);
			if(returnvalue==JFileChooser.APPROVE_OPTION) {
				File file = RPUtil.getSelectedFileWithExtension(fc); 
				Constants.lastDirectory = file.getPath();
				return file;
			}
			return null;
		}
	/**
	 * Muestra un dialog para abrir un GPX y devuelve el fichero elegido
	 * 
	 * @param frame JFrame que albergará el diálogo
	 * 
	 * @return File seleccionado o null
	 */
	public static File openGpxFile(JFrame frame) {
		LOG.info("App.openGpxFile()");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Ficheros gpx","gpx");
		String dialogTitle = "Abrir track .gpx";
		String btnOkText = "Abrir";
		File result = RPUtil.showOpenFileDialog(frame,filter, dialogTitle, btnOkText);
		return result;
	}

	/**
	 * Cierra el JFrame recibido como argumento mediante una llamada
	 * al evento WINDOW_CLOSING
	 * @param window
	 */
	public static void closeWindow(Window window) {
		WindowEvent ev = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(ev);
	}

	/**
	 * Crea una imagen del contenido de un JPanel
	 * @param panel
	 * @return
	 */
	public static BufferedImage createImage(JPanel panel) {
	    int w = panel.getWidth();
	    int h = panel.getHeight();
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    panel.paint(g);
	    return bi;
	}
	/**
	 * Works around a JFileChooser limitation, that the selected file when saving
	 * is returned exactly as typed and doesn't take into account the selected
	 * file filter.
	 */
	public static File getSelectedFileWithExtension(JFileChooser c) {
	    File file = c.getSelectedFile();
	    if (c.getFileFilter() instanceof FileNameExtensionFilter) {
	        String[] exts = ((FileNameExtensionFilter)c.getFileFilter()).getExtensions();
	        String nameLower = file.getName().toLowerCase();
	        for (String ext : exts) { // check if it already has a valid extension
	            if (nameLower.endsWith('.' + ext.toLowerCase())) {
	                return file; // if yes, return as-is
	            }
	        }
	        // if not, append the first one from the selected filter
	        file = new File(file.toString() + '.' + exts[0]);
	    }
	    return file;
	}

}
