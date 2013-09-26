package com.alexrnl.subtitlecorrector.common;

import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test suite for the {@link TranslationKeys} class.
 * @author Alex
 */
public class TranslationKeysTest {
	
	/**
	 * Test that the return values of the method is correct.
	 */
	@Test
	public void testTranslations () {
		assertEquals("subtitlecorrector.strategy", KEYS.strategy().toString());
		assertEquals("subtitlecorrector.strategy.letterreplacement", KEYS.strategy().letterReplacement().toString());
		assertEquals("subtitlecorrector.strategy.letterreplacement.description", KEYS.strategy().letterReplacement().description());
		assertEquals("subtitlecorrector.strategy.letterreplacement.originalletter", KEYS.strategy().letterReplacement().originalLetter());
		assertEquals("subtitlecorrector.strategy.letterreplacement.newletter", KEYS.strategy().letterReplacement().newLetter());
		assertEquals("subtitlecorrector.strategy.letterreplacement.onlymissingfromdictionary", KEYS.strategy().letterReplacement().onlyMissingFromDictionary());
		assertEquals("subtitlecorrector.strategy.letterreplacement.promptbeforecorrecting", KEYS.strategy().letterReplacement().promptBeforeCorrecting());
		assertEquals("subtitlecorrector.strategy.fixpunctuation", KEYS.strategy().fixPunctuation().toString());
		assertEquals("subtitlecorrector.strategy.fixpunctuation.description", KEYS.strategy().fixPunctuation().description());
		assertEquals("subtitlecorrector.strategy.fixpunctuation.locale", KEYS.strategy().fixPunctuation().locale());
		
		assertEquals("subtitlecorrector.mainwindow.title", KEYS.mainWindow().title());
		assertEquals("subtitlecorrector.mainwindow.subtitleLabel", KEYS.mainWindow().subtitleLabel());
		assertEquals("subtitlecorrector.mainwindow.subtitleButton", KEYS.mainWindow().subtitleButton());
		assertEquals("subtitlecorrector.mainwindow.strategyLabel", KEYS.mainWindow().strategyLabel());
	}
}
