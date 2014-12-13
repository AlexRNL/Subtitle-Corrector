package com.alexrnl.subtitlecorrector.correctionstrategy;

import static com.alexrnl.commons.utils.StringUtils.SPACE;
import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.commons.io.IOUtils;
import com.alexrnl.commons.utils.StringUtils;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.correctionstrategy.Parameter.Parser;

/**
 * Correction strategy which fix the punctuation in the subtitles.
 * @author Alex
 */
public class FixPunctuation extends AbstractStrategy {
	/** Logger */
	private static final Logger					LG				= Logger.getLogger(FixPunctuation.class.getName());
	
	/** Property name for the punctuation marks which have a space before. */
	private static final String					SPACE_BEFORE	= "spacebefore";
	/** Property name for the punctuation marks which have a space after. */
	private static final String					SPACE_AFTER		= "spaceafter";
	
	/** Map with the character which have a space after per locale */
	private final Map<Locale, List<Character>>	hasSpaceAfter;
	/** Map with the character which have a space before per locale */
	private final Map<Locale, List<Character>>	hasSpaceBefore;
	/** The locale parameter */
	private final Parameter<Locale>				locale;
	
	/**
	 * Constructor #1.<br />
	 * @param punctuationRuleFolder
	 *        the folder where the rules for the punctuation are stored.
	 * @throws IOException
	 *         if reading the rules fails.
	 */
	public FixPunctuation (final Path punctuationRuleFolder) throws IOException {
		super();
		Objects.requireNonNull(punctuationRuleFolder);
		if (!Files.isDirectory(punctuationRuleFolder)) {
			throw new IllegalArgumentException("The path must refer to a folder with the punctuation rules");
		}
		
		hasSpaceAfter = new HashMap<>();
		hasSpaceBefore = new HashMap<>();
		Files.walkFileTree(punctuationRuleFolder, new HashSet<FileVisitOption>(), 1, new PunctuationFileVisitor());
		locale = new Parameter<>(ParameterType.LIST, KEYS.strategy().fixPunctuation().locale(), new Parser<Locale>() {
			@Override
			public Locale parse (final String value) {
				return Locale.forLanguageTag(value);
			}
		}, hasSpaceAfter.keySet());
	}
	
	@Override
	public List<Parameter<?>> getParameters () {
		final List<Parameter<?>> parameters = new ArrayList<>();
		parameters.add(locale);
		return parameters;
	}
	
	@Override
	public String getTranslationKey () {
		return KEYS.strategy().fixPunctuation().toString();
	}
	
	@Override
	public String getDescription () {
		return KEYS.strategy().fixPunctuation().description();
	}
	
	@Override
	public void correct (final Subtitle subtitle) {
		final StringBuilder newContent = new StringBuilder();
		
		for (int indexChar = 0; indexChar < subtitle.getContent().length(); indexChar++) {
			final Character currentChar = subtitle.getContent().charAt(indexChar);
			if (indexChar - 1 > 0) {
				final Character charBefore = subtitle.getContent().charAt(indexChar - 1);
				if (hasSpaceBefore.get(locale.getValue()).contains(currentChar)) {
					// Check that there is a space before the punctuation mark.
					if (!StringUtils.isNewLine(charBefore) && !SPACE.equals(charBefore)) {
						newContent.append(SPACE);
					}
				} else {
					// Check that there is no space before the punctuation mark TODO add rule
				}
			} else {
				// Punctuation at the beginning of the subtitle, TODO
			}
			newContent.append(currentChar);
			if (indexChar + 1 < subtitle.getContent().length()) {
				// Check that there is a space after the punctuation mark
				final Character charAfter = subtitle.getContent().charAt(indexChar + 1);
				if (hasSpaceAfter.get(locale.getValue()).contains(currentChar)) {
					if (!StringUtils.isNewLine(charAfter) && !SPACE.equals(charAfter)) {
						newContent.append(SPACE);
					}
				}
			}
		}
		
		subtitle.setContent(newContent.toString().trim());
	}
	
	/**
	 * File visitor to load punctuation rules.
	 * @author Alex
	 */
	private class PunctuationFileVisitor extends SimpleFileVisitor<Path> {

		@Override
		public FileVisitResult visitFile (final Path file, final BasicFileAttributes attrs) throws IOException {
			final Locale key = Locale.forLanguageTag(IOUtils.getFilename(file));
			final Properties rules = new Properties();
			final InputStream stream = Files.newInputStream(file);
			rules.loadFromXML(stream);
			stream.close();
			if (LG.isLoggable(Level.INFO)) {
				LG.info("Loaded " + rules.size() + " punctuation rules for locale " + key);
			}
			hasSpaceAfter.put(key, StringUtils.toCharList(rules.getProperty(SPACE_AFTER)));
			hasSpaceBefore.put(key, StringUtils.toCharList(rules.getProperty(SPACE_BEFORE)));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed (final Path file, final IOException exc) throws IOException {
			LG.warning("Could not open or read the file " + file + ": " + ExceptionUtils.display(exc));
			return FileVisitResult.CONTINUE;
		}
	}
}
