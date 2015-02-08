package com.alexrnl.subtitlecorrector.correctionstrategy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.alexrnl.commons.utils.Word;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.service.DictionaryManager;
import com.alexrnl.subtitlecorrector.service.UserPrompt;
import com.alexrnl.subtitlecorrector.service.UserPromptAnswer;

/**
 * Test suite for the {@link CheckSpelling} class.
 * @author Alex
 */
public class CheckSpellingTest {
	/** The strategy to test */
	private CheckSpelling		checkSpelling;
	/** The dictionary manager used */
	@Mock
	private DictionaryManager	dictionary;
	/** The user prompt used */
	@Mock
	private UserPrompt			prompt;
	
	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		initMocks(this);
		checkSpelling = new CheckSpelling(dictionary, prompt);
	}
	
	/**
	 * Test method for {@link CheckSpelling#getTranslationKey()}.
	 */
	@Test
	public void testGetTranslationKey () {
		assertEquals("subtitlecorrector.strategy.checkspelling", checkSpelling.getTranslationKey());
	}
	
	/**
	 * Test method for {@link CheckSpelling#getDescription()}.
	 */
	@Test
	public void testGetDescription () {
		assertEquals("subtitlecorrector.strategy.checkspelling.description", checkSpelling.getDescription());
	}
	
	/**
	 * Test method for {@link CheckSpelling#correct(Subtitle)} with a subtitle which is correct.
	 */
	@Test
	public void testCorrectAllInDictionnary () {
		final Subtitle subtitle = new Subtitle(0, 2000, "Hello, world!");
		when(dictionary.contains("Hello")).thenReturn(true);
		when(dictionary.contains("world")).thenReturn(true);
		checkSpelling.correct(subtitle);
		assertEquals("Hello, world!", subtitle.getContent());
	}
	
	/**
	 * Test method for {@link CheckSpelling#correct(Subtitle)} with an empty subtitle.
	 */
	@Test
	public void testCorrectEmptySubtitle () {
		final Subtitle subtitle = mock(Subtitle.class);
		when(subtitle.getContent()).thenReturn("");
		checkSpelling.correct(subtitle);
		verify(subtitle, never()).setContent(anyString());
	}
	
	/**
	 * Test method for {@link CheckSpelling#correct(Subtitle)} with a replacement from user prompt.
	 */
	@Test
	public void testCorrectPrompt () {
		Subtitle subtitle = new Subtitle(0, 2000, "Hello, wxrld!");
		when(dictionary.contains("Hello")).thenReturn(true);
		when(dictionary.contains("wxrld")).thenReturn(false);
		when(prompt.confirm(eq(subtitle.getContent()), any(Word.class), anyString())).thenReturn(new UserPromptAnswer("world", true));
		checkSpelling.correct(subtitle);
		assertEquals("Hello, world!", subtitle.getContent());
		verify(prompt).confirm(anyString(), any(Word.class), anyString());
		
		// Test for saved choice
		subtitle = new Subtitle(0, 2000, "Hello, wxrld!");
		checkSpelling.correct(subtitle);
		assertEquals("Hello, world!", subtitle.getContent());
		verify(prompt).confirm(anyString(), any(Word.class), anyString());
	}
	
	/**
	 * Test method for {@link CheckSpelling#correct(Subtitle)} with a canceled replacement from user prompt.
	 */
	@Test
	public void testCorrectPromptCancel () {
		Subtitle subtitle = new Subtitle(0, 2000, "Hello, wxrld!");
		when(dictionary.contains("Hello")).thenReturn(true);
		when(dictionary.contains("wxrld")).thenReturn(false);
		when(prompt.confirm(eq(subtitle.getContent()), any(Word.class), anyString())).thenReturn(new UserPromptAnswer("", true, true));
		checkSpelling.correct(subtitle);
		assertEquals("Hello, wxrld!", subtitle.getContent());
		verify(prompt).confirm(anyString(), any(Word.class), anyString());
		
		// Test for saved choice
		subtitle = new Subtitle(0, 2000, "Hello, wxrld!");
		checkSpelling.correct(subtitle);
		assertEquals("Hello, wxrld!", subtitle.getContent());
		verify(prompt).confirm(anyString(), any(Word.class), anyString());
	}
	
	/**
	 * Test method for {@link CheckSpelling#correct(Subtitle)} with a replacement that should not be remembered.
	 */
	@Test
	public void testCorrectPromptNoRemember () {
		Subtitle subtitle = new Subtitle(0, 2000, "Hello, wxrld!");
		when(dictionary.contains("Hello")).thenReturn(true);
		when(dictionary.contains("wxrld")).thenReturn(false);
		when(prompt.confirm(eq(subtitle.getContent()), any(Word.class), anyString())).thenReturn(new UserPromptAnswer("world", false));
		checkSpelling.correct(subtitle);
		assertEquals("Hello, world!", subtitle.getContent());
		assertEquals("Hello, world!", subtitle.getContent());
		verify(prompt).confirm(anyString(), any(Word.class), anyString());
		
		// Test for saved choice
		subtitle = new Subtitle(0, 2000, "Hello, wxrld!");
		checkSpelling.correct(subtitle);
		assertEquals("Hello, world!", subtitle.getContent());
		verify(prompt, times(2)).confirm(anyString(), any(Word.class), anyString());
		
	}
}
