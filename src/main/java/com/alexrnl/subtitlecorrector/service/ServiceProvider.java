package com.alexrnl.subtitlecorrector.service;

import java.util.Map;

import com.alexrnl.commons.translation.Translator;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;
import com.alexrnl.subtitlecorrector.io.SubtitleFormatManager;

/**
 * Interface for a subtitle corrector service provider.
 * @author Alex
 */
public interface ServiceProvider {
	
	/**
	 * Return the attribute translator.
	 * @return the attribute translator.
	 */
	Translator getTranslator ();
	
	/**
	 * Return the attribute sessionManager.
	 * @return the attribute sessionManager.
	 */
	SessionManager getSessionManager ();
	
	/**
	 * Return the attribute dictionariesManager.
	 * @return the attribute dictionariesManager.
	 */
	DictionaryManager getDictionariesManager ();
	
	/**
	 * Return the attribute strategies.
	 * @return the attribute strategies.
	 */
	Map<String, Strategy> getStrategies ();
	
	/**
	 * Return the attribute subtitleFormatManager.
	 * @return the attribute subtitleFormatManager.
	 */
	SubtitleFormatManager getSubtitleFormatManager ();
	
	/**
	 * Return the attribute subtitleProvider.
	 * @return the attribute subtitleProvider.
	 */
	SubtitleProvider getSubtitleProvider ();
	
}