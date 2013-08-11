package com.alexrnl.subtitlecorrector.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import com.alexrnl.subtitlecorrector.io.Dictionary;

/**
 * Class in charge of managing the dictionary used while correcting the subtitles.
 * @author Alex
 */
public class DictionaryManager {
	/** Logger */
	private static Logger	lg	= Logger.getLogger(DictionaryManager.class.getName());
	
	/** Map with the dictionary for each locale */
	private final Map<Locale, Dictionary>	localeDictionaries;
	/** Map with the custom dictionaries of the user */
	private final Map<String, Dictionary>	customDictionaries;
	/** The dictionary of the current session */
	private final Dictionary				sessionDictionary;
	
	/**
	 * Constructor #1.<br />
	 * @throws IOException
	 *         if a dictionary could not be created.
	 */
	public DictionaryManager () throws IOException {
		super();
		localeDictionaries = new HashMap<>();
		customDictionaries = new HashMap<>();
		sessionDictionary = new Dictionary(Files.createTempFile("sessionDictionary", ".txt"),
				Charset.defaultCharset(), true);
	}
}
