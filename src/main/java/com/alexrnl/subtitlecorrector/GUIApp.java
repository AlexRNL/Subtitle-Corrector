package com.alexrnl.subtitlecorrector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.subtitlecorrector.gui.controller.MainWindowController;
import com.alexrnl.subtitlecorrector.gui.model.MainWindowModel;
import com.alexrnl.subtitlecorrector.gui.view.MainWindowView;

/**
 * Launcher class for the application.<br />
 * @author Alex
 */
public final class GUIApp extends AbstractApp {
	/** Logger */
	private static Logger		lg	= Logger.getLogger(GUIApp.class.getName());
	
	/**
	 * Constructor #1.<br />
	 * @throws IOException
	 * @throws URISyntaxException
	 *         if the path of the translation file cannot be build.
	 */
	private GUIApp () throws IOException, URISyntaxException {
		super();
	}
	
	@Override
	public boolean launch () {
		lg.info("Subtitle Corrector starting...");
		
		// Load MVC
		final MainWindowController controller = new MainWindowController();
		final MainWindowModel model = new MainWindowModel();
		final MainWindowView view = new MainWindowView(null, controller, getTranslator(), getStrategies());
		controller.addModel(model);
		controller.addView(view);
		
		synchronized (view) {
			try {
				while (!view.isReady()) {
					view.wait();
				}
			} catch (final InterruptedException e) {
				lg.warning("Main thread interrupted while waiting for GUI building: " + ExceptionUtils.display(e));
			}
		}
		view.setVisible(true);
		
		lg.info("Subtitle Corrector running");
		return true;
	}
	
	/**
	 * Entry point of the application.<br />
	 * If no arguments are provided, the GUI application will be launched.
	 * @param args
	 *        the arguments from the command line.
	 */
	public static void main (final String[] args) {
		if (args.length == 0) {
			try {
				new GUIApp().launch();
			} catch (final IOException | URISyntaxException e) {
				lg.warning("Could start subtitle corrector: " + ExceptionUtils.display(e));
			}
		} else {
			if (new ConsoleApp(args).launch()) {
				System.out.println("Exit subtitle correction session with success!");
			} else {
				System.out.println("Exit subtitle correction session with failure...");
			}
		}
	}
}
