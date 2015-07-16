package com.alexrnl.subtitlecorrector.gui.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

/**
 * Class representing a boolean strategy parameter.
 * @author Alex
 */
public class BooleanStrategyParameter implements StrategyParameterComponent {
	/** The check box representing the boolean parameter. */
	private final JCheckBox								component;
	/** List of listeners on the value of the component */
	private final List<StrategyParameterValueListener>	valueListeners;
	
	/**
	 * Constructor #1.<br />
	 * @param label
	 *        the label of the parameter.
	 */
	public BooleanStrategyParameter (final String label) {
		super();
		component = new JCheckBox(label);
		valueListeners = Collections.synchronizedList(new ArrayList<StrategyParameterValueListener>());
		
		component.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (final ActionEvent e) {
				for (final StrategyParameterValueListener strategyParameterValueListener : valueListeners) {
					strategyParameterValueListener.valueChanged(String.valueOf(component.isSelected()));
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
		component.setSelected(Boolean.valueOf(value));
	}
	
}
