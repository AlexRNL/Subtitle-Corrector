package com.alexrnl.subtitlecorrector.gui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.alexrnl.subtitlecorrector.correctionstrategy.Parameter;

/**
 * TODO
 * @author Alex
 */
public class ListStrategyParameter implements StrategyParameterComponent {
	/** The parameter managed by this component. */
	private final Parameter<?>	parameter;
	/** The panel representing the label and the text field. */
	private final JPanel		component;
	/** The combo box that hold the possible values */
	private final JComboBox<String> comboBox;
	
	
	/**
	 * Constructor #1.<br />
	 * @param parameter
	 *        the parameter managed by this component.
	 * @param label
	 *        the label of the parameter.
	 */
	public ListStrategyParameter (final Parameter<?> parameter, final String label) {
		super();
		this.parameter = parameter;
		component = new JPanel(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.BASELINE_TRAILING, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		
		component.add(new JLabel(label), c);
		final List<String> values = new ArrayList<>();
		for (final Object value : parameter.getPossibleValues()) {
			values.add(String.valueOf(value));
		}
		comboBox = new JComboBox<>(values.toArray(new String[values.size()]));
		c.gridx = 1;
		component.add(comboBox, c);
	}
	
	@Override
	public JComponent getComponent () {
		return component;
	}}
