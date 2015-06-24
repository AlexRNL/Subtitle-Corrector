package com.alexrnl.subtitlecorrector.gui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.alexrnl.subtitlecorrector.correctionstrategy.Parameter;

/**
 * Class representing a free strategy parameter.<br />
 * @author Alex
 */
public class FreeStrategyParameter implements StrategyParameterComponent {
	/** The parameter managed by this component. */
	private final Parameter<?>	parameter;
	/** The panel representing the label and the text field. */
	private final JPanel		component;
	/** The text field that hold the parameter value */
	private final JTextField textField;
	
	
	/**
	 * Constructor #1.<br />
	 * @param parameter
	 *        the parameter managed by this component.
	 * @param label
	 *        the label of the parameter.
	 */
	public FreeStrategyParameter (final Parameter<?> parameter, final String label) {
		super();
		this.parameter = parameter;
		component = new JPanel(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.BASELINE_TRAILING, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		
		component.add(new JLabel(label), c);
		textField = new JTextField();
		c.gridx = 1;
		c.weightx = 1;
		component.add(textField, c);
	}
	
	@Override
	public JComponent getComponent () {
		return component;
	}
}
