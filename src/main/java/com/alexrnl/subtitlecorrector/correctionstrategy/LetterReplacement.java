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
	
	/** The original letter to replace */
	private final Parameter<Character>	originalLetter;
	/** The new letter to put */
	private final Parameter<Character>	newLetter;
	/** Flag indicating to replace only in word which are not in the dictionary */
	private final Parameter<Boolean>	onlyMissingFromDictionary;
	/** Flag indicating to prompt user each time before replacing the letter */
	private final Parameter<Boolean>	promptBeforeCorrecting;

	/**
	 * Constructor #1.<br />
	 */
	private LetterReplacement () {
		super();
		this.originalLetter = new Parameter<>(ParameterType.FREE, "strategy.letterreplacement.originalletter");
		this.newLetter = new Parameter<>(ParameterType.FREE, "strategy.letterreplacement.newletter");
		this.onlyMissingFromDictionary = new Parameter<>(ParameterType.BOOLEAN, "strategy.letterreplacement.onlyfromdictionary", false, true);
		this.promptBeforeCorrecting = new Parameter<>(ParameterType.BOOLEAN, "strategy.letterreplacement.promptbeforecorrecting", false, true);
	}

	@Override
	public List<Parameter<?>> getParameters () {
		final List<Parameter<?>> parameters = new ArrayList<>(4);
		parameters.add(originalLetter);
		parameters.add(newLetter);
		parameters.add(onlyMissingFromDictionary);
		parameters.add(promptBeforeCorrecting);
		return parameters;
	}
	
	@Override
	public void correct (final Subtitle subtitle) {
		if (!subtitle.getContent().contains(originalLetter.getValue().toString())) {
			// Skip subtitles which are not concerned
			return;
		}
		
		final String remaining = subtitle.getContent();
		final StringBuilder newContent = new StringBuilder();
		while (!remaining.isEmpty()) {
			
			
		}
		
		
		subtitle.setContent(newContent.toString());
	}
}
