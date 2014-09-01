package com.alexrnl.subtitlecorrector.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alexrnl.commons.io.IOUtils;
import com.alexrnl.subtitlecorrector.io.Dictionary;

/**
 * Test suite for the {@link DictionaryManager} class.
 * @author Alex
 */
public class DictionaryManagerTest {
	/** The path where to load the custom dictionaries */
	private static Path pathToCustom;
	/** The path to the custom dictionary 1 */
	private static Path pathToCustom1;
	/** The path to the custom dictionary 2 */
	private static Path pathToCustom2;
	
	/** The dictionary manager to test */
	private DictionaryManager manager;
	
	/**
	 * Create the custom dictionaries which will be used in the test.
	 * @throws IOException
	 *         if there was an error while creating the temporary custom dictionaries.
	 */
	@BeforeClass
	public static void setUpBeforeClass () throws IOException {
		pathToCustom = Files.createTempDirectory("customDictionaries");
		pathToCustom1 = Files.createTempFile(pathToCustom, "custom1", DictionaryManager.DICTIONARY_EXTENSION);
		pathToCustom2 = Files.createTempFile(pathToCustom, "custom2", DictionaryManager.DICTIONARY_EXTENSION);
		pathToCustom.toFile().deleteOnExit();
		pathToCustom1.toFile().deleteOnExit();
		pathToCustom2.toFile().deleteOnExit();
		final Dictionary customDictionary1 = new Dictionary(pathToCustom1, true);
		final Dictionary customDictionary2 = new Dictionary(pathToCustom2, true);
		customDictionary1.addWord("helloworld");
		customDictionary2.addWord("abaldr");
		customDictionary1.save();
		customDictionary2.save();
	}
	
	/**
	 * Set up test attributes.
	 * @throws IOException
	 *         if there was an error while loading the dictionaries.
	 * @throws URISyntaxException
	 *         if the URI is badly formatted.
	 */
	@Before
	public void setUp () throws IOException, URISyntaxException {
		manager = new DictionaryManager(Paths.get(DictionaryManager.class.getResource("/dictionary").toURI()), pathToCustom);
	}
	
	/**
	 * Test method for {@link DictionaryManager#getLocaleDictionaries()}.
	 */
	@Test
	public void testGetLocaleDictionaries () {
		assertEquals(2, manager.getLocaleDictionaries().size());
		assertNotNull(manager.getLocaleDictionaries().get(Locale.FRENCH));
		assertNotNull(manager.getLocaleDictionaries().get(Locale.ENGLISH));
	}
	
	/**
	 * Test method for {@link DictionaryManager#getCustomDictionaries()}.
	 */
	@Test
	public void testGetCustomDictionaries () {
		assertEquals(2, manager.getCustomDictionaries().size());
	}
	
	/**
	 * Test method for {@link DictionaryManager#startSession(SessionParameters)},
	 * {@link DictionaryManager#stopSession()}, and {@link DictionaryManager#contains(String)}.
	 * @throws IOException
	 *         if an IO operation fails.
	 */
	@Test
	public void testSession () throws IOException {
		final SessionParameters sessionParameters = new SessionParameters();
		// Test with no dictionaries
		sessionParameters.setLocale(null);
		manager.startSession(sessionParameters);
		assertFalse(manager.contains("helloworld"));
		assertFalse(manager.contains("abaldr"));
		assertFalse(manager.contains("ldr"));
		assertFalse(manager.contains("mot"));
		assertTrue(manager.addWord("ldr"));
		assertTrue(manager.contains("ldr"));
		manager.stopSession();
		
		// Test with only French locale
		sessionParameters.setLocale(Locale.FRENCH);
		manager.startSession(sessionParameters);
		assertFalse(manager.contains("helloworld"));
		assertFalse(manager.contains("abaldr"));
		assertTrue(manager.contains("mot"));
		manager.stopSession();
		
		// Test with one custom dictionary
		sessionParameters.setLocale(null);
		sessionParameters.addCustomDictionay(IOUtils.getFilename(pathToCustom1));
		manager.startSession(sessionParameters);
		assertFalse(manager.contains("mot"));
		assertTrue(manager.contains("helloworld"));
		manager.stopSession();
		
		// Test with one custom (updated) and the French locale
		sessionParameters.setLocale(Locale.FRENCH);
		sessionParameters.addCustomDictionay(IOUtils.getFilename(pathToCustom2));
		manager.startSession(sessionParameters);
		assertTrue(manager.contains("mot"));
		assertTrue(manager.contains("abaldr"));
		assertFalse(manager.contains("aba"));
		assertTrue(manager.addWord(IOUtils.getFilename(pathToCustom2), "aba"));
		assertFalse(manager.addWord("MAn", "aba"));
		assertTrue(manager.contains("aba"));
		manager.stopSession();
		
		// Check that the custom dictionary has been saved
		final Dictionary dictionary = new Dictionary(pathToCustom2);
		assertEquals(2, dictionary.size());
		assertTrue(dictionary.contains("aba"));
		assertTrue(dictionary.contains("abaldr"));
	}
	
	/**
	 * Test method for {@link DictionaryManager#contains(String)}.
	 */
	@Test(expected = IllegalStateException.class)
	public void testContainsNoSession () {
		manager.contains("ldr");
	}
	
}
