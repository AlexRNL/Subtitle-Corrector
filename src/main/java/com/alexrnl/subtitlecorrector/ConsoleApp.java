package com.alexrnl.subtitlecorrector;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.alexrnl.commons.arguments.Arguments;
import com.alexrnl.commons.arguments.Param;
import com.alexrnl.commons.arguments.parsers.ClassParser;
import com.alexrnl.subtitlecorrector.common.SubtitleFile;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;

/**
 * Console application for the subtitle corrector.<br />
 * @author Alex
 */
public class ConsoleApp {
	/** Logger */
	private static Logger				lg				= Logger.getLogger(ConsoleApp.class.getName());
	
	/** The name of the program */
	private static final String			PROGRAM_NAME	= "subtitleCorrector";
	
	/** The print stream to use for interacting with the user */
	private final PrintStream out;
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
	 */
	public ConsoleApp (final String[] args) {
		super();
		out = System.out;
		final Arguments arguments = new Arguments(PROGRAM_NAME, this, out);
		arguments.addParameterParser(new ClassParser(Strategy.class.getPackage()));
		arguments.parse(args);
	}
	
	/**
	 * Launch the subtitle correction.
	 * @return <code>true</code> if the console application has been processing subtitles
	 *         successfully.
	 */
	public boolean launch () {
		final boolean exists = Files.exists(workingFiles);
		final boolean reads = Files.isReadable(workingFiles);
		if (!exists || !reads) {
			out.println("Cannot access files at " + workingFiles);
			lg.severe("Path " + workingFiles + " does " + (exists ? "" : "not") + " exist and can"
					+ (reads ? "" : "not") + " be read");
			return false;
		}
		
		final Set<SubtitleFile> files = new HashSet<>();
		if (Files.isDirectory(workingFiles)) {
			
		} else if (Files.isRegularFile(workingFiles)) {
			
		} else {
			out.println("Path " + workingFiles + " is neither file nor directory");
			lg.severe(workingFiles + " is not a directory or a file");
			return false;
		}
		
		return true;
	}
}
