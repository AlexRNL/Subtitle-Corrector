package com.alexrnl.subtitlecorrector.gui.view;

import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.alexrnl.commons.translation.Translator;
import com.alexrnl.commons.utils.Word;
import com.alexrnl.subtitlecorrector.common.TranslationKeys.Console;
import com.alexrnl.subtitlecorrector.service.SessionParameters;
import com.alexrnl.subtitlecorrector.service.UserPrompt;
import com.alexrnl.subtitlecorrector.service.UserPromptAnswer;

/**
 * A console implementation for the {@link UserPrompt} interface.
 * @author Alex
 */
public class ConsoleUserPrompt implements UserPrompt {
	/** The translator to use */
	private Translator	translator;
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
	public void setTranslator (final Translator translator) {
		this.translator = translator;
	}

	@Override
	public void startSession (final SessionParameters parameters) {
		if (translator == null) {
			throw new IllegalStateException("Cannot start session without translator set");
		}
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
		
		final Console consoleKey = KEYS.console();
		final com.alexrnl.subtitlecorrector.common.TranslationKeys.Console.UserPrompt userPromptKey = consoleKey.userPrompt();
		final String yes = translator.get(consoleKey.yes());
		final String yesNoChoice = translator.get(consoleKey.yesNoPrompt());
		
		output.println(translator.get(userPromptKey.replace(), original, replacement));
		if (context != null) {
			output.println(translator.get(userPromptKey.context()));
			output.println(context);
		}
		output.print(yesNoChoice);
		final boolean keep = inputScanner.nextLine().startsWith(yes);
		if (keep) {
			answer = replacement;
		} else {
			output.print(translator.get(userPromptKey.changeReplacement()));
			answer = inputScanner.nextLine();
			if (answer.isEmpty()) {
				cancelled = true;
			}
		}
		output.println(translator.get(userPromptKey.rememberChoice()));
		output.print(yesNoChoice);
		rememberChoice = inputScanner.nextLine().startsWith(yes);
		
		return new UserPromptAnswer(answer, cancelled, rememberChoice);
	}
	
	@Override
	public UserPromptAnswer confirm (final Word original, final String replacement) {
		return confirm(null, original, replacement);
	}
}
