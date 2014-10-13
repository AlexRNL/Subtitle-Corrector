package com.alexrnl.subtitlecorrector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.alexrnl.commons.translation.Translator;
import com.alexrnl.subtitlecorrector.correctionstrategy.FixPunctuation;
import com.alexrnl.subtitlecorrector.correctionstrategy.LetterReplacement;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;
import com.alexrnl.subtitlecorrector.io.SubtitleFormatManager;
import com.alexrnl.subtitlecorrector.io.subrip.SubRip;
import com.alexrnl.subtitlecorrector.service.DictionaryManager;
import com.alexrnl.subtitlecorrector.service.SessionManager;
import com.alexrnl.subtitlecorrector.service.SubtitleProvider;
import com.alexrnl.subtitlecorrector.service.UserPrompt;

/**
 * Abstract subtitle correction application.<br />
 * Allows to factorise initialization code between console and GUI application.
 * @author Alex
 */
public abstract class AbstractApp {
	/** The name of the program */
	public static final String			PROGRAM_NAME	= "subtitleCorrector";
	
	/** The translator to use in the application */
	private final Translator			translator;
	/** The session manager to use */
	private final SessionManager		sessionManager;
	/** The dictionary manager */
	private final DictionaryManager		dictionariesManager;
	/** The available strategies */
	private final Map<String, Strategy>	strategies;
	/** The subtitle format manager */
	private final SubtitleFormatManager	subtitleFormatManager;
	/** The subtitle provider */
	private final SubtitleProvider		subtitleProvider;
	
	/**
	 * Default constructor.<br />
	 * Initialize default behavior:
	 * <ul>
	 * <li>Translator</li>
	 * <li>Dictionary manager</li>
	 * <li>Strategies</li>
	 * <li>Subtitle format manager</li>
	 * </ul>
	 * @param userPrompt
	 *        the user prompt to use for initializing strategies.
	 * @throws IOException
	 *         if a resource cannot be loaded.
	 * @throws URISyntaxException
	 *         if there is an error while building a Path.
	 */
	public AbstractApp (final UserPrompt userPrompt) throws IOException, URISyntaxException {
		super();
		
		// TODO load language dynamically
		translator = new Translator(Paths.get(AbstractApp.class.getResource("/locale/en.xml").toURI()));
		userPrompt.setTranslator(translator);
		
		sessionManager = new SessionManager();
		sessionManager.addSessionListener(userPrompt);
		
		// Load services TODO load custom dictionaries from configuration
		dictionariesManager = new DictionaryManager(Paths.get(AbstractApp.class.getResource("/dictionary").toURI()),
				Paths.get(AbstractApp.class.getResource("/locale").toURI()));
		sessionManager.addSessionListener(dictionariesManager);
		
		strategies = new HashMap<>();
		addStrategy(new LetterReplacement(dictionariesManager, userPrompt));
		addStrategy(new FixPunctuation(Paths.get(AbstractApp.class.getResource("/punctuation").toURI())));
		
		subtitleFormatManager = new SubtitleFormatManager();
		subtitleFormatManager.registerFormat(new SubRip());
		
		subtitleProvider = new SubtitleProvider(subtitleFormatManager, userPrompt);
	}
	
	/**
	 * Return the attribute translator.
	 * @return the attribute translator.
	 */
	protected Translator getTranslator () {
		return translator;
	}
	
	/**
	 * Return the attribute sessionManager.
	 * @return the attribute sessionManager.
	 */
	protected SessionManager getSessionManager () {
		return sessionManager;
	}

	/**
	 * Return the attribute dictionariesManager.
	 * @return the attribute dictionariesManager.
	 */
	protected DictionaryManager getDictionariesManager () {
		return dictionariesManager;
	}
	
	/**
	 * Return the attribute strategies.
	 * @return the attribute strategies.
	 */
	protected Map<String, Strategy> getStrategies () {
		return Collections.unmodifiableMap(strategies);
	}
	
	/**
	 * Add a strategy to the map.<br />
	 * Also add the new strategy to the session manager, and remove the previously installed one.
	 * @param strategy
	 *        the strategy to add.
	 * @return <code>true</code> if there was a previous strategy registered with the same
	 *         translation.
	 */
	protected boolean addStrategy (final Strategy strategy) {
		final String name = getTranslator().get(strategy.getTranslationKey());
		final Strategy previousStrategy = strategies.put(name, strategy);
		getSessionManager().removeSessionListener(previousStrategy);
		return previousStrategy != null;
	}
	
	/**
	 * Return the attribute subtitleFormatManager.
	 * @return the attribute subtitleFormatManager.
	 */
	protected SubtitleFormatManager getSubtitleFormatManager () {
		return subtitleFormatManager;
	}
	
	/**
	 * Return the attribute subtitleProvider.
	 * @return the attribute subtitleProvider.
	 */
	protected SubtitleProvider getSubtitleProvider () {
		return subtitleProvider;
	}

	/**
	 * Launch the application.<br />
	 * @return <code>true</code> if the application has been launched successfully.
	 */
	public abstract boolean launch ();
}
