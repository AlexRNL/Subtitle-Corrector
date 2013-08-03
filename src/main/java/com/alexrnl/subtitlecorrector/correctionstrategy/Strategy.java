package com.alexrnl.subtitlecorrector.correctionstrategy;

import java.util.List;

import com.alexrnl.subtitlecorrector.common.Subtitle;

/**
 * Interface for a given strategy to correct the subtitles.
 * @author Alex
 */
public interface Strategy {
	
	List<Parameter> getParameters ();
	
	void correct (Subtitle subtitle);
	
}
