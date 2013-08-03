package com.alexrnl.subtitlecorrector.correctionstrategy;

import java.util.List;

import com.alexrnl.subtitlecorrector.common.Subtitle;

/**
 * Interface for a given strategy to correct the subtitles.
 * @author Alex
 */
public interface Strategy {
	
	/**
	 * Return the list of {@link Parameter parameters} needed by this strategy.
	 * @return a list of parameters.
	 */
	List<Parameter<?>> getParameters ();
	
	/**
	 * Called when parsing a subtitle file, on each subtitle.
	 * @param subtitle
	 *        the subtitle being processed.
	 */
	void correct (Subtitle subtitle);
	
}
