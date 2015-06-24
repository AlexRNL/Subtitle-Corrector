package com.alexrnl.subtitlecorrector.gui.view;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import com.alexrnl.subtitlecorrector.correctionstrategy.Parameter;

/**
 * Class representing a boolean strategy parameter.
 * @author Alex
 */
public class BooleanStrategyParameter implements StrategyParameterComponent {
	/** The parameter managed by this component. */
	private final Parameter<?>	parameter;
	/** The check box representing the boolean parameter. */
	private final JCheckBox		component;
	
	/**
	 * Constructor #1.<br />
	 * @param parameter
	 *        the parameter managed by this component.
	 * @param label
	 *        the label of the parameter.
	 */
	public BooleanStrategyParameter (final Parameter<?> parameter, final String label) {
		super();
		this.parameter = parameter;
		component = new JCheckBox(label);
	}
	
	@Override
	public JComponent getComponent () {
		return component;
	}
}
