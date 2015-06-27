package com.alexrnl.subtitlecorrector.gui.view;

/**
 * Interface for a listener on the changes of a strategy parameter.
 * @author Alex
 */
public interface StrategyParameterValueListener {
	
	/**
	 * Called when the value of the parameter is changed.
	 * @param newValue
	 *        the new value of the parameter.
	 */
	void valueChanged (String newValue);
}
