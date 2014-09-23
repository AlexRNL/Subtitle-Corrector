package com.alexrnl.subtitlecorrector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.commons.io.UncloseableInputStream;

/**
 * Launcher class for the application.<br />
 * @author Alex
 */
public final class App {
	/** Logger */
	private static Logger	lg	= Logger.getLogger(App.class.getName());
	
	/**
	 * Entry point of the application.<br />
	 * If no arguments are provided, the GUI application will be launched.
	 * @param args
	 *        the arguments from the command line.
	 */
	public static void main (final String[] args) {
		System.setIn(new UncloseableInputStream(System.in));
		try {
			final AbstractApp app = args.length == 0 ? new GUIApp() : new ConsoleApp(args);
			if (!app.launch()) {
				lg.warning("Something went wrong with the subtitle correction, check logs.");
			}
		} catch (IOException | URISyntaxException e) {
			lg.warning("Could not start subtitle correction: " + ExceptionUtils.display(e));
		}
	}
}
