package com.alexrnl.subtitlecorrector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alexrnl.commons.arguments.Arguments;
import com.alexrnl.commons.arguments.Param;
import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.commons.error.TopLevelError;
import com.alexrnl.commons.io.UncloseableInputStream;

/**
 * Launcher class for the application.<br />
 * @author Alex
 */
public final class App {
	/** Name for the console argument */
	private static final String	CONSOLE_ARGUMENT_NAME	= "-console";
	
	/** <code>true</code> to launch the console application */
	@Param(names = { CONSOLE_ARGUMENT_NAME }, description = "If this argument is present, the console application is launched")
	private boolean				isConsole;
	/** The application */
	private final AbstractApp	app;
	
	/**
	 * Constructor #1.<br />
	 * @param args
	 *        the arguments from the command line.
	 */
	public App (final List<String> args) {
		super();
		try {
			new Arguments(AbstractApp.PROGRAM_NAME, this, true).parse(args);
			args.remove(CONSOLE_ARGUMENT_NAME);
			app = isConsole ? new ConsoleApp(args) : new GUIApp(args);
		} catch (IOException | URISyntaxException | IllegalArgumentException e) {
			System.err.println("Could not start subtitle correction: " + ExceptionUtils.display(e));
			throw new TopLevelError(AbstractApp.PROGRAM_NAME + " failed to start", e);
		}
	}
	
	/**
	 * Launch the application.
	 */
	public void launch () {
		if (!app.launch()) {
			System.err.println("Something went wrong with the subtitle correction, check logs.");
		}
	}
	
	/**
	 * Entry point of the application.<br />
	 * If no arguments are provided, the GUI application will be launched.
	 * @param args
	 *        the arguments from the command line.
	 */
	public static void main (final String[] args) {
		System.setIn(new UncloseableInputStream(System.in));
		new App(new ArrayList<>(Arrays.asList(args))).launch();
	}
}
