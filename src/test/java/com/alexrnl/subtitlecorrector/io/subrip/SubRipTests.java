package com.alexrnl.subtitlecorrector.io.subrip;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for the subrip package.
 * @author Alex
 */
@RunWith(Suite.class)
@SuiteClasses({ SubRipTest.class, SubRipReaderTest.class, SubRipWriterTest.class })
public class SubRipTests {
	
}
