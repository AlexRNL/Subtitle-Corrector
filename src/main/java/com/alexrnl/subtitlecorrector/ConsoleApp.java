package com.alexrnl.subtitlecorrector;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexrnl.commons.arguments.Arguments;
import com.alexrnl.commons.arguments.Param;
import com.alexrnl.commons.arguments.parsers.AbstractParser;
import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.common.SubtitleFile;
import com.alexrnl.subtitlecorrector.correctionstrategy.Parameter;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;
import com.alexrnl.subtitlecorrector.gui.view.ConsoleUserPrompt;
import com.alexrnl.subtitlecorrector.io.SubtitleFormat;
import com.alexrnl.subtitlecorrector.service.SessionParameters;

/**
 * Console application for the subtitle corrector.<br />
 * @author Alex
 */
public class ConsoleApp extends AbstractApp {
	/** Logger */
	private static Logger		lg				= Logger.getLogger(ConsoleApp.class.getName());
	
	/** The name of the program */
	private static final String	PROGRAM_NAME	= "subtitleCorrector";
	
	/** The print stream to use for interacting with the user */
	private final PrintStream	out;
	// Command line parameters
	/** The subtitle files to correct */
	@Param(names = { "-i" }, description = "the subtitles file to correct", required = true)
	private Path				workingFiles;
	/** The strategy to use to correct the subtitles */
	@Param(names = { "-s" }, description = "the strategy to use for correcting subtitles", required = true)
	private Strategy			strategy;
	/** The locale to use */
	@Param(names = { "-l" }, description = "the language of the subtitle")
	private Locale				locale;
	
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
		super(new ConsoleUserPrompt());
		out = System.out;
		locale = Locale.getDefault();
		
		// Parse arguments
		final Arguments arguments = new Arguments(PROGRAM_NAME, this, out);
		arguments.addParameterParser(new AbstractParser<Strategy>(Strategy.class) {
			@Override
			public Strategy getValue (final String parameter) throws IllegalArgumentException {
				final Strategy strategyParameter = getStrategies().get(parameter);
				
				if (strategyParameter == null) {
					throw new IllegalArgumentException("No strategy with name " + parameter
							+ ", available strategies are: " + getStrategies().keySet());
				}
				return strategyParameter;
			}
		});
		arguments.addParameterParser(new AbstractParser<Locale>(Locale.class) {
			@Override
			public Locale getValue (final String parameter) throws IllegalArgumentException {
				return Locale.forLanguageTag(parameter);
			}
		});
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
		
		// Gather files
		final Set<Path> files = new TreeSet<>();
		if (Files.isDirectory(workingFiles)) {
			try {
				Files.walkFileTree(workingFiles, new SubtitleVisitor(files));
			} catch (final IOException e) {
				out.println("Could not retrieve subtitles in folder " + workingFiles);
				lg.warning("Could not retrieve subtitles to process: " + ExceptionUtils.display(e));
				return false;
			}
		} else if (Files.exists(workingFiles)) {
			files.add(workingFiles);
		} else {
			out.println("Path " + workingFiles + " is neither file nor directory");
			lg.severe(workingFiles + " is not a directory or a file");
			return false;
		}
		
		// Read files
		final Map<SubtitleFile, SubtitleFormat> subtitles = new HashMap<>(files.size(), 1.0f);
		for (final Path file : files) {
			final Set<SubtitleFormat> readers = getSubtitleFormatManager().getFormatByPath(file);
			final SubtitleFormat format;
			
			if (readers.isEmpty()) {
				continue;
			}
			if (readers.size() == 1) {
				format = readers.iterator().next();
			} else {
				// TODO select file format
				throw new UnsupportedOperationException("Several format sharing an extension is not supported");
			}
			
			try {
				subtitles.put(format.getReader().readFile(file), format);
			} catch (final IOException e) {
				out.println("Subtitle " + file + " could not be properly read, it will not be corrected.");
				lg.warning("Exception while parsing file " + file + ": " + ExceptionUtils.display(e));
			}
		}
		
		// Prepare strategy TODO
		for (final Parameter<?> parameter : strategy.getParameters()) {
			switch (parameter.getType()) {
				case BOOLEAN:
					break;
				case FREE:
					break;
				case LIST:
					break;
				default:
					break;
			}
		}
		
		final SessionParameters parameters = new SessionParameters();
		// TODO set parameters
		
		// Actually correct subtitles
		getSessionManager().startSession(parameters);
		for (final SubtitleFile subtitleFile : subtitles.keySet()) {
			for (final Subtitle subtitle : subtitleFile) {
				strategy.correct(subtitle);
			}
		}
		getSessionManager().stopSession();
		
		// Save subtitles
		for (final Entry<SubtitleFile, SubtitleFormat> entry : subtitles.entrySet()) {
			try {
				entry.getValue().getWriter().writeFile(entry.getKey(), entry.getKey().getFile());
			} catch (final IOException e) {
				out.println("Subtitle " + entry.getKey().getFile() + " could not be properly write, issues may occur.");
				// TODO restore a copy of the original file
				lg.warning("Exception while writing file " + entry.getKey().getFile() + ": " + ExceptionUtils.display(e));
			}
		}
		
		return true;
	}
	
	/**
	 * Visitor which add the subtitle file whose format is known.
	 * TODO externalize for re-usability
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
