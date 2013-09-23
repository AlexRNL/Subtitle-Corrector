package com.alexrnl.subtitlecorrector;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.commons.translation.Translator;
import com.alexrnl.subtitlecorrector.gui.controller.MainWindowController;
import com.alexrnl.subtitlecorrector.gui.model.MainWindowModel;
import com.alexrnl.subtitlecorrector.gui.view.MainWindowView;

/**
 * Launcher class for the application.<br />
 * @author Alex
 */
public final class App {
	/** Logger */
	private static Logger		lg	= Logger.getLogger(App.class.getName());
	
	/** The translator to use in the application */
	private final Translator	translator;
	
	/**
	 * Constructor #1.<br />
	 * @throws URISyntaxException
	 *         if the translation file cannot be loaded.
	 */
	private App () throws URISyntaxException {
		super();
		translator = new Translator(Paths.get(App.class.getResource("/locale/en.xml").toURI()));
	}
	
	/**
	 * Launch the application.
	 */
	private void launch () {
		lg.info("Subtitle Corrector starting...");
		
		final MainWindowController controller = new MainWindowController();
		final MainWindowModel model = new MainWindowModel();
		final MainWindowView view = new MainWindowView(null, controller, translator);
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
		try {
			new App().launch();
		} catch (final URISyntaxException e) {
			lg.warning("Could not load translation file: " + ExceptionUtils.display(e));
		}
	}
}
