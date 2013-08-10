package com.alexrnl.subtitlecorrector.service;

import java.util.HashMap;
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
	
	/** Map with the dictionaries */
	private final Map<String, Dictionary> dictionaries;
	
	/**
	 * Constructor #1.<br />
	 */
	public DictionaryManager () {
		super();
		this.dictionaries = new HashMap<>();
	}
}
