package com.alexrnl.subtitlecorrector;

import java.util.logging.Logger;

import com.alexrnl.subtitlecorrector.gui.controller.MainWindowController;
import com.alexrnl.subtitlecorrector.gui.model.MainWindowModel;
import com.alexrnl.subtitlecorrector.gui.view.MainWindowView;

/**
 * Launcher class for the application.<br />
 * @author Alex
 */
public final class App {
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
		
		final MainWindowController controller = new MainWindowController();
		final MainWindowModel model = new MainWindowModel();
		final MainWindowView view = new MainWindowView(null, controller);
		controller.addModel(model);
		controller.addView(view);
		
		view.setVisible(true);
		
		lg.info("Subtitle Corrector running");
	}
	
	/**
	 * Entry point of the application.
	 * @param args
	 *        the arguments from the command line.
	 */
	public static void main (final String[] args) {
		new App().launch();
	}
}
