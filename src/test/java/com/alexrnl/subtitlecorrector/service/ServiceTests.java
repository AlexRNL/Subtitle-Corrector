package com.alexrnl.subtitlecorrector.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for the service package.
 * @author Alex
 */
@RunWith(Suite.class)
@SuiteClasses({ DictionaryManagerTest.class, SessionStateAdapterTest.class,
		SessionManagerTest.class, SessionParametersTest.class, SubtitleProviderTest.class,
		UserPromptAnswerTest.class })
public class ServiceTests {
	
}
