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
import java.util.Collections;
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
import com.alexrnl.commons.io.IOUtils;
import com.alexrnl.subtitlecorrector.io.Dictionary;

/**
 * Class in charge of managing the dictionary used while correcting the subtitles.
 * @author Alex
 */
public class DictionaryManager {
	/** Logger */
	private static Logger					lg						= Logger.getLogger(DictionaryManager.class.getName());
	
	/** The file extension for the dictionary files */
	public static final String				DICTIONARY_EXTENSION	= ".txt";
	
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
		final Path sessionDictionaryPath = Files.createTempFile("sessionDictionary", ".txt");
		sessionDictionaryPath.toFile().deleteOnExit();
		sessionDictionary = new Dictionary(sessionDictionaryPath, Charset.defaultCharset(), true);
		Files.walkFileTree(pathToLocale, new HashSet<FileVisitOption>(), 1, new LocaleDictionaryFileVisitor());
		Files.walkFileTree(pathToCustom, new HashSet<FileVisitOption>(), 1, new CustomDictionaryFileVisitor());
	}
	
	/**
	 * Check that the session has been started.<br />
	 * Throw an {@link IllegalStateException} if it is not the case.
	 */
	private void checkSessionStarted () {
		if (activeDictionaries.isEmpty()) {
			throw new IllegalStateException("Session has not been started");
		}
	}
	
	/**
	 * Check if the active dictionaries contains the following word.<br />
	 * This method requires that a correcting session have been started.
	 * @param word
	 *        the word to check.
	 * @return <code>true</code> if any of the dictionaries contains the word.
	 */
	public boolean contains (final String word) {
		checkSessionStarted();
		
		for (final Dictionary dictionary : activeDictionaries) {
			if (dictionary.contains(word)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add a word to the dictionary for the current session.
	 * @param word
	 *        the word to add.
	 * @return <code>true</code> if the word could be added to the dictionary.
	 */
	public boolean addWord (final String word) {
		checkSessionStarted();
		
		return sessionDictionary.addWord(word);
	}
	
	/**
	 * Add a word to a specific custom dictionary.
	 * @param customDictionaryKey
	 *        the custom dictionary to edit.
	 * @param word
	 *        the word to add.
	 * @return <code>true</code> if the word could be added.
	 */
	public boolean addWord (final String customDictionaryKey, final String word) {
		checkSessionStarted();
		
		if (customDictionaries.containsKey(customDictionaryKey)) {
			return customDictionaries.get(customDictionaryKey).addWord(word);
		}
		lg.warning("Could not find custom dictionary " + customDictionaryKey);
		return false;
	}
	
	/**
	 * Return an unmodifiable view of the map with the locale dictionaries.
	 * @return the map with the locale dictionaries.
	 */
	public Map<Locale, Dictionary> getLocaleDictionaries () {
		return Collections.unmodifiableMap(localeDictionaries);
	}
	
	/**
	 * Return an unmodifiable view of the map with the custom dictionaries.
	 * @return the map with the custom dictionaries.
	 */
	public Map<String, Dictionary> getCustomDictionaries () {
		return Collections.unmodifiableMap(customDictionaries);
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
	
	/**
	 * Template for dictionary file visitors.<br />
	 * @author Alex
	 * @param <T>
	 *        the type of keys of the map to fill.
	 */
	private abstract class DictionaryFileVisitor<T> extends SimpleFileVisitor<Path> {
		/** The reference to the dictionary map used */
		private final Map<T, Dictionary> dictionaryMap;
		
		/**
		 * Constructor #1.<br />
		 * @param dictionaryMap
		 *        the dictionary map to fill by the visitor.
		 */
		public DictionaryFileVisitor (final Map<T, Dictionary> dictionaryMap) {
			super();
			this.dictionaryMap = dictionaryMap;
		}
		
		@Override
		public FileVisitResult visitFile (final Path file, final BasicFileAttributes attrs)
				throws IOException {
			if (file.getFileName().toString().endsWith(DICTIONARY_EXTENSION)) {
				final T key = getDictionaryKey(file);
				if (lg.isLoggable(Level.INFO)) {
					lg.info("Adding dictionary " + key + " from file " + file);
				}
				dictionaryMap.put(key, new Dictionary(file, isEditable()));
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed (final Path file, final IOException exc) throws IOException {
			lg.warning("Could not open or read the file " + file + ": " + ExceptionUtils.display(exc));
			return FileVisitResult.CONTINUE;
		}
		
		/**
		 * This method computes the dictionary key to use for the current file.
		 * @param file
		 *        the dictionary file.
		 * @return the key to map the dictionary.
		 */
		protected abstract T getDictionaryKey (Path file);
		
		/**
		 * Return <code>true</code> if the dictionary should be editable.
		 * @return <code>true</code> for an editable dictionary.
		 */
		protected abstract boolean isEditable ();
	}
	
	/**
	 * File visitor for the locale dictionary.
	 * @author Alex
	 */
	private class LocaleDictionaryFileVisitor extends DictionaryFileVisitor<Locale> {

		/**
		 * Constructor #1.<br />
		 */
		public LocaleDictionaryFileVisitor () {
			super(localeDictionaries);
		}

		@Override
		protected Locale getDictionaryKey (final Path file) {
			return Locale.forLanguageTag(IOUtils.getFilename(file));
		}

		@Override
		protected boolean isEditable () {
			return false;
		}
	}
	
	/**
	 * File visitor for the custom dictionary.
	 * @author Alex
	 */
	private class CustomDictionaryFileVisitor extends DictionaryFileVisitor<String> {

		/**
		 * Constructor #1.<br />
		 */
		public CustomDictionaryFileVisitor () {
			super(customDictionaries);
		}

		@Override
		protected String getDictionaryKey (final Path file) {
			return IOUtils.getFilename(file);
		}

		@Override
		protected boolean isEditable () {
			return true;
		}
	}
}
