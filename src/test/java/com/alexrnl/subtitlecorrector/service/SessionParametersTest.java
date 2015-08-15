package com.alexrnl.subtitlecorrector.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.alexrnl.subtitlecorrector.common.SubtitleFile;

/**
 * Test suite for the {@link SessionParameters} class.
 * @author Alex
 */
public class SessionParametersTest {
	/** The default session parameters */
	private SessionParameters	defaultParameters;
	/** Other session parameters */
	private SessionParameters	otherParameters;
	/** The mocked subtitle file */
	@Mock
	private SubtitleFile		subtitleFile;

	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		initMocks(this);
		defaultParameters = new SessionParameters();
		otherParameters = new SessionParameters();
		otherParameters.setLocale(Locale.ENGLISH);
		otherParameters.addCustomDictionay("MAD");
		otherParameters.setSubtitleFile(subtitleFile);
		otherParameters.setCurrentCorrectionIndex(88);
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
	 * Test method for {@link SessionParameters#getSubtitleFile}.
	 */
	@Test
	public void testGetSubtitleFile () {
		assertNull(defaultParameters.getSubtitleFile());
		assertEquals(subtitleFile, otherParameters.getSubtitleFile());
	}
	
	/**
	 * Test method for {@link SessionParameters#setSubtitleFile(SubtitleFile)}.
	 */
	@Test
	public void testSetSubtitleFile () {
		defaultParameters.setSubtitleFile(subtitleFile);
		final SubtitleFile subtitleFile2 = mock(SubtitleFile.class);
		otherParameters.setSubtitleFile(subtitleFile2);
		assertEquals(subtitleFile, defaultParameters.getSubtitleFile());
		assertEquals(subtitleFile2, otherParameters.getSubtitleFile());
	}
	
	/**
	 * Test method for {@link SessionParameters#getCurrentCorrectionIndex()}.
	 */
	@Test
	public void testGetCurrentCorrectionIndex () {
		assertEquals(-1, defaultParameters.getCurrentCorrectionIndex());
		assertEquals(88, otherParameters.getCurrentCorrectionIndex());
	}
	
	/**
	 * Test method for {@link SessionParameters#setCurrentCorrectionIndex(int)}.
	 */
	@Test
	public void testSetCurrentCorrectionIndex () {
		defaultParameters.setCurrentCorrectionIndex(28);
		otherParameters.setCurrentCorrectionIndex(128);
		assertEquals(28, defaultParameters.getCurrentCorrectionIndex());
		assertEquals(128, otherParameters.getCurrentCorrectionIndex());
	}
	
	/**
	 * Test method for {@link SessionParameters#toString()}.
	 */
	@Test
	public void testToString () {
		assertEquals("SessionParameters [locale=" + Locale.getDefault().toString() + ", customDictionaries=[], subtitleFile=null, currentCorrectionIndex=-1]",
				defaultParameters.toString());
		when(subtitleFile.toString()).thenReturn("mocked.srt");
		assertEquals("SessionParameters [locale=en, customDictionaries=[MAD], subtitleFile=mocked.srt, currentCorrectionIndex=88]",
				otherParameters.toString());
	}
}
