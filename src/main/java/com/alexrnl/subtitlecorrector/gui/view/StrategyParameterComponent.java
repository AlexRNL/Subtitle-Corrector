package com.alexrnl.subtitlecorrector.gui.view;

import javax.swing.JComponent;

import com.alexrnl.subtitlecorrector.correctionstrategy.Parameter;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;

/**
 * Interface for component representing a {@link Strategy} {@link Parameter}.
 * @author Alex
 */
public interface StrategyParameterComponent {
	
	/**
	 * Return the swing component for the parameter.
	 * @return the {@link JComponent} that allows modification on the parameter value.
	 */
	JComponent getComponent ();
	
}
