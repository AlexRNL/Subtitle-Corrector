package com.alexrnl.subtitlecorrector.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Class describing the session parameters.<br />
 * @author Alex
 */
public class SessionParameters {
	
	/** The locale used during the session */
	private Locale				locale;
	/** The custom dictionaries selected for the session */
	private final Set<String>	customDictionaries;
	
	/**
	 * Constructor #1.<br />
	 * Default constructor.
	 */
	public SessionParameters () {
		super();
		locale = Locale.getDefault();
		customDictionaries = new HashSet<>();
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
		return customDictionaries;
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

	@Override
	public String toString () {
		return "SessionParameters [locale=" + locale +
				", customDictionaries=" + customDictionaries + "]";
	}
	
}
