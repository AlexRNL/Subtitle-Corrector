package com.alexrnl.subtitlecorrector;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexrnl.commons.arguments.Arguments;
import com.alexrnl.commons.arguments.Param;
import com.alexrnl.commons.arguments.parsers.ClassParser;
import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;

/**
 * Console application for the subtitle corrector.<br />
 * @author Alex
 */
public class ConsoleApp extends AbstractApp {
	/** Logger */
	private static Logger				lg				= Logger.getLogger(ConsoleApp.class.getName());
	
	/** The name of the program */
	private static final String			PROGRAM_NAME	= "subtitleCorrector";
	
	/** The print stream to use for interacting with the user */
	private final PrintStream			out;
	// Command line parameters
	/** The subtitle files to correct */
	@Param(names = { "-i" }, description = "the subtitles file to correct", required = true)
	private Path						workingFiles;
	@Param(names = { "-s" }, description = "the strategy to use for correcting subtitles", required = true)
	private Class<? extends Strategy>	strategy;
	
	/**
	 * Constructor #1.<br />
	 * @param args
	 *        the arguments from the command line.
	 * @throws IOException
	 *         if a resource cannot be loaded.
	 * @throws URISyntaxException
	 *         if there is an error while building a Path.
	 */
	public ConsoleApp (final String[] args) throws IOException, URISyntaxException {
		super();
		out = System.out;
		final Arguments arguments = new Arguments(PROGRAM_NAME, this, out);
		arguments.addParameterParser(new ClassParser(Strategy.class.getPackage()));
		arguments.parse(args);
	}
	
	@Override
	public boolean launch () {
		final boolean exists = Files.exists(workingFiles);
		final boolean reads = Files.isReadable(workingFiles);
		if (!exists || !reads) {
			out.println("Cannot access files at " + workingFiles);
			lg.severe("Path " + workingFiles + " does " + (exists ? "" : "not") + " exist and can"
					+ (reads ? "" : "not") + " be read");
			return false;
		}
		
		final Set<Path> files = new HashSet<>();
		if (Files.isDirectory(workingFiles)) {
			try {
				Files.walkFileTree(workingFiles, new SubtitleVisitor(files));
			} catch (final IOException e) {
				out.println("Could not retrieve subtitles in folder " + workingFiles);
				lg.warning("Could not retrieve subtitles to process: " + ExceptionUtils.display(e));
				return false;
			}
		} else if (Files.isRegularFile(workingFiles)) {
			files.add(workingFiles);
		} else {
			out.println("Path " + workingFiles + " is neither file nor directory");
			lg.severe(workingFiles + " is not a directory or a file");
			return false;
		}
		
		return true;
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
			if (!getSubtitleFormatManager().getFormatByPath(file).isEmpty()) {
				if (files.add(file)) {
					if (lg.isLoggable(Level.INFO)) {
						lg.info("File " + file + " added to subtitles to process.");
					}
				} else {
					lg.warning("Could not add file " + file + " to set because it is already present.");
				}
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed (final Path file, final IOException exc) throws IOException {
			lg.warning("Could not open or read the file " + file + ": " + ExceptionUtils.display(exc));
			return FileVisitResult.CONTINUE;
		}
		
	}
}
