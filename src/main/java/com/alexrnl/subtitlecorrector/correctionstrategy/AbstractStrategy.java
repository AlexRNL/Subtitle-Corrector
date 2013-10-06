package com.alexrnl.subtitlecorrector.correctionstrategy;

import java.util.ArrayList;
import java.util.List;

import com.alexrnl.subtitlecorrector.service.SessionParameters;

/**
 * Abstract strategy implementation.<br />
 * Provide a basic body for the actual strategies. There is no logic in this class.
 * @author Alex
 */
public abstract class AbstractStrategy implements Strategy {
	
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
		return null;
	}
	
}
