package com.alexrnl.subtitlecorrector.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.alexrnl.subtitlecorrector.common.SubtitleFile;

/**
 * Class describing the session parameters.<br />
 * @author Alex
 */
public class SessionParameters {
	
	/** The locale used during the session */
	private Locale				locale;
	/** The custom dictionaries selected for the session */
	private final Set<String>	customDictionaries;
	/** The subtitle file being corrected */
	private SubtitleFile		subtitleFile;
	/** The current position for the correction in the file */
	private int					currentCorrectionIndex;
	
	/**
	 * Constructor #1.<br />
	 * Default constructor.
	 */
	public SessionParameters () {
		super();
		locale = Locale.getDefault();
		customDictionaries = new HashSet<>();
		subtitleFile = null;
		currentCorrectionIndex = -1;
	}
	
	/**
	 * Return the attribute locale.
	 * @return the attribute locale.
	 */
	public Locale getLocale () {
		return locale;
	}
	
	/**
	 * Set the attribute locale.
	 * @param locale
	 *        the attribute locale.
	 */
	public void setLocale (final Locale locale) {
		this.locale = locale;
	}
	
	/**
	 * Return the attribute customDictionaries.
	 * @return the attribute customDictionaries.
	 */
	public Set<String> getCustomDictionaries () {
		return Collections.unmodifiableSet(customDictionaries);
	}
	
	/**
	 * Add the specified custom dictionary to the session parameters.
	 * @param customDictionary
	 *        the custom dictionary to add.
	 */
	public void addCustomDictionay (final String customDictionary) {
		customDictionaries.add(customDictionary);
	}
	
	/**
	 * Remove the specified custom dictionary to the session parameters.
	 * @param customDictionary
	 *        the custom dictionary to remove.
	 */
	public void removeCustomDictionay (final String customDictionary) {
		customDictionaries.remove(customDictionary);
	}
	
	/**
	 * Clear the custom dictionaries in the session parameters.
	 */
	public void clearCustomDictionaries () {
		customDictionaries.clear();
	}
	
	/**
	 * Return the attribute subtitleFile.
	 * @return the attribute subtitleFile.
	 */
	public SubtitleFile getSubtitleFile () {
		return subtitleFile;
	}
	
	/**
	 * Set the attribute subtitleFile.
	 * @param subtitleFile
	 *        the attribute subtitleFile.
	 */
	public void setSubtitleFile (final SubtitleFile subtitleFile) {
		this.subtitleFile = subtitleFile;
	}
	
	/**
	 * Return the attribute currentCorrectionIndex.
	 * @return the attribute currentCorrectionIndex.
	 */
	public int getCurrentCorrectionIndex () {
		return currentCorrectionIndex;
	}
	
	/**
	 * Set the attribute currentCorrectionIndex.
	 * @param currentCorrectionIndex
	 *        the attribute currentCorrectionIndex.
	 */
	public void setCurrentCorrectionIndex (final int currentCorrectionIndex) {
		this.currentCorrectionIndex = currentCorrectionIndex;
	}
	
	@Override
	public String toString () {
		return "SessionParameters [locale=" + locale +
				", customDictionaries=" + customDictionaries +
				", subtitleFile=" + subtitleFile +
				", currentCorrectionIndex=" + currentCorrectionIndex + "]";
	}
	
}
