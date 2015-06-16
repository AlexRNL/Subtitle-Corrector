package com.alexrnl.subtitlecorrector.gui.controller;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;

import com.alexrnl.commons.mvc.AbstractController;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.common.SubtitleFile;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;
import com.alexrnl.subtitlecorrector.gui.model.MainWindowModel;
import com.alexrnl.subtitlecorrector.io.SubtitleFormat;
import com.alexrnl.subtitlecorrector.service.ServiceProvider;
import com.alexrnl.subtitlecorrector.service.SessionParameters;

/**
 * The controller of the main window.<br />
 * @author Alex
 */
public class MainWindowController extends AbstractController {
	
	/** The name for the subtitle property */
	public static final String	SUBTITLE_PROPERTY	= "Subtitle";
	/** The name for the strategy property */
	public static final String	STRATEGY_PROPERTY	= "Strategy";
	
	/** The service provider */
	private final ServiceProvider serviceProvider;
	
	/**
	 * Constructor #1.<br />
	 * @param serviceProvider
	 *        the provider for subtitle corrector services.
	 */
	public MainWindowController (final ServiceProvider serviceProvider) {
		super();
		this.serviceProvider = serviceProvider;
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
	public void changeStrategy (final Strategy strategy) {
		setModelProperty(STRATEGY_PROPERTY, strategy);
	}
	
	/**
	 * Start the correcting session.
	 */
	public void startCorrection () {
		// TODO perform in worker?
		// TODO retrieve model differently?
		final MainWindowModel model = (MainWindowModel) getRegisteredModels()[0];
		final Map<SubtitleFile, SubtitleFormat> subtitles = serviceProvider.getSubtitleProvider().loadSubtitles(model.getSubtitle());
		if (subtitles.isEmpty()) {
			return;
		}
		
		// TODO parameterize strategy
		

		// TODO set custom dictionaries and locale
		final SessionParameters parameters = new SessionParameters();
		parameters.setLocale(Locale.ENGLISH);
		
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
	}
}
