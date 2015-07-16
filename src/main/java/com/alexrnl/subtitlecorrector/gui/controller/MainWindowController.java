package com.alexrnl.subtitlecorrector.gui.controller;

import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.commons.gui.swing.SwingUtils;
import com.alexrnl.commons.io.IOUtils;
import com.alexrnl.commons.mvc.AbstractController;
import com.alexrnl.commons.translation.StandardDialog;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.common.SubtitleFile;
import com.alexrnl.subtitlecorrector.common.TranslationKeys;
import com.alexrnl.subtitlecorrector.gui.model.MainWindowModel;
import com.alexrnl.subtitlecorrector.io.SubtitleFormat;
import com.alexrnl.subtitlecorrector.service.ServiceProvider;
import com.alexrnl.subtitlecorrector.service.SessionParameters;

/**
 * The controller of the main window.<br />
 * @author Alex
 */
public class MainWindowController extends AbstractController {
	/** Logger */
	private static final Logger		LG = Logger.getLogger(MainWindowController.class.getName());
	
	// TODO move these constants to model?
	/** The name for the subtitle property */
	public static final String		SUBTITLE_PROPERTY			= "Subtitle";
	/** The name for the strategy property */
	public static final String		STRATEGY_PROPERTY			= "Strategy";
	/** The name for the locale property */
	public static final String		LOCALE_PROPERTY				= "Locale";
	/** The name for the overwrite property */
	public static final String		OVERWRITE_PROPERTY			= "Overwrite";
	/** The name for the strategy parameters property */
	public static final String		STRATEGY_PARAMETER_PROPERTY	= "StrategyParameter";
	
	/** The service provider */
	private final ServiceProvider	serviceProvider;
	/** The model for the main window */
	private final MainWindowModel	model;
	
	/**
	 * Constructor #1.<br />
	 * @param serviceProvider
	 *        the provider for subtitle corrector services.
	 * @param model
	 *        the model for the window.
	 */
	public MainWindowController (final ServiceProvider serviceProvider, final MainWindowModel model) {
		super();
		this.serviceProvider = serviceProvider;
		this.model = model;
	}
	
	/**
	 * The names of the available strategies.
	 * @return the set with the strategies names.
	 */
	public Set<String> getStrategiesNames () {
		return Collections.unmodifiableSet(serviceProvider.getStrategies().keySet());
	}
	
	/**
	 * The available locales for dictionary based verifications.
	 * @return the set with the locales.
	 */
	public Set<Locale> getAvailableLocales () {;
		return Collections.unmodifiableSet(serviceProvider.getDictionariesManager().getLocaleDictionaries().keySet());
	}
	
	/**
	 * Change the value of the subtitle.
	 * @param newSubtitle
	 *        the new subtitle.
	 */
	public void changeSubtitle (final Path newSubtitle) {
		setModelProperty(SUBTITLE_PROPERTY, newSubtitle);
	}
	
	/**
	 * Change the value of the strategy.
	 * @param strategy
	 *        the new strategy.
	 */
	public void changeStrategy (final String strategy) {
		// TODO null check
		setModelProperty(STRATEGY_PROPERTY, serviceProvider.getStrategies().get(strategy));
	}
	
	/**
	 * Change the value of the overwrite property.
	 * @param overwrite
	 *        the new overwrite property.
	 */
	public void changeOverwrite (final boolean overwrite) {
		setModelProperty(OVERWRITE_PROPERTY, overwrite);
	}
	
	/**
	 * Change the value of the locale property.
	 * @param locale
	 *        the new locale property.
	 */
	public void changeLocale (final Locale locale) {
		setModelProperty(LOCALE_PROPERTY, locale);
	}

	/**
	 * Change the value of the parameter specified.
	 * @param key
	 *        the label of the parameter.
	 * @param value
	 *        the value of the parameter.
	 */
	public void changeStrategyParameterValue (final String key, final String value) {
		model.setStrategyParameter(key, value);
	}
	
	private enum Result {
		NO_SUBTITLES,
		FINISHED,
		ERROR;
	}
	
	/**
	 * Start the correcting session.
	 */
	public void startCorrection () {
		final SwingWorker<Result, Void> worker = new SwingWorker<Result, Void>() {
			@Override
			protected Result doInBackground () throws Exception {
				// TODO Auto-generated method stub
				final Map<SubtitleFile, SubtitleFormat> subtitles = serviceProvider.getSubtitleProvider().loadSubtitles(model.getSubtitle());
				if (subtitles.isEmpty()) {
					return Result.NO_SUBTITLES;
				}
				
				// TODO parameterize strategy
				
				// TODO set custom dictionaries and locale
				final SessionParameters parameters = new SessionParameters();
				parameters.setLocale(model.getLocale());
				
				// Actually correct subtitles
				serviceProvider.getSessionManager().addSessionListener(model.getStrategy());
				serviceProvider.getSessionManager().startSession(parameters);
				for (final SubtitleFile subtitleFile : subtitles.keySet()) {
					for (final Subtitle subtitle : subtitleFile) {
						model.getStrategy().correct(subtitle);
					}
				}
				serviceProvider.getSessionManager().stopSession();
				serviceProvider.getSessionManager().removeSessionListener(model.getStrategy());
				
				// Save subtitles
				for (final Entry<SubtitleFile, SubtitleFormat> entry : subtitles.entrySet()) {
					try {
						Path target = entry.getKey().getFile();
						if (!model.isOverwrite()) {
							target = target.getParent().resolve(IOUtils.getFilename(target)
											+ IOUtils.FILE_EXTENSION_SEPARATOR + serviceProvider.getTranslator().get(KEYS.misc().fileExtension())
											+ IOUtils.FILE_EXTENSION_SEPARATOR + IOUtils.getFileExtension(target));
						}
						entry.getValue().getWriter().writeFile(entry.getKey(), target);
					} catch (final IOException e) {
						SwingUtils.showMessageDialog(null, serviceProvider.getTranslator(),
								new StandardDialog(TranslationKeys.KEYS.console().app().subtitleWriteError(), entry.getKey().getFile(), e.getMessage()),
								JOptionPane.ERROR_MESSAGE, 50);
						LG.warning("Exception while writing file " + entry.getKey().getFile() + ": " + ExceptionUtils.display(e));
					}
				}
				
				return Result.FINISHED;
			}
		};
		
		worker.execute();
	}
	
}
