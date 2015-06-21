package com.alexrnl.subtitlecorrector.correctionstrategy;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.alexrnl.subtitlecorrector.service.SessionParameters;

/**
 * Abstract strategy implementation.<br />
 * Provide a basic body for the actual strategies. There is no logic in this class.
 * @author Alex
 */
public abstract class AbstractStrategy implements Strategy {
	/** Logger */
	private static final Logger	LG	= Logger.getLogger(AbstractStrategy.class.getName());
	
	/** The translation key for the strategy */
	private final com.alexrnl.subtitlecorrector.common.TranslationKeys.Strategy.AbstractStrategy translationKey;
	
	/**
	 * Constructor #1.<br />
	 * @param translationKey
	 *        the translation key of the strategy.
	 */
	public AbstractStrategy (final com.alexrnl.subtitlecorrector.common.TranslationKeys.Strategy.AbstractStrategy translationKey) {
		super();
		this.translationKey = translationKey;
	}
	
	@Override
	public String getTranslationKey () {
		return translationKey.toString();
	}
	
	@Override
	public String getDescription () {
		return translationKey.description();
	}
	
	@Override
	public void startSession (final SessionParameters parameters) {
		// Nothing to do here, override if strategy depends on session state
	}
	
	@Override
	public void stopSession () {
		// Nothing to do here, override if strategy depends on session state
	}
	
	@Override
	public List<Parameter<?>> getParameters () {
		return Collections.emptyList();
	}
	
	@Override
	public Parameter<?> getParameterByName (final String name) {
		Objects.requireNonNull(name);
		for (final Parameter<?> parameter : getParameters()) {
			if (parameter.getTranslationKey().equals(name)) {
				return parameter;
			}
		}
		LG.info("No parameter with name " + name + " found");
		return null;
	}
	
}
