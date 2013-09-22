package com.alexrnl.subtitlecorrector.correctionstrategy;

import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.alexrnl.commons.utils.Word;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.service.DictionaryManager;
import com.alexrnl.subtitlecorrector.service.UserPrompt;
import com.alexrnl.subtitlecorrector.service.UserPromptAnswer;

/**
 * Test suite for the {@link LetterReplacement} class.
 * @author Alex
 */
public class LetterReplacementTest {
	/** The dictionary manager used */
	@Mock
	private DictionaryManager		dictionary;
	/** The user prompt used */
	@Mock
	private UserPrompt				prompt;
	/** The strategy tested */
	private LetterReplacement		letterReplacement;
	/** Reference to the original letter parameter */
	private Parameter<Character>	originalLetter;
	/** Reference to the replacement letter parameter */
	private Parameter<Character>	replacementLetter;
	/** Reference to the only missing from dictionary parameter */
	private Parameter<Boolean>		onlyMissingFromDictionary;
	/** Reference to the prompt before correcting parameter */
	private Parameter<Boolean>		promptBeforeCorrecting;

	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		initMocks(this);
		letterReplacement = new LetterReplacement(dictionary, prompt);
		originalLetter = (Parameter<Character>) letterReplacement.getParameterByName(KEYS.strategy().letterReplacement().originalLetter());
		replacementLetter = (Parameter<Character>) letterReplacement.getParameterByName(KEYS.strategy().letterReplacement().newLetter());
		onlyMissingFromDictionary = (Parameter<Boolean>) letterReplacement.getParameterByName(KEYS.strategy().letterReplacement().onlyMissingFromDictionary());
		promptBeforeCorrecting = (Parameter<Boolean>) letterReplacement.getParameterByName(KEYS.strategy().letterReplacement().promptBeforeCorrecting());
		assertNotNull(originalLetter);
		assertNotNull(replacementLetter);
		assertNotNull(onlyMissingFromDictionary);
		assertNotNull(promptBeforeCorrecting);
	}
	
	/**
	 * Test method for {@link LetterReplacement#getParameters()}.
	 */
	@Test
	public void testGetParameters () {
		assertEquals(4, letterReplacement.getParameters().size());
	}
	
	/**
	 * Test method for {@link LetterReplacement#getParameterByName(String)}.
	 */
	@Test
	public void testGetParameterByName () {
		final Iterator<Parameter<?>> parametersIterator = letterReplacement.getParameters().iterator();
		assertEquals(parametersIterator.next(), letterReplacement.getParameterByName(KEYS.strategy().letterReplacement().originalLetter()));
		assertEquals(parametersIterator.next(), letterReplacement.getParameterByName(KEYS.strategy().letterReplacement().newLetter()));
		assertEquals(parametersIterator.next(), letterReplacement.getParameterByName(KEYS.strategy().letterReplacement().onlyMissingFromDictionary()));
		assertEquals(parametersIterator.next(), letterReplacement.getParameterByName(KEYS.strategy().letterReplacement().promptBeforeCorrecting()));
		assertNull(letterReplacement.getParameterByName("random"));
	}
	
	/**
	 * Test method for {@link LetterReplacement#getParameterByName(String)}.
	 */
	@Test(expected = NullPointerException.class)
	public void testGetParameterByNameNullPointerException () {
		letterReplacement.getParameterByName(null);
	}
	
	/**
	 * Test method for {@link LetterReplacement#correct(Subtitle)}.
	 * Null original letter parameter.
	 */
	@Test(expected = NullPointerException.class)
	public void testCorrectOriginalParameterNPE () {
		letterReplacement.correct(new Subtitle(0, 2000, "ldr"));
	}
	
	/**
	 * Test method for {@link LetterReplacement#correct(Subtitle)}.
	 * Null replacementLetter parameter.
	 */
	@Test(expected = NullPointerException.class)
	public void testCorrectReplacementParameterNPE () {
		originalLetter.setValue('l');
		letterReplacement.correct(new Subtitle(0, 2000, "ldr"));
	}
	
	/**
	 * Test method for {@link LetterReplacement#correct(Subtitle)}.
	 * Null subtitle test.
	 */
	@Test(expected = NullPointerException.class)
	public void testCorrectNullSubtitle () {
		letterReplacement.correct(null);
	}
	
	/**
	 * Test method for {@link LetterReplacement#correct(Subtitle)}.
	 * No relevant letter in subtitle test.
	 */
	@Test
	public void testCorrectNoRelevantLetter () {
		final Subtitle subtitleToCorrect = new Subtitle(0, 2000, "Hello everyone!");
		originalLetter.setValue('a');
		replacementLetter.setValue('x');
		letterReplacement.correct(subtitleToCorrect);
		assertEquals("Hello everyone!", subtitleToCorrect.getContent());
	}
	
	/**
	 * Test method for {@link LetterReplacement#correct(Subtitle)}.
	 * Test correction without prompt or dictionary.
	 */
	@Test
	public void testCorrectNoPromptNoDictionary () {
		final Subtitle subtitleToCorrect = new Subtitle(0, 2000, "Hello everyone!");
		originalLetter.setValue('e');
		replacementLetter.setValue('x');
		onlyMissingFromDictionary.setValue(false);
		promptBeforeCorrecting.setValue(false);
		letterReplacement.correct(subtitleToCorrect);
		assertEquals("Hxllo xvxryonx!", subtitleToCorrect.getContent());
	}
	
	/**
	 * Test method for {@link LetterReplacement#correct(Subtitle)}.
	 * Test correction without prompt and with dictionary.
	 */
	@Test
	public void testCorrectNoPromptWithDictionary () {
		final Subtitle subtitleToCorrect = new Subtitle(0, 2000, "Hello everyone!");
		originalLetter.setValue('e');
		replacementLetter.setValue('x');
		onlyMissingFromDictionary.setValue(true);
		promptBeforeCorrecting.setValue(false);
		when(dictionary.contains("Hello")).thenReturn(true);
		letterReplacement.correct(subtitleToCorrect);
		assertEquals("Hello xvxryonx!", subtitleToCorrect.getContent());
	}
	
	/**
	 * Test method for {@link LetterReplacement#correct(Subtitle)}.
	 * Test correction with prompt and without dictionary.
	 */
	@Test
	public void testCorrectWithPromptNoDictionary () {
		final Subtitle subtitleToCorrect = new Subtitle(0, 2000, "Hello everyone!");
		originalLetter.setValue('e');
		replacementLetter.setValue('x');
		onlyMissingFromDictionary.setValue(false);
		promptBeforeCorrecting.setValue(true);
		when(prompt.confirm(anyString(), any(Word.class), anyString())).then(new Answer<UserPromptAnswer>() {
			@Override
			public UserPromptAnswer answer (final InvocationOnMock invocation) throws Throwable {
				return new UserPromptAnswer((String) invocation.getArguments()[2]);
			}
		});
		when(prompt.confirm(anyString(), eq(new Word("Hello", 0, 5)), anyString())).thenReturn(new UserPromptAnswer("LDR"));
		letterReplacement.correct(subtitleToCorrect);
		assertEquals("LDR xvxryonx!", subtitleToCorrect.getContent());
	}
	
	/**
	 * Test method for {@link LetterReplacement#correct(Subtitle)}.
	 * Test correction with prompt which cancel the question and without dictionary.
	 */
	@Test
	public void testCorrectWithPromptCancelNoDictionary () {
		final Subtitle subtitleToCorrect = new Subtitle(0, 2000, "Hello everyone!");
		originalLetter.setValue('e');
		replacementLetter.setValue('x');
		onlyMissingFromDictionary.setValue(false);
		promptBeforeCorrecting.setValue(true);
		when(prompt.confirm(anyString(),  any(Word.class), anyString())).thenReturn(new UserPromptAnswer(null, true, false));
		letterReplacement.correct(subtitleToCorrect);
		assertEquals("Hello everyone!", subtitleToCorrect.getContent());
	}
	
	/**
	 * Test method for {@link LetterReplacement#correct(Subtitle)}.
	 * Test correction with a complex subtitle.
	 */
	@Test
	public void testComplexCorrection () {
		final Subtitle subtitleToCorrect = new Subtitle(0, 200, "Becaune it's not\ngoing up there now.");
		originalLetter.setValue('n');
		replacementLetter.setValue('s');
		onlyMissingFromDictionary.setValue(true);
		promptBeforeCorrecting.setValue(false);
		when(dictionary.contains(anyString())).thenReturn(true);
		when(dictionary.contains("Becaune")).thenReturn(false);
		letterReplacement.correct(subtitleToCorrect);
		assertEquals("Because it's not\ngoing up there now.", subtitleToCorrect.getContent());
	}
	
}