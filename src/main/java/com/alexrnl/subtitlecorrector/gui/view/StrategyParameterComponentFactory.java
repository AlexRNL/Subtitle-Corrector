package com.alexrnl.subtitlecorrector.gui.view;

import com.alexrnl.subtitlecorrector.correctionstrategy.Parameter;

/**
 * Factory which creates {@link StrategyParameterComponent} depending for each component.
 * @author Alex
 */
public class StrategyParameterComponentFactory {
	
	/**
	 * Constructor #1.<br />
	 * Default constructor.
	 */
	public StrategyParameterComponentFactory () {
		super();
	}
	
	/**
	 * Create the parameter component for the specified parameter.<br />
	 * @param parameter
	 *        the parameter.
	 * @param label
	 *        the label of the parameter (translated).
	 * @return the parameter component to use.
	 * @throws IllegalArgumentException
	 *         if the parameter is <code>null</code> or the parameter type is not supported.
	 */
	public StrategyParameterComponent getParameterComponent (final Parameter<?> parameter, final String label) {
		if (parameter == null) {
			throw new IllegalArgumentException("Cannot create component for null parameter");
		}
		
		switch (parameter.getType()) {
			case BOOLEAN:
				return new BooleanStrategyParameter(parameter, label);
			case FREE:
				return new FreeStrategyParameter(parameter, label);
			case LIST:
				return new ListStrategyParameter(parameter, label);
			default:
				throw new IllegalArgumentException("Unsupported parameter type " + parameter.getType());
		}
	}
	
}
