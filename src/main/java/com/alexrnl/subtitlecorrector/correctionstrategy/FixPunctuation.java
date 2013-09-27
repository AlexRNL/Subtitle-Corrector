package com.alexrnl.subtitlecorrector.correctionstrategy;

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
import com.alexrnl.commons.utils.CollectionUtils;
import com.alexrnl.subtitlecorrector.common.Subtitle;

/**
 * TODO
 * @author Alex
 */
public class FixPunctuation implements Strategy {
	/** Logger */
	private static Logger							lg	= Logger.getLogger(FixPunctuation.class.getName());
	
	/** Map with the punctuation rule per locale */
	private final Map<Locale, Map<String, String>>	punctuationRules;
	/** The locale parameter */
	private final Parameter<Locale>					locale;
	
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
		
		locale = new Parameter<>(ParameterType.LIST, KEYS.strategy().fixPunctuation().locale());
		punctuationRules = new HashMap<>();
		Files.walkFileTree(punctuationRuleFolder, new HashSet<FileVisitOption>(), 1, new PunctuationFileVisitor());
	}
	
	@Override
	public List<Parameter<?>> getParameters () {
		final List<Parameter<?>> parameters = new ArrayList<>();
		parameters.add(locale);
		return parameters;
	}
	
	@Override
	public Parameter<?> getParameterByName (final String name) {
		Objects.requireNonNull(name);
		// TODO factorise between strategies
		if (name.equals(locale.getDescription())) {
			return locale;
		}
		return null;
	}
	
	@Override
	public void startSession () {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void stopSession () {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void correct (final Subtitle subtitle) {
		final StringBuilder newContent = new StringBuilder();
		
		for (int indexChar = 0; indexChar < subtitle.getContent().length(); indexChar++) {
			final Character currentChar = subtitle.getContent().charAt(indexChar);
			if (punctuationRules.get(locale.getValue()).containsKey(currentChar.toString())) {
				final String rule = punctuationRules.get(locale.getValue()).get(currentChar.toString());
				final int indexPunctuation = rule.indexOf(indexChar);
				
			} else {
				newContent.append(currentChar);
			}
		}
		
		subtitle.setContent(newContent.toString());
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
			rules.load(stream);
			stream.close();
			if (lg.isLoggable(Level.INFO)) {
				lg.info("Loaded " + rules.size() + " punctuation rules for locale " + key);
			}
			punctuationRules.put(key, CollectionUtils.convertPropertiesToMap(rules));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed (final Path file, final IOException exc) throws IOException {
			lg.warning("Could not open or read the file " + file + ": " + ExceptionUtils.display(exc));
			return FileVisitResult.CONTINUE;
		}
	}
}
