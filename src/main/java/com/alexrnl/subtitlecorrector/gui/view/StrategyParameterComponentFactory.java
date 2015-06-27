package com.alexrnl.subtitlecorrector.gui.view;

import java.util.ArrayList;
import java.util.List;

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
				return new BooleanStrategyParameter(label);
			case FREE:
				return new FreeStrategyParameter(label);
			case LIST:
				final List<String> values = new ArrayList<>();
				for (final Object value : parameter.getPossibleValues()) {
					values.add(String.valueOf(value));
				}
				return new ListStrategyParameter(label, values);
			default:
				throw new IllegalArgumentException("Unsupported parameter type " + parameter.getType());
		}
	}
	
}
