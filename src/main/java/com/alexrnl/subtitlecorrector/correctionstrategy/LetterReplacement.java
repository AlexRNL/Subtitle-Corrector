package com.alexrnl.subtitlecorrector.correctionstrategy;

import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexrnl.commons.utils.Word;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.service.DictionaryManager;

/**
 * TODO
 * @author Alex
 */
public class LetterReplacement implements Strategy {
	/** Logger */
	private static Logger				lg	= Logger.getLogger(LetterReplacement.class.getName());
	
	/** The dictionary manager used in the application */
	private final DictionaryManager		dictionaryManager;
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
	public LetterReplacement (final DictionaryManager dictionaryManager) {
		super();
		this.dictionaryManager = dictionaryManager;
		originalLetter = new Parameter<>(ParameterType.FREE, KEYS.strategy().letterReplacement().originalLetter());
		newLetter = new Parameter<>(ParameterType.FREE, KEYS.strategy().letterReplacement().newLetter());
		onlyMissingFromDictionary = new Parameter<>(ParameterType.BOOLEAN, KEYS.strategy().letterReplacement().onlyMissingFromDictionary(), false, true);
		promptBeforeCorrecting = new Parameter<>(ParameterType.BOOLEAN, KEYS.strategy().letterReplacement().promptBeforeCorrecting(), false, true);
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
			if (lg.isLoggable(Level.FINE)) {
				lg.fine("Skipping subtitle " + subtitle);
			}
			return;
		}
		
		String remaining = subtitle.getContent();
		final StringBuilder newContent = new StringBuilder();
		while (!remaining.isEmpty()) {
			final Word currentWord = Word.getNextWord(remaining);
			newContent.append(remaining.substring(0, currentWord.getBegin() - 1));
			remaining = remaining.substring(currentWord.getEnd());
			
			if (!currentWord.getWord().contains(originalLetter.getValue().toString())) {
				// The letter to replace is not in the word
				newContent.append(currentWord);
				continue;
			}
			
			if (onlyMissingFromDictionary.getValue() && dictionaryManager.contains(currentWord.getWord())) {
				// The current word is in the dictionary
				continue;
			}
			
			final String replacement = currentWord.getWord().replaceAll(
					originalLetter.getValue().toString(), newLetter.getValue().toString());
			
			if (promptBeforeCorrecting.getValue()) {
				// replacement = prompt.ask(subtitle.getContent(), currentWord, replacement);
				// TODO prompt for auto correction on next occurrence of word?
			}
			newContent.append(replacement);
		}
		
		if (lg.isLoggable(Level.FINE)) {
			lg.fine("Replacing subtitle content '" + subtitle.getContent() + "' with '" + newContent + "'");
		}
		
		subtitle.setContent(newContent.toString());
	}
}
