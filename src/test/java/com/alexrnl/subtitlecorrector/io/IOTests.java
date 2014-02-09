package com.alexrnl.subtitlecorrector.io;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.alexrnl.subtitlecorrector.io.subrip.SubRipTests;

/**
 * Test suite for the io package.
 * @author Alex
 */
@RunWith(Suite.class)
@SuiteClasses({ DictionaryTest.class, SubRipTests.class, SubtitleFormatTest.class })
public class IOTests {
	
}
