package com.alexrnl.subtitlecorrector.gui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Class representing a free strategy parameter.<br />
 * @author Alex
 */
public class FreeStrategyParameter implements StrategyParameterComponent {
	/** The panel representing the label and the text field. */
	private final JPanel								component;
	/** The text field that hold the parameter value */
	private final JTextField							textField;
	/** List of listeners on the value of the component */
	private final List<StrategyParameterValueListener>	valueListeners;
	
	
	/**
	 * Constructor #1.<br />
	 * @param label
	 *        the label of the parameter.
	 */
	public FreeStrategyParameter (final String label) {
		super();
		component = new JPanel(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.BASELINE_TRAILING, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		
		component.add(new JLabel(label), c);
		textField = new JTextField();
		c.gridx = 1;
		c.weightx = 1;
		component.add(textField, c);
		
		valueListeners = Collections.synchronizedList(new ArrayList<StrategyParameterValueListener>());
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped (final KeyEvent e) {
				for (final StrategyParameterValueListener strategyParameterValueListener : valueListeners) {
					strategyParameterValueListener.valueChanged(textField.getText());
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
	
}
