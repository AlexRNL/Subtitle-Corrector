package com.alexrnl.subtitlecorrector.correctionstrategy;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.alexrnl.subtitlecorrector.common.Subtitle;

/**
 * Test suite for the {@link FixPunctuation} class.
 * @author Alex
 */
public class FixPunctuationTest {
	/** The punctuation fixer strategy */
	private FixPunctuation	fixPunctuation;
	
	/**
	 * Set up test attributes.
	 * @throws URISyntaxException
	 *         if there is an issue when parsing the path.
	 * @throws IOException
	 *         if there is an issue when loading the rules.
	 */
	@Before
	public void setUp () throws IOException, URISyntaxException {
		fixPunctuation = new FixPunctuation(Paths.get(FixPunctuation.class.getResource("/punctuation").toURI()));
	}
	
	/**
	 * Test method for {@link FixPunctuation#getParameters()}.
	 */
	@Test
	public void testGetParameters () {
		fail("Not yet implemented"); // TODO
	}
	
	/**
	 * Test method for {@link FixPunctuation#getParameterByName(String)}.
	 */
	@Test
	public void testGetParameterByName () {
		fail("Not yet implemented"); // TODO
	}
	
	/**
	 * Test method for {@link FixPunctuation#startSession()}.
	 */
	@Test
	public void testStartSession () {
		fail("Not yet implemented"); // TODO
	}
	
	/**
	 * Test method for {@link FixPunctuation#stopSession()}.
	 */
	@Test
	public void testStopSession () {
		fail("Not yet implemented"); // TODO
	}
	
	/**
	 * Test method for {@link FixPunctuation#correct(Subtitle)}.
	 */
	@Test
	public void testCorrect () {
		fail("Not yet implemented"); // TODO
	}
}
