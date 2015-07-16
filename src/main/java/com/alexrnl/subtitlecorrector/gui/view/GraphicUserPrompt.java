package com.alexrnl.subtitlecorrector.gui.view;


import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.alexrnl.commons.gui.swing.SwingUtils;
import com.alexrnl.commons.translation.Dialog;
import com.alexrnl.commons.translation.Translatable;
import com.alexrnl.commons.translation.Translator;
import com.alexrnl.commons.utils.StringUtils;
import com.alexrnl.commons.utils.Word;
import com.alexrnl.subtitlecorrector.service.SessionParameters;
import com.alexrnl.subtitlecorrector.service.SessionStateAdapter;
import com.alexrnl.subtitlecorrector.service.UserPrompt;
import com.alexrnl.subtitlecorrector.service.UserPromptAnswer;

/**
 * A GUI implementation of the {@link UserPrompt} interface.
 * @author Alex
 */
public class GraphicUserPrompt extends SessionStateAdapter implements UserPrompt {
	/** Logger */
	private static final Logger	LG	= Logger.getLogger(GraphicUserPrompt.class.getName());
	
	/** The maximum length for lines in dialogs */
	private static final int	MAXIMUM_LINE_LENGTH = 80;
	
	/** The translator to use */
	private Translator			translator;
	
	/**
	 * Constructor #1.<br />
	 */
	public GraphicUserPrompt () {
		super();
	}
	
	private final class UserPromptDialog implements Dialog {
		/** The title of the prompt */
		private final String				title;
		/** The message of the prompt */
		private final String				message;
		/** The parameters of the prompt */
		private final Collection<Object>	parameters;

		/**
		 * Constructor #1.<br />
		 * @param title
		 *        the title of the prompt.
		 * @param message
		 *        the message of the prompt.
		 * @param parameters
		 *        the parameters of the prompt.
		 */
		public UserPromptDialog (final String title, final String message, final Collection<Object> parameters) {
			super();
			this.title = title;
			this.message = message;
			this.parameters = parameters;
		}
		
		/**
		 * Constructor #2.<br />
		 * @param title
		 *        the title of the prompt.
		 * @param message
		 *        the message of the prompt.
		 * @param parameters
		 *        the parameters of the prompt.
		 */
		public UserPromptDialog (final String title, final String message, final Object... parameters) {
			this(title, message, Arrays.asList(parameters));
		}
		
		/**
		 * Constructor #3.<br />
		 * @param title
		 *        the title of the prompt.
		 * @param message
		 *        the message of the prompt.
		 */
		public UserPromptDialog (final String title, final String message) {
			this(title, message, Collections.emptyList());
		}

		@Override
		public String title () {
			return title;
		}
		
		@Override
		public String message () {
			return message;
		}
		
		@Override
		public Collection<Object> getParameters () {
			return parameters;
		}
		
		@Override
		public String getTranslationKey () {
			return message();
		}
		
	}
	
	@Override
	public void setTranslator (final Translator translator) {
		this.translator = translator;
	}
	
	@Override
	public void startSession (final SessionParameters parameters) {
		if (translator == null) {
			throw new IllegalStateException("Cannot start session without translator");
		}
	}
	
	/**
	 * Show a dialog of the specified type.
	 * @param dialog
	 *        the dialog to show.
	 * @param type
	 *        the type of the dialog.
	 */
	private void showDialog (final Dialog dialog, final int type) {
		SwingUtils.showMessageDialog(null, translator, dialog, type, MAXIMUM_LINE_LENGTH);
	}
	
	@Override
	public void information (final String translationKey, final Object... parameters) {
		showDialog(new UserPromptDialog("title", translationKey, parameters), JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public void warning (final String translationKey, final Object... parameters) {
		showDialog(new UserPromptDialog("title", translationKey, parameters), JOptionPane.WARNING_MESSAGE);
		
	}
	
	@Override
	public void error (final String translationKey, final Object... parameters) {
		showDialog(new UserPromptDialog("title", translationKey, parameters), JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public <T extends Translatable> T askChoice (final Collection<T> choices, final String translationKey,
			final Object... parameters) {
		return SwingUtils.askChoice(null, translator, new UserPromptDialog("title", translationKey, parameters), choices, MAXIMUM_LINE_LENGTH);
	}
	
	@Override
	public UserPromptAnswer confirm (final String context, final Word original, final String replacement) {
		final com.alexrnl.subtitlecorrector.common.TranslationKeys.Gui.UserPrompt userPromptKey = KEYS.gui().userPrompt();
		final Dialog dialog = StringUtils.neitherNullNorEmpty(context) ?
				new UserPromptDialog("title", userPromptKey.replaceWithContext(), original, replacement, context) :
				new UserPromptDialog("title", userPromptKey.replace(), original, replacement);
		final String answer = SwingUtils.askFreeInput(null, translator, dialog, replacement, MAXIMUM_LINE_LENGTH);
		
		return new UserPromptAnswer(answer, answer == null,
				SwingUtils.askConfirmation(null, translator, new UserPromptDialog("title", userPromptKey.rememberChoice()), MAXIMUM_LINE_LENGTH));
	}
	
	@Override
	public UserPromptAnswer confirm (final Word original, final String replacement) {
		return confirm(null, original, replacement);
	}
}
