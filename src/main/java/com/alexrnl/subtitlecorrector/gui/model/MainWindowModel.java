package com.alexrnl.subtitlecorrector.gui.model;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.alexrnl.commons.mvc.AbstractModel;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;
import com.alexrnl.subtitlecorrector.gui.controller.MainWindowController;

/**
 * The model for the main window.
 * @author Alex
 */
public class MainWindowModel extends AbstractModel {
	
	/** The path of the subtitle */
	private Path						subtitle;
	/** The name of the strategy to use */
	private Strategy					strategy;
	/** Flag to overwrite the file */
	private Boolean						overwrite;
	/** The language to use for the subtitle file */
	private Locale						locale;
	/** The map with the strategy parameters */
	private final Map<String, String>	parameters;
	
	/**
	 * Constructor #1.<br />
	 */
	public MainWindowModel () {
		super();
		parameters = new HashMap<>();
	}

	/**
	 * Return the attribute subtitle.
	 * @return the attribute subtitle.
	 */
	public Path getSubtitle () {
		return subtitle;
	}
	
	/**
	 * Set the attribute subtitle.
	 * @param subtitle
	 *        the attribute subtitle.
	 */
	public void setSubtitle (final Path subtitle) {
		final Path oldSubtitle = this.subtitle;
		this.subtitle = subtitle;
		fireModelChange(MainWindowController.SUBTITLE_PROPERTY, oldSubtitle, subtitle);
		
	}
	
	/**
	 * Return the attribute strategy.
	 * @return the attribute strategy.
	 */
	public Strategy getStrategy () {
		return strategy;
	}
	
	/**
	 * Set the attribute strategy.
	 * @param strategy
	 *        the attribute strategy.
	 */
	public void setStrategy (final Strategy strategy) {
		final Strategy oldStrategyName = this.strategy;
		this.strategy = strategy;
		fireModelChange(MainWindowController.STRATEGY_PROPERTY, oldStrategyName, strategy);
	}
	
	/**
	 * Return the attribute overwrite.
	 * @return the attribute overwrite.
	 */
	public Boolean isOverwrite () {
		return overwrite;
	}
	
	/**
	 * Set the attribute overwrite.
	 * @param overwrite
	 *        the attribute overwrite.
	 */
	public void setOverwrite (final Boolean overwrite) {
		final Boolean oldOverwrite = this.overwrite;
		this.overwrite = overwrite;
		fireModelChange(MainWindowController.OVERWRITE_PROPERTY, oldOverwrite, overwrite);
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
		final Locale oldLocale = this.locale;
		this.locale = locale;
		fireModelChange(MainWindowController.LOCALE_PROPERTY, oldLocale, locale);
	}
	
	/**
	 * Return the strategy parameter value for the given parameter.
	 * @param key
	 *        the key of the parameter.
	 * @return the value of the parameter, <code>null</code> if it is not found.
	 */
	public String getStrategyParameter (final String key) {
		return parameters.get(key);
	}
	
	/**
	 * Return an unmodifiable view of the parameters map.
	 * @return the parameters.
	 */
	public Map<String, String> getStrategyParameters () {
		return Collections.unmodifiableMap(parameters);
	}
	
	/**
	 * Set the value of the specified parameter.
	 * @param key
	 *        the key of the parameter.
	 * @param value
	 *        the new value of the parameter.
	 */
	public void setStrategyParameter (final String key, final String value) {
		final String oldValue = parameters.put(key, value);
		fireModelChange(MainWindowController.STRATEGY_PARAMETER_PROPERTY + key, oldValue, value);
	}
	
}
