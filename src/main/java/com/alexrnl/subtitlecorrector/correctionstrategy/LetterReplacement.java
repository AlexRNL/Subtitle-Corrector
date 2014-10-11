package com.alexrnl.subtitlecorrector.correctionstrategy;

import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexrnl.commons.utils.Word;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.service.DictionaryManager;
import com.alexrnl.subtitlecorrector.service.SessionParameters;
import com.alexrnl.subtitlecorrector.service.UserPrompt;
import com.alexrnl.subtitlecorrector.service.UserPromptAnswer;

/**
 * Strategy for replacing a letter by an other in subtitles.
 * @author Alex
 */
public class LetterReplacement implements Strategy {
	/** Logger */
	private static Logger				lg	= Logger.getLogger(LetterReplacement.class.getName());
	
	/** The dictionary manager used in the application */
	private final DictionaryManager		dictionaryManager;
	/** The user prompt to use */
	private final UserPrompt			prompt;
	/** The choice which were saved by the user for this session */
	private final Map<String, String>	savedChoices;
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
	 * @param dictionaryManager
	 *        the dictionary manager to use.
	 * @param prompt
	 *        the prompt to use.
	 */
	public LetterReplacement (final DictionaryManager dictionaryManager, final UserPrompt prompt) {
		super();
		this.dictionaryManager = dictionaryManager;
		this.prompt = prompt;
		savedChoices = new HashMap<>();
		originalLetter = new Parameter<>(ParameterType.FREE, KEYS.strategy().letterReplacement().originalLetter(), StandardParameterParsers.character());
		newLetter = new Parameter<>(ParameterType.FREE, KEYS.strategy().letterReplacement().newLetter(), StandardParameterParsers.character());
		onlyMissingFromDictionary = new Parameter<>(ParameterType.BOOLEAN, KEYS.strategy().letterReplacement().onlyMissingFromDictionary(), false, StandardParameterParsers.bool(), true);
		promptBeforeCorrecting = new Parameter<>(ParameterType.BOOLEAN, KEYS.strategy().letterReplacement().promptBeforeCorrecting(), false, StandardParameterParsers.bool(), true);
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
	public Parameter<?> getParameterByName (final String name) {
		Objects.requireNonNull(name);
		for (final Parameter<?> parameter : getParameters()) {
			if (parameter.getDescription().equals(name)) {
				return parameter;
			}
		}
		lg.info("No parameter with name " + name + " found");
		return null;
	}

	@Override
	public String getTranslationKey () {
		return KEYS.strategy().letterReplacement().toString();
	}
	
	@Override
	public String getDescription () {
		return KEYS.strategy().letterReplacement().description();
	}
	
	@Override
	public void startSession (final SessionParameters parameters) {
		if (!savedChoices.isEmpty()) {
			throw new IllegalStateException("Cannot start session with non-empty saved choices.");
		}
		
	}
	
	@Override
	public void stopSession () {
		if (lg.isLoggable(Level.INFO)) {
			lg.info("End of session, " + savedChoices.size() + " were saved during the session.");
		}
		savedChoices.clear();
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
			if (currentWord.getBegin() > 0) {
				newContent.append(remaining.substring(0, currentWord.getBegin()));
			}
			remaining = remaining.substring(currentWord.getEnd());
			
			if (!currentWord.getWord().contains(originalLetter.getValue().toString())) {
				// The letter to replace is not in the word
				newContent.append(currentWord);
				continue;
			}
			
			if (savedChoices.containsKey(currentWord.getWord())) {
				newContent.append(savedChoices.get(currentWord.getWord()));
				continue;
			}
			
			if (onlyMissingFromDictionary.getValue() && dictionaryManager.contains(currentWord.getWord())) {
				// The current word is in the dictionary
				newContent.append(currentWord);
				continue;
			}
			
			String replacement = currentWord.getWord().replaceAll(
					originalLetter.getValue().toString(), newLetter.getValue().toString());
			
			if (promptBeforeCorrecting.getValue()) {
				final UserPromptAnswer answer = prompt.confirm(subtitle.getContent(), currentWord, replacement);
				if (answer.isCancelled()) {
					newContent.append(currentWord);
					continue;
				}
				// TODO remember cancelled choices?
				if (answer.isRememberChoice()) {
					savedChoices.put(currentWord.getWord(), answer.getAnswer());
				}
				replacement = answer.getAnswer();
			}
			newContent.append(replacement);
		}
		
		if (lg.isLoggable(Level.FINE)) {
			lg.fine("Replacing subtitle content '" + subtitle.getContent() + "' with '" + newContent + "'");
		}
		
		subtitle.setContent(newContent.toString());
	}
}
