package com.alexrnl.subtitlecorrector.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
public abstract class SubtitleWriter {
	/** Logger */
	private static final Logger	LG	= Logger.getLogger(SubtitleWriter.class.getName());
	
	/** The character set used for writing the subtitles */
	private final Charset		charSet;
	
	/**
	 * Constructor #1.<br />
	 * Default constructor, uses {@link StandardCharsets#UTF_8 UTF-8} for writing the subtitles.
	 */
	public SubtitleWriter () {
		this(StandardCharsets.UTF_8);
	}
	
	/**
	 * Constructor #2.<br />
	 * @param charSet
	 *        the character set to use for writing the subtitles.
	 */
	public SubtitleWriter (final Charset charSet) {
		super();
		this.charSet = charSet;
	}
	
	/**
	 * Return the current character set used by this writer.
	 * @return the charset used.
	 */
	protected Charset getCharSet () {
		return charSet;
	}

	/**
	 * Write the generated subtitle file to the path specified in the {@link SubtitleFile} class.<br />
	 * @param file
	 *        the subtitle file to write.
	 * @throws IOException
	 *         if there was an issue while writing the subtitle.
	 */
	public void writeFile (final SubtitleFile file) throws IOException {
		writeFile(file, file.getFile());
	}

	/**
	 * Write the generated subtitle file to a specified file.<br />
	 * This method is synchronized to avoid write files simultaneously.
	 * @param file
	 *        the subtitle file to write.
	 * @param target
	 *        the target location.
	 * @throws IOException
	 *         if there was an issue while writing the subtitle.
	 */
	public synchronized void writeFile (final SubtitleFile file, final Path target) throws IOException {
		if (Files.isDirectory(target)) {
			LG.warning(target + " is a directory, it will not be overwritten");
			throw new IllegalArgumentException(target + " is a directory");
		}
		
		if (Files.exists(target) && !Files.isWritable(target)) {
			LG.warning(target + " exists and is not writable");
			throw new IllegalArgumentException(target + " already exists is not writable");
		}
		
		if (Files.exists(target) && LG.isLoggable(Level.INFO)) {
			LG.info("File " + target + " will be overwritten");
		}
		
		try (BufferedWriter writer = Files.newBufferedWriter(target, charSet, StandardOpenOption.CREATE)) {
			if (charSet.equals(StandardCharsets.UTF_8)) {
				writer.write(IOUtils.UNICODE_BYTE_ORDER_MARK);
			}
			writeHeader(file, writer);
			for (final Subtitle subtitle : file) {
				writeSubtitle(subtitle, writer);
			}
			writeFooter(file, writer);
		} catch (final IOException e) {
			LG.warning("Problem while writing the file: " + ExceptionUtils.display(e));
			throw e;
		}
		
		if (LG.isLoggable(Level.INFO)) {
			LG.info(target + " has been successfully written");
		}
	}

	/**
	 * Write the header of the subtitle in the file.<br />
	 * May be override by specific implementations.
	 * @param file
	 *        the subtitle file to write.
	 * @param writer
	 *        the writer to use.
	 * @throws IOException
	 *         if there was a problem while writing the subtitle.
	 */
	protected void writeHeader (final SubtitleFile file, final BufferedWriter writer) throws IOException {
		// Do nothing
	}
	
	/**
	 * Write the footer of the subtitle file.<br />
	 * May be override by specific implementations.
	 * @param file
	 *        the subtitle file to write.
	 * @param writer
	 *        the writer to use.
	 * @throws IOException
	 *         if there was a problem while writing the subtitle.
	 */
	protected void writeFooter (final SubtitleFile file, final BufferedWriter writer) throws IOException {
		// Do nothing.
	}
	
	/**
	 * Write the specified subtitle.<br />
	 * @param subtitle
	 *        the subtitle to write.
	 * @param writer
	 *        the writer to use.
	 * @throws IOException
	 *         if there was a problem while writing the subtitle.
	 */
	protected abstract void writeSubtitle (Subtitle subtitle, BufferedWriter writer) throws IOException;
}
