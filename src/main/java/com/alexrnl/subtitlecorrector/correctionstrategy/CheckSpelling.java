package com.alexrnl.subtitlecorrector.correctionstrategy;

import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.alexrnl.commons.utils.Word;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.service.DictionaryManager;
import com.alexrnl.subtitlecorrector.service.UserPrompt;
import com.alexrnl.subtitlecorrector.service.UserPromptAnswer;

/**
 * Strategy which checks the spellings of words in subtitles.
 * @author Alex
 */
public class CheckSpelling extends AbstractStrategy {
	/** Logger */
	private static final Logger			LG	= Logger.getLogger(CheckSpelling.class.getName());
	
	/** The dictionary manager used in the application */
	private final DictionaryManager		dictionaryManager;
	/** The user prompt to use */
	private final UserPrompt			prompt;
	/** The choice which were saved by the user for this session */
	private final Map<String, String>	savedChoices;
	
	/**
	 * Constructor #1.<br />
	 * @param dictionaryManager
	 *        the dictionary manager to use.
	 * @param prompt
	 *        the prompt to use.
	 */
	public CheckSpelling (final DictionaryManager dictionaryManager, final UserPrompt prompt) {
		super(KEYS.strategy().checkSpelling());
		this.dictionaryManager = dictionaryManager;
		this.prompt = prompt;
		savedChoices = new HashMap<>();
	}
	
	@Override
	public void correct (final Subtitle subtitle) {
		final String content = subtitle.getContent();
		if (content.isEmpty()) {
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
			
			if (savedChoices.containsKey(currentWord.getWord())) {
				final String replacement = savedChoices.get(currentWord.getWord());
				newContent.append(replacement == null ? currentWord : replacement);
				continue;
			}
			
			if (dictionaryManager.contains(currentWord.getWord())) {
				newContent.append(currentWord);
				continue;
			}
			
			final UserPromptAnswer answer = prompt.confirm(content, currentWord, "");
			if (answer.isRememberChoice()) {
				savedChoices.put(currentWord.getWord(), answer.isCancelled() ? null : answer.getAnswer());
			}
			if (answer.isCancelled()) {
				newContent.append(currentWord);
			} else {
				newContent.append(answer.getAnswer());
			}
		}
	}
}
