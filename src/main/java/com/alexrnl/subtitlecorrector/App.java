package com.alexrnl.subtitlecorrector;

import java.io.IOException;
import java.net.URISyntaxException;

import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.commons.io.UncloseableInputStream;

/**
 * Launcher class for the application.<br />
 * @author Alex
 */
public final class App {
	
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
				System.err.println("Something went wrong with the subtitle correction, check logs.");
			}
		} catch (IOException | URISyntaxException | IllegalArgumentException e) {
			System.err.println("Could not start subtitle correction: " + ExceptionUtils.display(e));
		}
	}
}
