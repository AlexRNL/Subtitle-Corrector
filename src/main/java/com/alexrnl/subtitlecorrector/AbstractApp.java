package com.alexrnl.subtitlecorrector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.alexrnl.commons.translation.Translator;
import com.alexrnl.subtitlecorrector.correctionstrategy.FixPunctuation;
import com.alexrnl.subtitlecorrector.correctionstrategy.LetterReplacement;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;
import com.alexrnl.subtitlecorrector.io.SubtitleFormatManager;
import com.alexrnl.subtitlecorrector.io.subrip.SubRip;
import com.alexrnl.subtitlecorrector.service.DictionaryManager;
import com.alexrnl.subtitlecorrector.service.UserPrompt;

/**
 * Abstract subtitle correction application.<br />
 * Allows to factorise initialization code between console and GUI application.
 * @author Alex
 */
public abstract class AbstractApp {
	/** The translator to use in the application */
	private final Translator			translator;
	/** The dictionary manager */
	private final DictionaryManager		dictionariesManager;
	/** The available strategies */
	private final List<Strategy>		strategies;
	/** The subtitle format manager */
	private final SubtitleFormatManager	subtitleFormatManager;
	
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
		translator = new Translator(Paths.get(AbstractApp.class.getResource("/locale/en.xml").toURI()));
		userPrompt.setTranslator(translator);
		// Load services TODO load custom dictionaries from configuration
		dictionariesManager = new DictionaryManager(Paths.get(AbstractApp.class.getResource("/locale").toURI()),
				Paths.get(AbstractApp.class.getResource("/dictionary").toURI()));
		
		strategies = new ArrayList<>();
		strategies.add(new LetterReplacement(dictionariesManager, userPrompt));
		strategies.add(new FixPunctuation(Paths.get(AbstractApp.class.getResource("/punctuation").toURI())));
		
		subtitleFormatManager = new SubtitleFormatManager();
		subtitleFormatManager.registerFormat(new SubRip());
	}
	
	/**
	 * Return the attribute translator.
	 * @return the attribute translator.
	 */
	protected Translator getTranslator () {
		return translator;
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
	protected List<Strategy> getStrategies () {
		return strategies;
	}

	/**
	 * Return the attribute subtitleFormatManager.
	 * @return the attribute subtitleFormatManager.
	 */
	protected SubtitleFormatManager getSubtitleFormatManager () {
		return subtitleFormatManager;
	}

	/**
	 * Launch the application.<br />
	 * @return <code>true</code> if the application has been launched successfully.
	 */
	public abstract boolean launch ();
}
