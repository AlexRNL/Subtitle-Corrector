package com.alexrnl.subtitlecorrector.correctionstrategy;

import java.util.ArrayList;
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
		return new ArrayList<>(0);
	}
	
	@Override
	public Parameter<?> getParameterByName (final String name) {
		Objects.requireNonNull(name);
		for (final Parameter<?> parameter : getParameters()) {
			if (parameter.getDescription().equals(name)) {
				return parameter;
			}
		}
		LG.info("No parameter with name " + name + " found");
		return null;
	}
	
}
