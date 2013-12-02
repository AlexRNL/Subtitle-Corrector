package com.alexrnl.subtitlecorrector.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the {@link SessionParameters} class.
 * @author Alex
 */
public class SessionParametersTest {
	/** The default session parameters */
	private SessionParameters	defaultParameters;
	/** Other session parameters */
	private SessionParameters	otherParameters;

	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		defaultParameters = new SessionParameters();
		otherParameters = new SessionParameters();
		otherParameters.setLocale(Locale.ENGLISH);
		otherParameters.addCustomDictionay("MAD");
	}
	
	/**
	 * Test method for {@link SessionParameters#getLocale()}.
	 */
	@Test
	public void testGetLocale () {
		assertEquals(Locale.getDefault(), defaultParameters.getLocale());
		assertEquals(Locale.ENGLISH, otherParameters.getLocale());
	}
	
	/**
	 * Test method for {@link SessionParameters#setLocale(Locale)}.
	 */
	@Test
	public void testSetLocale () {
		defaultParameters.setLocale(Locale.UK);
		otherParameters.setLocale(Locale.CANADA);
		assertEquals(Locale.UK, defaultParameters.getLocale());
		assertEquals(Locale.CANADA, otherParameters.getLocale());
	}
	
	/**
	 * Test method for {@link SessionParameters#getCustomDictionaries()}.
	 */
	@Test
	public void testGetCustomDictionaries () {
		assertTrue(defaultParameters.getCustomDictionaries().isEmpty());
		assertEquals(1, otherParameters.getCustomDictionaries().size());
		assertEquals("MAD", otherParameters.getCustomDictionaries().iterator().next());
	}
	
	/**
	 * Test method for {@link SessionParameters#getCustomDictionaries()}.<br />
	 * Check that the custom dictionaries cannot be edited outside of the class.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testUpdateCustomDictionaries () {
		defaultParameters.getCustomDictionaries().add("My dictionary");
	}
	
	/**
	 * Test method for {@link SessionParameters#addCustomDictionay(String)}.
	 */
	@Test
	public void testAddCustomDictionay () {
		assertEquals(1, otherParameters.getCustomDictionaries().size());
		otherParameters.addCustomDictionay("ABA");
		assertEquals(2, otherParameters.getCustomDictionaries().size());
		for (final String dictionary : otherParameters.getCustomDictionaries()) {
			if (!dictionary.equals("ABA") && ! dictionary.equals("MAD")) {
				fail("Unexpected custom dictionary found: " + dictionary);
			}
		}
	}
	
	/**
	 * Test method for {@link SessionParameters#removeCustomDictionay(String)}.
	 */
	@Test
	public void testRemoveCustomDictionay () {
		assertEquals(1, otherParameters.getCustomDictionaries().size());
		otherParameters.removeCustomDictionay("MAD");
		assertEquals(0, otherParameters.getCustomDictionaries().size());
	}
	
	/**
	 * Test method for {@link SessionParameters#clearCustomDictionaries()}.
	 */
	@Test
	public void testClearCustomDictionaries () {
		assertEquals(1, otherParameters.getCustomDictionaries().size());
		otherParameters.clearCustomDictionaries();
		assertEquals(0, otherParameters.getCustomDictionaries().size());
	}
	
	/**
	 * Test method for {@link SessionParameters#toString()}.
	 */
	@Test
	public void testToString () {
		assertEquals("SessionParameters [locale=" + Locale.getDefault().toString() + ", customDictionaries=[]]",
				defaultParameters.toString());
		assertEquals("SessionParameters [locale=en, customDictionaries=[MAD]]",
				otherParameters.toString());
	}
}
