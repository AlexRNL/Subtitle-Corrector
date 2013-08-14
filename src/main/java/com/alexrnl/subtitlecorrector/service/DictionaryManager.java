package com.alexrnl.subtitlecorrector.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.subtitlecorrector.io.Dictionary;

/**
 * Class in charge of managing the dictionary used while correcting the subtitles.
 * @author Alex
 */
public class DictionaryManager {
	/** Logger */
	private static Logger					lg						= Logger.getLogger(DictionaryManager.class.getName());
	
	/** The file extension for the dictionary files */
	private static final String				DICTIONARY_EXTENSION	= ".txt";
	
	/** Map with the dictionary for each locale */
	private final Map<Locale, Dictionary>	localeDictionaries;
	/** Map with the custom dictionaries of the user */
	private final Map<String, Dictionary>	customDictionaries;
	/** The dictionary of the current session */
	private final Dictionary				sessionDictionary;
	/** The list of active dictionaries in the current session */
	private final List<Dictionary>			activeDictionaries;
	
	/**
	 * Constructor #1.<br />
	 * @param pathToLocale
	 *        the path to the directory where are the dictionaries for locales.
	 * @param pathToCustom
	 *        the path to the directory where are the custom dictionaries.
	 * @throws IOException
	 *         if a dictionary could not be created.
	 */
	public DictionaryManager (final Path pathToLocale, final Path pathToCustom) throws IOException {
		super();
		localeDictionaries = new HashMap<>();
		customDictionaries = new HashMap<>();
		activeDictionaries = new LinkedList<>();
		sessionDictionary = new Dictionary(Files.createTempFile("sessionDictionary", ".txt"),
				Charset.defaultCharset(), true);
		// TODO load dictionaries
		Files.walkFileTree(pathToLocale, new HashSet<FileVisitOption>(), 1, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile (final Path file, final BasicFileAttributes attrs)
					throws IOException {
				// TODO remove extension for parsing the locale
				final Locale locale = Locale.forLanguageTag(file.getFileName().toString());
				localeDictionaries.put(locale, new Dictionary(file));
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed (final Path file, final IOException exc) throws IOException {
				lg.warning("Could not open or read the file " + file + ": " + ExceptionUtils.display(exc));
				return FileVisitResult.CONTINUE;
			}

		});
	}
	
	/**
	 * Check if the active dictionaries contains the following word.
	 * @param word
	 *        the word to check.
	 * @return <code>true</code> if any of the dictionaries contains the word.
	 */
	public boolean contains (final String word) {
		if (activeDictionaries.isEmpty()) {
			throw new IllegalStateException("");
		}
		
		for (final Dictionary dictionary : activeDictionaries) {
			if (dictionary.contains(word)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Start the session with the following dictionaries.
	 * @param localeDictionary
	 *        the locale dictionary to use (<code>null</code> disable any locale dictionary).
	 * @param customDictionariesKeys
	 *        the custom dictionaries to use.
	 */
	public void startSession (final Locale localeDictionary, final String... customDictionariesKeys) {
		activeDictionaries.add(sessionDictionary);
		if (lg.isLoggable(Level.INFO)) {
			lg.info("Activating locale dictionary " + localeDictionary + " and customs "
					+ Arrays.toString(customDictionariesKeys) + " for next session");
		}
		if (localeDictionary != null) {
			activeDictionaries.add(localeDictionaries.get(localeDictionary));
		}
		for (final String customDictionary : customDictionariesKeys) {
			activeDictionaries.add(customDictionaries.get(customDictionary));
		}
	}
	
	/**
	 * Notify that the correcting session has ended, thus all dictionaries are disabled.
	 */
	public void stopSession () {
		for (final Entry<String, Dictionary> dictionary : customDictionaries.entrySet()) {
			if (activeDictionaries.contains(dictionary.getValue()) && dictionary.getValue().isUpdated()) {
				if (lg.isLoggable(Level.INFO)) {
					lg.info("Saving dictionary " + dictionary.getKey() + " because it has been updated in the last session");
				}
				try {
					dictionary.getValue().save();
					if (lg.isLoggable(Level.INFO)) {
						lg.info("Dictionary successfully saved");
					}
				} catch (final IOException e) {
					lg.warning("The dictionary file " + dictionary.getKey() + " could not be saved: "
							+ ExceptionUtils.display(e));
				}
			}
		}
		activeDictionaries.clear();
	}
}
