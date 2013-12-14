package com.alexrnl.subtitlecorrector.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the {@link SessionStateAdapter} class.
 * @author Alex
 */
public class SessionStateAdapterTest {
	/** The adapter to test */
	private SessionStateAdapter sessionAdapter;
	
	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		sessionAdapter = new SessionStateAdapter();
	}
	
	/**
	 * Test method for {@link SessionStateAdapter#startSession(SessionParameters)}.
	 */
	@Test
	public void testStartSession () {
		final SessionParameters parameters = mock(SessionParameters.class);
		sessionAdapter.startSession(parameters);
		verifyZeroInteractions(parameters);
	}
	
	/**
	 * Test method for {@link SessionStateAdapter#stopSession()}.
	 */
	@Test
	public void testStopSession () {
		sessionAdapter.stopSession();
	}
}
