package com.alexrnl.subtitlecorrector.gui.view;

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
	
	/** The scanner plugged on the console input */
	private Scanner				input;
	/** The console output */
	private final PrintStream	output;
	
	/**
	 * Constructor #1.<br />
	 */
	public ConsoleUserPrompt () {
		super();
		input = null;
		output = System.out;
	}
	
	@Override
	public void startSession (final SessionParameters parameters) {
		if (input != null) {
			throw new IllegalStateException("Session was not properly stop, input was not null");
		}
		input = new Scanner(System.in);
	}

	@Override
	public void stopSession () {
		if (input == null) {
			throw new IllegalStateException("Session was not properly started, input was null");
		}
		input = null;
		
	}

	@Override
	public UserPromptAnswer confirm (final String context, final Word original, final String replacement) {
		if (input == null) {
			throw new IllegalStateException("Session was not properly started, input is null, " +
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
		final boolean keep = 'y' == input.nextLine().charAt(0);
		if (keep) {
			answer = replacement;
		} else {
			output.println("Type the replacement:");
			answer = input.nextLine();
			if (answer.isEmpty()) {
				cancelled = true;
			}
		}
		output.println("Remember choice for the rest of the session?");
		output.println("y/n >");
		rememberChoice = 'y' == input.nextLine().charAt(0);
		
		return new UserPromptAnswer(answer, cancelled, rememberChoice);
	}
	
	@Override
	public UserPromptAnswer confirm (final Word original, final String replacement) {
		return confirm(null, original, replacement);
	}
}
