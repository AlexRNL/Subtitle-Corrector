package com.alexrnl.subtitlecorrector.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.subtitlecorrector.common.SubtitleFile;
import com.alexrnl.subtitlecorrector.common.TranslationKeys;
import com.alexrnl.subtitlecorrector.io.SubtitleFormat;
import com.alexrnl.subtitlecorrector.io.SubtitleFormatManager;

/**
 * Class which provide a method to load subtitles from a {@link Path}.
 * @author Alex
 */
public class SubtitleProvider {
	/** Logger */
	private static final Logger			LG	= Logger.getLogger(SubtitleProvider.class.getName());
	
	/** The key to the subtitle provider translations */
	private static final com.alexrnl.subtitlecorrector.common.TranslationKeys.SubtitleProvider	TRANSLATION_KEY	= TranslationKeys.KEYS.subtitleProvider();

	/** The user prompt to communicate with the user */
	private final UserPrompt			userPrompt;
	/** The subtitle format manager to use */
	private final SubtitleFormatManager	subtitleFormatManager;
	
	/**
	 * Constructor #1.<br />
	 * @param subtitleFormatManager
	 *        the format manager to use.
	 * @param userPrompt
	 *        the user prompt to communicate with the user.
	 */
	public SubtitleProvider (final SubtitleFormatManager subtitleFormatManager, final UserPrompt userPrompt) {
		super();
		this.subtitleFormatManager = subtitleFormatManager;
		this.userPrompt = userPrompt;
	}
	
	/**
	 * Load the subtitles located under the provided {@link Path}.<br />
	 * If the path is a file, only this file will be loaded. If it is a folder, all files whose
	 * extension are subtitle extension (according to their format) are loaded.
	 * @param workingFiles
	 *        the {@link Path} to load.
	 * @return the map with the subtitle files and their format.
	 */
	public Map<SubtitleFile, SubtitleFormat> loadSubtitles (final Path workingFiles) {
		final boolean exists = Files.exists(workingFiles);
		final boolean reads = Files.isReadable(workingFiles);
		if (!exists || !reads) {
			userPrompt.error(TRANSLATION_KEY.noAccess(), workingFiles);
			LG.severe("Path " + workingFiles + " does " + (exists ? "" : "not") + " exist and can"
					+ (reads ? "" : "not") + " be read");
			return Collections.emptyMap();
		}
		
		// Gather files
		final Set<Path> files = new TreeSet<>();
		if (Files.isDirectory(workingFiles)) {
			try {
				Files.walkFileTree(workingFiles, new SubtitleVisitor(files));
			} catch (final IOException e) {
				userPrompt.error(TRANSLATION_KEY.folderVisitError(), workingFiles);
				LG.warning("Could not retrieve subtitles to process: " + ExceptionUtils.display(e));
				return Collections.emptyMap();
			}
		} else if (Files.isRegularFile(workingFiles)) {
			files.add(workingFiles);
		} else {
			userPrompt.error(TRANSLATION_KEY.notFileNotDirectory(), workingFiles);
			LG.severe(workingFiles + " is not a directory or a file");
			return Collections.emptyMap();
		}
		
		// Read files
		final Map<SubtitleFile, SubtitleFormat> subtitles = new HashMap<>(files.size(), 1.0f);
		for (final Path file : files) {
			final Set<SubtitleFormat> readers = subtitleFormatManager.getFormatByPath(file);
			final SubtitleFormat format;
			
			if (readers.size() == 1) {
				format = readers.iterator().next();
			} else {
				if (readers.isEmpty()) {
					readers.addAll(subtitleFormatManager.getAvailableFormats());
				}
				
				format = userPrompt.askChoice(readers, TRANSLATION_KEY.chooseSubtitleFormat(), file);
			}
			
			if (format == null) {
				continue;
			}
			
			try {
				subtitles.put(format.getReader().readFile(file), format);
			} catch (final IOException e) {
				userPrompt.warning(TRANSLATION_KEY.subtitleFileReadError(), file);
				LG.warning("Exception while parsing file " + file + ": " + ExceptionUtils.display(e));
			}
		}
		
		if (subtitles.isEmpty()) {
			userPrompt.warning(TRANSLATION_KEY.noSubtitleToCorrect());
		}
		return subtitles;
	}
	
	/**
	 * Visitor which add the subtitle file whose format is known.
	 * @author Alex
	 */
	private class SubtitleVisitor extends SimpleFileVisitor<Path> {
		/** The set of files to populate */
		private final Set<Path>	files;
		
		/**
		 * Constructor #1.<br />
		 * @param files
		 *        the set to populate with the subtitle files.
		 */
		public SubtitleVisitor (final Set<Path> files) {
			super();
			this.files = files;
		}
		
		@Override
		public FileVisitResult visitFile (final Path file, final BasicFileAttributes attrs) throws IOException {
			// TODO allow custom extensions?
			if (!subtitleFormatManager.getFormatByPath(file).isEmpty()) {
				if (files.add(file)) {
					if (LG.isLoggable(Level.INFO)) {
						LG.info("File " + file + " added to subtitles to process.");
					}
				} else {
					LG.warning("Could not add file " + file + " to set because it is already present.");
				}
			}
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFileFailed (final Path file, final IOException exc) throws IOException {
			LG.warning("Could not open or read the file " + file + ": " + ExceptionUtils.display(exc));
			return FileVisitResult.CONTINUE;
		}
	}
}
