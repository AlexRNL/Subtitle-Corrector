package com.alexrnl.subtitlecorrector.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for the common package.
 * @author Alex
 */
@RunWith(Suite.class)
@SuiteClasses({ SubtitleTest.class, SubtitleFileTest.class, TranslationKeysTest.class })
public class CommonTests {
	
}
