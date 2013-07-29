package com.alexrnl.subtitlecorrector;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.alexrnl.subtitlecorrector.common.CommonTests;
import com.alexrnl.subtitlecorrector.io.IOTests;

/**
 * Test suite for the subtitle corrector application.
 * @author Alex
 */
@RunWith(Suite.class)
@SuiteClasses({ CommonTests.class, IOTests.class })
public class AllTests {
	
}
