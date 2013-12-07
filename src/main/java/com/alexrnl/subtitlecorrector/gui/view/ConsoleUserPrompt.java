package com.alexrnl.subtitlecorrector.gui.view;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Logger;

import com.alexrnl.commons.utils.Word;
import com.alexrnl.subtitlecorrector.service.SessionParameters;
import com.alexrnl.subtitlecorrector.service.UserPrompt;
import com.alexrnl.subtitlecorrector.service.UserPromptAnswer;

/**
 * A console implementation for the {@link UserPrompt} interface.
 * @author Alex
 */
public class ConsoleUserPrompt implements UserPrompt {
	/** Logger */
	private static Logger		lg	= Logger.getLogger(ConsoleUserPrompt.class.getName());
	
	/** The console input stream */
	private final InputStream	input;
	/** The scanner plugged on the console inputScanner */
	private Scanner				inputScanner;
	/** The console output */
	private final PrintStream	output;
	
	/**
	 * Constructor #1.<br />
	 * @param input
	 *        the input stream to use for reading the user's answers.
	 * @param output
	 *        the output to use for displaying information to the user.
	 */
	public ConsoleUserPrompt (final InputStream input, final PrintStream output) {
		super();
		this.input = input;
		this.output = output;
	}
	
	/**
	 * Constructor #2.<br />
	 * Build a {@link ConsoleUserPrompt} with the {@link System#in} and {@link System#out}.
	 */
	public ConsoleUserPrompt () {
		this(System.in, System.out);
	}
	
	@Override
	public void startSession (final SessionParameters parameters) {
		if (inputScanner != null) {
			throw new IllegalStateException("Session was not properly stop, inputScanner was not null");
		}
		inputScanner = new Scanner(input);
	}

	@Override
	public void stopSession () {
		if (inputScanner == null) {
			throw new IllegalStateException("Session was not properly started, inputScanner was null");
		}
		inputScanner.close();
		inputScanner = null;
		
	}

	@Override
	public UserPromptAnswer confirm (final String context, final Word original, final String replacement) {
		if (inputScanner == null) {
			throw new IllegalStateException("Session was not properly started, inputScanner is null, " +
					"cannot confirm replacement");
		}
		String answer;
		boolean cancelled = false;
		boolean rememberChoice;
		
		// TODO translate
		output.println("Replace '" + original + "' by '" + replacement + "'?");
		if (context != null) {
			output.println("Context:\n\t");
			output.println(context);
		}
		output.println("y/n >");
		final boolean keep = 'y' == inputScanner.nextLine().charAt(0);
		if (keep) {
			answer = replacement;
		} else {
			output.println("Type the replacement:");
			answer = inputScanner.nextLine();
			if (answer.isEmpty()) {
				cancelled = true;
			}
		}
		output.println("Remember choice for the rest of the session?");
		output.println("y/n >");
		rememberChoice = 'y' == inputScanner.nextLine().charAt(0);
		
		return new UserPromptAnswer(answer, cancelled, rememberChoice);
	}
	
	@Override
	public UserPromptAnswer confirm (final Word original, final String replacement) {
		return confirm(null, original, replacement);
	}
}
