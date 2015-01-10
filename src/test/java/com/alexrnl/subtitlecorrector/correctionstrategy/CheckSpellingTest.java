package com.alexrnl.subtitlecorrector.correctionstrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.service.DictionaryManager;
import com.alexrnl.subtitlecorrector.service.UserPrompt;

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
	 * Test method for {@link CheckSpelling#correct(Subtitle)}.
	 */
	@Test @Ignore
	public void testCorrect () {
		fail("Not yet implemented"); // TODO
	}
}
