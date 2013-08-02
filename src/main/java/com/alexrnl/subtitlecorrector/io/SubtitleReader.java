package com.alexrnl.subtitlecorrector.io;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.commons.io.IOUtils;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.common.SubtitleFile;

/**
 * Abstract class for a subtitle reader.<br />
 * @author Alex
 */
public abstract class SubtitleReader {
	/** Logger */
	private static Logger	lg	= Logger.getLogger(SubtitleReader.class.getName());
	
	/** The charset of the subtitle files read */
	private final Charset			charSet;
	
	/**
	 * Constructor #1.<br />
	 * Default constructor, uses {@link StandardCharsets#UTF_8 UTF-8} as charset.
	 */
	public SubtitleReader () {
		this(StandardCharsets.UTF_8);
	}
	
	/**
	 * Constructor #.<br />
	 * @param charSet
	 *        the character set to use for reading the subtitles.
	 */
	public SubtitleReader (final Charset charSet) {
		super();
		this.charSet = charSet;
	}
	
	/**
	 * Return the current character set used by this reader.
	 * @return the charset used.
	 */
	protected Charset getCharSet () {
		return charSet;
	}
	
	/**
	 * Read the specified file and return the loaded {@link SubtitleFile}.<br />
	 * This method is synchronized, to avoid read files from different threads simultaneously.
	 * @param file
	 *        the file to read.
	 * @return the subtitle file, loaded.
	 * @throws IOException
	 *         if there was a problem while reading the file.
	 */
	public synchronized SubtitleFile readFile (final Path file) throws IOException {
		if (!Files.exists(file) || !Files.isReadable(file)) {
			lg.warning("File " + file + " does not exists or cannot be read");
			throw new IllegalArgumentException("The file does not exist or cannot be read");
		}
		if (lg.isLoggable(Level.INFO)) {
			lg.fine("Loading file " + file);
		}
		
		SubtitleFile subtitleFile = null;
		
		try (final BufferedReader reader = Files.newBufferedReader(file, charSet)) {
			try {
				reader.mark(1);
				if (reader.read() != IOUtils.UNICODE_BYTE_ORDER_MARK.charValue()) {
					reader.reset();
				}
				subtitleFile = readHeader(file, reader);
				for (;;) {
					subtitleFile.add(readSubtitle(subtitleFile, reader));
				}
			} catch (final EOFException e) {
				if (lg.isLoggable(Level.INFO)) {
					lg.info("Finished reading file " + file);
				}
				readFooter(subtitleFile, reader);
			}
		} catch (final IOException e) {
			lg.warning("Problem while reading subitle file: " + ExceptionUtils.display(e));
			throw e;
		}
		
		return subtitleFile;
	}
	
	/**
	 * Read the header of the subtitle file and build the {@link SubtitleFile} to hold the data.<br />
	 * May be override by specific subtitle implementations.
	 * @param file
	 *        the file being read.
	 * @param reader
	 *        the reader to use.
	 * @return the subtitle file to use to store the data.
	 * @throws IOException
	 *         if there was a problem while reading the file.
	 */
	protected SubtitleFile readHeader (final Path file, final BufferedReader reader) throws IOException {
		return new SubtitleFile(file);
	}
	
	/**
	 * Read the footer of the subtitle file.<br />
	 * May be override by specific implementations.
	 * @param subtitleFile
	 *        the subtitle file being read.
	 * @param reader
	 *        the reader to use.
	 * @throws IOException
	 *         if there was a problem while reading the file.
	 */
	protected void readFooter (final SubtitleFile subtitleFile, final BufferedReader reader) throws IOException {
		// Do nothing
	}
	
	/**
	 * Read a single subtitle of the file.
	 * @param subtitleFile
	 *        the subtitle file being read.
	 * @param reader
	 *        the reader to use.
	 * @return The subtitle read.
	 * @throws IOException
	 *         if there was a problem while reading the file.
	 */
	protected abstract Subtitle readSubtitle (final SubtitleFile subtitleFile, final BufferedReader reader) throws IOException;
}
