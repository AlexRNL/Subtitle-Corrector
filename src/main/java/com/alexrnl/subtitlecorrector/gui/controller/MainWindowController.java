package com.alexrnl.subtitlecorrector.gui.controller;

import java.nio.file.Path;

import com.alexrnl.commons.mvc.AbstractController;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;

/**
 * The controller of the main window.<br />
 * @author Alex
 */
public class MainWindowController extends AbstractController {
	/** The name for the subtitle property */
	public static final String SUBTITLE_PROPERTY = "Subtitle";
	/** The name for the strategy property */
	public static final String STRATEGY_PROPERTY = "Strategy";
	
	/**
	 * Change the value of the subtitle.
	 * @param newSubtitle
	 *        the new subtitle.
	 */
	public void changeSubtitle (final Path newSubtitle) {
		setModelProperty(SUBTITLE_PROPERTY, newSubtitle);
	}
	
	/**
	 * Change the value of the strategy.
	 * @param strategy
	 *        the new strategy.
	 */
	public void changeStrategy (final Strategy strategy) {
		setModelProperty(STRATEGY_PROPERTY, strategy);
	}
}
