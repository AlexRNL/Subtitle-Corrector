package com.alexrnl.subtitlecorrector.correctionstrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.alexrnl.subtitlecorrector.common.Subtitle;

/**
 * TODO
 * @author Alex
 */
public class LetterReplacement implements Strategy {
	/** Logger */
	private static Logger	lg	= Logger.getLogger(LetterReplacement.class.getName());
	
	private Parameter<Character>	originalLetter;
	private Parameter<Character>	newLetter;
	private Parameter<Boolean>		onlyFromDictionnary;
	private Parameter<Boolean>		promptBeforeCorrecting;
	
	@Override
	public List<Parameter<?>> getParameters () {
		final List<Parameter<?>> parameters = new ArrayList<>(4);
		parameters.add(originalLetter);
		parameters.add(newLetter);
		parameters.add(onlyFromDictionnary);
		parameters.add(promptBeforeCorrecting);
		return parameters;
	}
	
	@Override
	public void correct (final Subtitle subtitle) {
		// TODO Auto-generated method stub
		if (!subtitle.getContent().contains(originalLetter.getValue().toString())) {
			return;
		}
		
	}
}
