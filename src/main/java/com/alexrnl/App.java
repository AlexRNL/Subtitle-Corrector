package com.alexrnl;

import java.util.logging.Logger;

/**
 * Launcher class for the application.<br />
 * @author Alex
 */
public class App {
	/** Logger */
	private static Logger	lg	= Logger.getLogger(App.class.getName());
	
	/**
	 * Constructor #1.<br />
	 */
	private App () {
		super();
	}
	
	/**
	 * Launch the application.
	 */
	private void launch () {
		lg.info("Subtitle Corrector starting...");
	}
	
	/**
	 * Entry point of the application.
	 * @param args
	 *        the arguments from the command line.
	 */
	public static void main (final String[] args) {
		final App app = new App();
		app.launch();
	}
}
