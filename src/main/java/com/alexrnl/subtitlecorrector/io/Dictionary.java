package com.alexrnl.subtitlecorrector.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.commons.io.IOUtils;

/**
 * This class represent a dictionary.<br />
 * A dictionary is a {@link Set} of words ({@link String Strings}) which can be loaded or saved from
 * the disk.<br />
 * A valid dictionary file is composed of one word per line, each word will be an entry of the set.<br />
 * A dictionary can be case sensitive, or case insensitive: when the case insensitive mode is used,
 * a provided {@link Locale} is used to make all words loaded lower case.
 * @author Alex
 */
public class Dictionary {
	/** Logger */
	private static final Logger	LG	= Logger.getLogger(Dictionary.class.getName());
	
	/** The dictionary with the words */
	private final Set<String>	dictionary;
	/** The path to the dictionary file */
	private final Path			dictionaryFile;
	/** The character set of the file */
	private final Charset		charSet;
	/** <code>true</code> if word can be added to the dictionary */
	private final boolean		editable;
	/** <code>true</code> if the dictionary is case sensitive */
	private final Locale		caseInsensitive;
	/** <code>true</code> if the dictionary has been updated with new words since the last save/load */
	private boolean				updated;
	
	/**
	 * Constructor #1.<br />
	 * Load the word from the specified file, using UTF-8 character set.
	 * @param dictionaryFile
	 *        the file to read.
	 * @throws IOException
	 *         if there was an issue while reading the file.
	 */
	public Dictionary (final Path dictionaryFile) throws IOException {
		this(dictionaryFile, false);
	}
	
	/**
	 * Constructor #2.<br />
	 * Load the word from the specified file, using UTF-8 character set.
	 * @param dictionaryFile
	 *        the file to read.
	 * @param editable
	 *        <code>true</code> if the dictionary can be updated with new words.
	 * @throws IOException
	 *         if there was an issue while reading the file.
	 */
	public Dictionary (final Path dictionaryFile, final boolean editable) throws IOException {
		this(dictionaryFile, StandardCharsets.UTF_8, editable);
	}
	
	/**
	 * Constructor #3.<br />
	 * @param dictionaryFile
	 *        the file to read.
	 * @param charSet
	 *        the character set to use for reading the file.
	 * @param editable
	 *        <code>true</code> if the dictionary can be updated with new words.
	 * @throws IOException
	 *         if there was an issue while reading the file.
	 */
	public Dictionary (final Path dictionaryFile, final Charset charSet, final boolean editable) throws IOException {
		this(dictionaryFile, charSet, editable, null);
	}
	
	/**
	 * Constructor #4.<br />
	 * @param dictionaryFile
	 *        the file to read.
	 * @param charSet
	 *        the character set to use for reading the file.
	 * @param editable
	 *        <code>true</code> if the dictionary can be updated with new words.
	 * @param caseInsensitive
	 *        if non-<code>null</code>, then the dictionary will be case insensitive; using the
	 *        locale defined.
	 * @throws IOException
	 *         if there was an issue while reading the file.
	 */
	public Dictionary (final Path dictionaryFile, final Charset charSet, final boolean editable, final Locale caseInsensitive) throws IOException {
		super();
		Objects.requireNonNull(dictionaryFile);
		Objects.requireNonNull(charSet);
		
		if (!Files.exists(dictionaryFile) || !Files.isReadable(dictionaryFile)) {
			throw new IllegalArgumentException("File " + dictionaryFile + " does not exists or cannot be read");
		}
		if (editable && !Files.isWritable(dictionaryFile)) {
			throw new IllegalArgumentException("File " + dictionaryFile + " cannot be write whereas" +
					"it must be editable");
		}
		
		this.dictionary = new TreeSet<>();
		this.editable = editable;
		this.caseInsensitive = caseInsensitive;
		this.dictionaryFile = dictionaryFile;
		this.charSet = charSet;
		load();
	}
	
	/**
	 * Load the content of the file in the set.<br />
	 * Uses the character set defined in the constructor.
	 * @throws IOException
	 *         if there was an issue while reading the file.
	 */
	private void load () throws IOException {
		dictionary.clear();
		try (BufferedReader reader = Files.newBufferedReader(dictionaryFile, charSet)) {
			for (;;) {
				dictionary.add(getLowerCaseWord(IOUtils.readLine(reader).trim()));
			}
		} catch (final EOFException e) {
			if (LG.isLoggable(Level.INFO)) {
				LG.info("Finished reading dictionary file " + dictionaryFile
						+ " (" + dictionary.size() + " words)");
			}
		} catch (final IOException e) {
			LG.warning("Exception while reading dictionary file " + dictionaryFile + ": "
					+ ExceptionUtils.display(e));
			throw e;
		}
		updated = false;
	}
	
	/**
	 * Return the word to use, depending if the dictionary is case insensitive or not.
	 * @param word
	 *        the actual word.
	 * @return the lower case version of the word, if the dictionary is case sensitive.
	 */
	private String getLowerCaseWord (final String word) {
		return isCaseSensitive() ? word : word.toLowerCase(caseInsensitive);
	}
	
	/**
	 * Save the content of the dictionary to the disk.<br />
	 * This operation fails if the dictionary has been marked as non-editable.
	 * @throws IOException
	 *         if there was an issue while writing the data.
	 */
	public void save () throws IOException {
		if (!editable) {
			throw new IllegalStateException("Cannot save dictionary which is set as non-editable");
		}
		try (BufferedWriter writer = Files.newBufferedWriter(dictionaryFile, charSet)) {
			for (final String word : dictionary) {
				writer.write(word);
				writer.write(System.lineSeparator());
			}
		} catch (final IOException e) {
			LG.warning("Exception while writing dictionary file " + dictionaryFile + ": "
					+ ExceptionUtils.display(e));
			throw e;
		}
		updated = false;
	}
	
	/**
	 * Return the attribute editable.
	 * @return the attribute editable.
	 */
	public boolean isEditable () {
		return editable;
	}
	
	/**
	 * Return the attribute updated.
	 * @return the attribute updated.
	 */
	public boolean isUpdated () {
		return updated;
	}
	
	/**
	 * Return <code>true</code> if the dictionary is case sensitive.
	 * @return <code>true</code> if the dictionary is case sensitive.
	 */
	public boolean isCaseSensitive () {
		return caseInsensitive == null;
	}
	
	/**
	 * Return the number of words in the dictionary.
	 * @return the number of words.
	 */
	public int size () {
		return dictionary.size();
	}
	
	/**
	 * Check if the word is in the dictionary.
	 * @param word
	 *        the word to check.
	 * @return <code>true</code> if the word is in the dictionary.
	 */
	public boolean contains (final String word) {
		if (word == null) {
			return false;
		}
		return dictionary.contains(getLowerCaseWord(word));
	}
	
	/**
	 * Add the specified word to the dictionary.<br />
	 * @param word
	 *        the word to add to the dictionary.
	 * @return <code>true</code> if the word has been added to the set, <code>false</code> if the
	 *         world was already in it.
	 */
	public boolean addWord (final String word) {
		final String wordToAdd = getLowerCaseWord(Objects.requireNonNull(word).trim());
		if (dictionary.contains(wordToAdd)) {
			return false;
		}
		updated = dictionary.add(wordToAdd);
		return updated;
	}
}
