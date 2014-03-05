package com.alexrnl.subtitlecorrector.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Test suite for the {@link SessionManager} class.
 * @author Alex
 */
public class SessionManagerTest {
	/** The session manager to test */
	private SessionManager			manager;
	/** A mocked listener */
	@Mock
	private SessionStateListener	listener;
	
	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		initMocks(this);
		manager = new SessionManager();
		manager.addSessionListener(listener);
	}
	
	/**
	 * Check that the list returned by the manager cannot be modified.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testGetSessionListenerModification () {
		manager.getSessionListeners().clear();
	}
	
	/**
	 * Test method for {@link SessionManager#addSessionListener(SessionStateListener)}.
	 */
	@Test
	public void testAddSessionListener () {
		final Collection<SessionStateListener> currentListeners = manager.getSessionListeners();
		final SessionStateListener secondListener = mock(SessionStateListener.class);
		assertTrue(manager.addSessionListener(secondListener));
		
		final Collection<SessionStateListener> newListeners = new ArrayList<>(manager.getSessionListeners());
		newListeners.removeAll(currentListeners);
		assertEquals(Arrays.asList(secondListener), newListeners);
		
		assertFalse(manager.addSessionListener(secondListener));
	}
	
	/**
	 * Check that a <code>null</code> listener cannot be added.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddNullSessionListener () {
		manager.addSessionListener(null);
	}
	
	/**
	 * Check that a the current manager cannot be added as a listener.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddThisSessionListener () {
		manager.addSessionListener(manager);
	}
	
	/**
	 * Test method for {@link SessionManager#removeSessionListener(SessionStateListener)}.
	 */
	@Test
	public void testRemoveSessionListener () {
		assertTrue(manager.removeSessionListener(listener));
		assertFalse(manager.removeSessionListener(manager));
		assertFalse(manager.removeSessionListener(null));
	}
	
	/**
	 * Test method for {@link SessionManager#isInSession()}.
	 */
	@Test
	public void testIsInSession () {
		assertFalse(manager.isInSession());
		manager.startSession(null);
		assertTrue(manager.isInSession());
	}
	
	/**
	 * Test method for {@link SessionManager#startSession(SessionParameters)}.
	 */
	@Test
	public void testStartSession () {
		final SessionParameters parameters = new SessionParameters();
		manager.startSession(parameters);
		assertTrue(manager.isInSession());
		verify(listener).startSession(parameters);
	}
	
	/**
	 * Test that the manager cannot be start twice.
	 */
	@Test(expected = IllegalStateException.class)
	public void testDoubleStart () {
		manager.startSession(null);
		manager.startSession(null);
	}
	
	/**
	 * Test method for {@link SessionManager#stopSession()}.
	 */
	@Test
	public void testStopSession () {
		manager.startSession(null);
		assertTrue(manager.isInSession());
		manager.stopSession();
		assertFalse(manager.isInSession());
		verify(listener).stopSession();
	}
	
	/**
	 * Test that the manager cannot be stopped twice.
	 */
	@Test(expected = IllegalStateException.class)
	public void testDoubleStop () {
		manager.stopSession();
	}
}
