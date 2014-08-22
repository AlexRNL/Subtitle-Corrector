package com.alexrnl.subtitlecorrector.correctionstrategy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for the Correction Strategy package.
 * @author Alex
 */
@RunWith(Suite.class)
@SuiteClasses({ FixPunctuationTest.class, LetterReplacementTest.class, ParameterTest.class,
		StandardParameterParsersTest.class })
public class CorrectionStrategyTests {
	
}
