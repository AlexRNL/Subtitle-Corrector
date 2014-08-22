package com.alexrnl.subtitlecorrector.correctionstrategy;

import java.util.List;

import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.service.SessionStateListener;

/**
 * Interface for a given strategy to correct the subtitles.
 * @author Alex
 */
public interface Strategy extends SessionStateListener {
	
	/**
	 * Return the list of {@link Parameter parameters} needed by this strategy.
	 * @return a list of parameters.
	 */
	List<Parameter<?>> getParameters ();
	
	/**
	 * Return the parameter matching the name specified.
	 * @param name
	 *        the name to search for.
	 * @return the relevant parameter.
	 */
	Parameter<?> getParameterByName (String name);
	
	/**
	 * Return the name of the strategy.
	 * @return the name of the strategy.
	 */
	String getName ();
	
	/**
	 * Return the description of the strategy.
	 * @return the description of the strategy.
	 */
	String getDescription ();
	
	/**
	 * Called when parsing a subtitle file, on each subtitle.
	 * @param subtitle
	 *        the subtitle being processed.
	 */
	void correct (Subtitle subtitle);
}
