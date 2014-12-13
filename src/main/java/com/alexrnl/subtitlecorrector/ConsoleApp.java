package com.alexrnl.subtitlecorrector;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Logger;

import com.alexrnl.commons.arguments.Arguments;
import com.alexrnl.commons.arguments.Param;
import com.alexrnl.commons.arguments.parsers.AbstractParser;
import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.commons.io.IOUtils;
import com.alexrnl.commons.utils.StringUtils;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.common.SubtitleFile;
import com.alexrnl.subtitlecorrector.common.TranslationKeys;
import com.alexrnl.subtitlecorrector.common.TranslationKeys.Console.App;
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
	private static final Logger	LG					= Logger.getLogger(ConsoleApp.class.getName());
	
	/** The prefix to indicate that the file was corrected (in non-overwrite mode) */
	private static final String	CORRECTED_EXTENSION	= "corrected";
	
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
	/** Flag to indicate to overwrite original subtitles file */
	@Param(names = { "-o" }, description = "overwrite subtitle file while correcting")
	private boolean				overwrite;
	
	
	/**
	 * Constructor #1.<br />
	 * @param args
	 *        the arguments from the command line.
	 * @throws IOException
	 *         if a resource cannot be loaded.
	 * @throws URISyntaxException
	 *         if there is an error while building a Path.
	 */
	public ConsoleApp (final List<String> args) throws IOException, URISyntaxException {
		super(new ConsoleUserPrompt());
		out = System.out;
		locale = Locale.getDefault();
		
		// Parse arguments
		final Arguments arguments = new Arguments(PROGRAM_NAME, this, out);
		arguments.addParameterParser(new AbstractParser<Strategy>(Strategy.class) {
			@Override
			public Strategy getValue (final String parameter) {
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
			public Locale getValue (final String parameter) {
				return Locale.forLanguageTag(parameter);
			}
		});
		arguments.parse(args);
	}
	
	@Override
	public boolean launch () {
		final App appKey = TranslationKeys.KEYS.console().app();
		
		final Map<SubtitleFile, SubtitleFormat> subtitles = getSubtitleProvider().loadSubtitles(workingFiles);
		if (subtitles.isEmpty()) {
			return false;
		}
		
		// Prepare strategy
		final Scanner input = new Scanner(System.in);
		final List<Parameter<?>> strategyParameters = strategy.getParameters();
		if (!strategyParameters.isEmpty()) {
			out.println(getTranslator().get(appKey.strategyParametersInput(), getTranslator().get(strategy.getTranslationKey())));
		}
		for (final Parameter<?> parameter : strategyParameters) {
			String prompt = "";
			switch (parameter.getType()) {
				// TODO translate this
				case BOOLEAN:
					prompt = " (y/n)";
					break;
				case FREE:
					break;
				case LIST:
					prompt = "(" + StringUtils.separateWith("|", parameter.getPossibleValues()) + ")";
					break;
			}
			boolean success = false;
			while (!success) {
				out.print("\t" + getTranslator().get(parameter.getDescription()) + prompt + " > ");
				try {
					parameter.setValue(input.nextLine());
					success = true;
				} catch (final IllegalArgumentException e) {
					out.print(getTranslator().get(appKey.strategyParametersInvalidValue(), e.getMessage()));
				}
				
			}
		}
		input.close();
		
		final SessionParameters parameters = new SessionParameters();
		parameters.setLocale(locale);
		// TODO set custom dictionaries
		
		// Actually correct subtitles
		getSessionManager().addSessionListener(strategy);
		getSessionManager().startSession(parameters);
		for (final SubtitleFile subtitleFile : subtitles.keySet()) {
			for (final Subtitle subtitle : subtitleFile) {
				strategy.correct(subtitle);
			}
		}
		getSessionManager().stopSession();
		getSessionManager().removeSessionListener(strategy);
		
		// Save subtitles
		for (final Entry<SubtitleFile, SubtitleFormat> entry : subtitles.entrySet()) {
			try {
				Path target = entry.getKey().getFile();
				if (!overwrite) {
					target = target.getParent().resolve(IOUtils.getFilename(target)
									+ IOUtils.FILE_EXTENSION_SEPARATOR + CORRECTED_EXTENSION
									+ IOUtils.FILE_EXTENSION_SEPARATOR + IOUtils.getFileExtension(target));
				}
				entry.getValue().getWriter().writeFile(entry.getKey(), target);
			} catch (final IOException e) {
				out.println(getTranslator().get(appKey.subtitleWriteError(), entry.getKey().getFile(), e.getMessage()));
				// TODO restore a copy of the original file
				LG.warning("Exception while writing file " + entry.getKey().getFile() + ": " + ExceptionUtils.display(e));
			}
		}
		
		return true;
	}
	
}
