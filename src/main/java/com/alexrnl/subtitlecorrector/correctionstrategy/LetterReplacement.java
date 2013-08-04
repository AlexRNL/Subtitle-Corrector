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
	private static Logger				lg	= Logger.getLogger(LetterReplacement.class.getName());
	
	private final Parameter<Character>	originalLetter;
	private final Parameter<Character>	newLetter;
	private final Parameter<Boolean>	onlyFromDictionnary;
	private final Parameter<Boolean>	promptBeforeCorrecting;

	/**
	 * Constructor #1.<br />
	 */
	private LetterReplacement () {
		super();
		this.originalLetter = new Parameter<>(ParameterType.FREE, "strategy.letterreplacement.originalletter");
		this.newLetter = new Parameter<>(ParameterType.FREE, "strategy.letterreplacement.newletter");
		this.onlyFromDictionnary = new Parameter<>(ParameterType.BOOLEAN, "strategy.letterreplacement.onlyfromdictionnary", false, true);
		this.promptBeforeCorrecting = new Parameter<>(ParameterType.BOOLEAN, "strategy.letterreplacement.promptbeforecorrecting", false, true);
	}

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
