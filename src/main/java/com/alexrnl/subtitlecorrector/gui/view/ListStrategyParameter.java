package com.alexrnl.subtitlecorrector.gui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * TODO
 * @author Alex
 */
public class ListStrategyParameter implements StrategyParameterComponent {
	/** The panel representing the label and the text field. */
	private final JPanel								component;
	/** The combo box that hold the possible values */
	private final JComboBox<String>						comboBox;
	/** List of listeners on the value of the component */
	private final List<StrategyParameterValueListener>	valueListeners;
	
	
	/**
	 * Constructor #1.<br />
	 * @param label
	 *        the label of the parameter.
	 * @param values
	 *        the values of the combo box.
	 */
	public ListStrategyParameter (final String label, final List<String> values) {
		super();
		component = new JPanel(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.BASELINE_TRAILING, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		
		component.add(new JLabel(label), c);
		comboBox = new JComboBox<>(values.toArray(new String[values.size()]));
		c.gridx = 1;
		component.add(comboBox, c);
		
		valueListeners = Collections.synchronizedList(new ArrayList<StrategyParameterValueListener>());
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged (final ItemEvent e) {
				for (final StrategyParameterValueListener strategyParameterValueListener : valueListeners) {
					strategyParameterValueListener.valueChanged((String) e.getItem());
				}
			
			}
		});
	}
	
	@Override
	public JComponent getComponent () {
		return component;
	}
	
	@Override
	public void addValueListener (final StrategyParameterValueListener listener) {
		if (valueListeners == null) {
			throw new IllegalArgumentException("Cannot add null listener to be notified");
		}
		valueListeners.add(listener);
	}
	
	@Override
	public void setValue (final String value) {
		comboBox.setSelectedItem(value);
	}
	
}
