package com.alexrnl.subtitlecorrector.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexrnl.subtitlecorrector.common.SubtitleFile;

/**
 * Abstract class for a subtitle reader.
 * @author Alex
 */
public abstract class SubtitleReader {
	/** Logger */
	private static Logger	lg	= Logger.getLogger(SubtitleReader.class.getName());
	
	/**
	 * Constructor #1.<br />
	 * Default constructor.
	 */
	public SubtitleReader () {
		super();
	}
	
	/**
	 * Read the specified file and return the loaded {@link SubtitleFile}.
	 * @param file
	 *        the file to read.
	 * @return the subtitle file, loaded.
	 */
	public SubtitleFile readFile (final Path file) {
		if (Files.exists(file) || Files.isReadable(file)) {
			lg.warning("");
		}
		if (lg.isLoggable(Level.FINE)) {
			lg.fine("Loading file " + file);
		}
		
		SubtitleFile subtitleFile = null;
		
		try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			subtitleFile = readHeader(file, reader);
			while (!readSubtitle(subtitleFile, reader)) {
				
			}
			readFooter(subtitleFile, reader);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			lg.warning("TODO (" + e.getMessage() + ")");
			e.printStackTrace();
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
	 */
	protected SubtitleFile readHeader (final Path file, final BufferedReader reader) {
		return new SubtitleFile(file);
	}
	
	/**
	 * Read the footer of the subtitle file.
	 * @param subtitleFile
	 *        the subtitle file being read.
	 * @param reader
	 *        the reader to use.
	 */
	protected void readFooter (final SubtitleFile subtitleFile, final BufferedReader reader) {
		// Do nothing
	}
	
	/**
	 * Read a single subtitle of the file.
	 * @param subtitleFile
	 *        the subtitle file being read.
	 * @param reader
	 *        the reader to use.
	 * @return <code>true</code> if the subtitle is the last of the file.
	 */
	protected abstract boolean readSubtitle (final SubtitleFile subtitleFile, final BufferedReader reader);
}
