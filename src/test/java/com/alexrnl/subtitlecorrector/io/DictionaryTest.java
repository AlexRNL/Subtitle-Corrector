package com.alexrnl.subtitlecorrector.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test suite for the {@link Dictionary} class.
 * @author Alex
 */
public class DictionaryTest {
	/** The file for the dictionary. */
	private static Path	dictionaryFile;
	/** The file for the copy of the dictionary */
	private static Path	dictionaryCopy;
	
	/** A read-only dictionary */
	private Dictionary dictionary;
	/** An editable dictionary */
	private Dictionary editableDictionary;
	
	/**
	 * Copy the dictionary to a temporary file so it can be safely edited.
	 * @throws IOException
	 *         if there is a problem when loading the file.
	 * @throws URISyntaxException
	 *         if the the path is badly formatted.
	 */
	@BeforeClass
	public static void setUpBeforeClass () throws IOException, URISyntaxException {
		dictionaryFile = Paths.get(Dictionary.class.getResource("/dictionary/fr.txt").toURI());
		dictionaryCopy = Files.createTempFile("dictionary", ".txt");
		dictionaryCopy.toFile().deleteOnExit();
		Files.copy(dictionaryFile, dictionaryCopy, StandardCopyOption.REPLACE_EXISTING);
	}
	
	/**
	 * Load the regular dictionary for the tests.
	 */
	private void loadDictionary () {
		try {
			dictionary = new Dictionary(dictionaryFile);
		} catch (final IOException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Load the editable dictionary for the tests.
	 */
	private void loadEditableDictionary () {
		try {
			editableDictionary = new Dictionary(dictionaryCopy, true);
		} catch (final IOException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test method for {@link Dictionary#Dictionary(Path)}.
	 * @throws IOException
	 *         if there is a problem when loading the file.
	 */
	@SuppressWarnings("unused")
	@Test(expected = NullPointerException.class)
	public void testDictionaryNPEPath () throws IOException {
		new Dictionary(null);
	}
	
	/**
	 * Test method for {@link Dictionary#Dictionary(Path)}.
	 * @throws IOException
	 *         if there is a problem when loading the file.
	 */
	@SuppressWarnings("unused")
	@Test(expected = NullPointerException.class)
	public void testDictionaryNPECharSet () throws IOException {
		new Dictionary(Paths.get("dummy", "path"), null, true);
	}
	
	/**
	 * Test method for {@link Dictionary#Dictionary(Path)}.
	 * @throws IOException
	 *         if there is a problem when loading the file.
	 */
	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void testDictionaryIAEPathNotExists () throws IOException {
		new Dictionary(Paths.get("dummy", "path"));
	}
	
	/**
	 * Test method for {@link Dictionary#Dictionary(Path)}.
	 * @throws IOException
	 *         if there is a problem when loading the file.
	 */
	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void testDictionaryIAENotWritable () throws IOException {
		final Path temporaryFile = Files.createTempFile("dictionary", ".txt");
		temporaryFile.toFile().setWritable(false);
		temporaryFile.toFile().deleteOnExit();
		new Dictionary(temporaryFile, StandardCharsets.UTF_8, true);
	}
	
	/**
	 * Test method for {@link Dictionary#save()}.
	 * @throws IOException
	 *         if there is a problem when writing the file.
	 */
	@Test
	public void testSave () throws IOException {
		loadEditableDictionary();
		assertFalse(editableDictionary.contains("zedzfrgtlermforopfz"));
		assertTrue(editableDictionary.addWord("zedzfrgtlermforopfz"));
		editableDictionary.save();
		final Dictionary savedDictionary = new Dictionary(dictionaryCopy);
		assertTrue(savedDictionary.contains("zedzfrgtlermforopfz"));
	}
	
	/**
	 * Test method for {@link Dictionary#save()}.
	 * @throws IOException
	 *         if there is a problem when writing the file.
	 */
	@Test(expected = IllegalStateException.class)
	public void testSaveIllegalStateExcetion () throws IOException {
		loadDictionary();
		dictionary.save();
	}
	
	/**
	 * Test method for {@link Dictionary#isEditable()}.
	 */
	@Test
	public void testIsEditable () {
		loadDictionary();
		loadEditableDictionary();
		assertFalse(dictionary.isEditable());
		assertTrue(editableDictionary.isEditable());
	}
	
	/**
	 * Test method for {@link Dictionary#isUpdated()}.
	 * @throws IOException
	 *         if a save action fails.
	 */
	@Test
	public void testIsUpdated () throws IOException {
		loadDictionary();
		loadEditableDictionary();
		assertFalse(dictionary.isUpdated());
		assertTrue(dictionary.addWord("zedzfrgtforopfz"));
		assertTrue(dictionary.isUpdated());

		assertFalse(editableDictionary.isUpdated());
		assertTrue(editableDictionary.addWord("zedgtlermforop"));
		assertTrue(editableDictionary.isUpdated());
		editableDictionary.save();
		assertFalse(editableDictionary.isUpdated());
	}
	
	/**
	 * Test method for {@link Dictionary#isCaseSensitive()}.
	 */
	@Test
	public void testIsCaseSensitive () {
		loadDictionary();
		loadEditableDictionary();
		assertTrue(dictionary.isCaseSensitive());
		assertTrue(editableDictionary.isCaseSensitive());
	}
	
	/**
	 * Test method for {@link Dictionary#size()}.
	 * @throws IOException
	 *         if there was an error while saving the dictionary.
	 */
	@Test
	public void testSize () throws IOException {
		loadDictionary();
		loadEditableDictionary();
		assertEquals(336531, dictionary.size());
		final int beforeAdd = editableDictionary.size();
		editableDictionary.addWord("ldraba");
		editableDictionary.save();
		assertEquals(beforeAdd + 1, editableDictionary.size());
	}
	
	/**
	 * Test method for {@link Dictionary#contains(String)}.
	 */
	@Test
	public void testContains () {
		loadDictionary();
		assertFalse(dictionary.contains("zedzfrgtlermforo"));
		assertFalse(dictionary.contains(null));
		assertFalse(dictionary.contains(""));
		assertTrue(dictionary.contains("mot"));
	}
	
	/**
	 * Test method for {@link Dictionary#addWord(String)}.
	 */
	@Test
	public void testAddWord () {
		loadDictionary();
		assertFalse(dictionary.addWord("mot"));
		assertFalse(dictionary.contains("zedzfrgtlermforopfz"));
		assertTrue(dictionary.addWord("zedzfrgtlermforopfz"));
		assertTrue(dictionary.contains("zedzfrgtlermforopfz"));
	}
	
	/**
	 * Test method for a case insensitive dictionary.
	 * @throws IOException
	 *         if the dictionary file cannot be read.
	 */
	@Test
	public void testCaseInsensitiveDictionnary () throws IOException {
		final Dictionary caseInsensitiveDictionary = new Dictionary(dictionaryFile, StandardCharsets.UTF_8, false, Locale.FRENCH);
		assertFalse(caseInsensitiveDictionary.isCaseSensitive());
		assertTrue(caseInsensitiveDictionary.contains("mot"));
		assertTrue(caseInsensitiveDictionary.contains("MOT"));
	}
}
