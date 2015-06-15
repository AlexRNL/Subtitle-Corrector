package com.alexrnl.subtitlecorrector.gui.view;


import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;

import java.util.Collection;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.alexrnl.commons.gui.swing.SwingUtils;
import com.alexrnl.commons.translation.Dialog;
import com.alexrnl.commons.translation.StandardDialog;
import com.alexrnl.commons.translation.Translatable;
import com.alexrnl.commons.translation.Translator;
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
	
	/** The translator to use */
	private Translator			translator;
	
	/**
	 * Constructor #1.<br />
	 */
	public GraphicUserPrompt () {
		super();
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
		SwingUtils.showMessageDialog(null, translator, dialog, type, 50);
	}
	
	@Override
	public void information (final String translationKey, final Object... parameters) {
		showDialog(new StandardDialog(translationKey, parameters), JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public void warning (final String translationKey, final Object... parameters) {
		showDialog(new StandardDialog(translationKey, parameters), JOptionPane.WARNING_MESSAGE);
		
	}
	
	@Override
	public void error (final String translationKey, final Object... parameters) {
		showDialog(new StandardDialog(translationKey, parameters), JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public <T extends Translatable> T askChoice (final Collection<T> choices, final String translationKey,
			final Object... parameters) {
		return SwingUtils.askChoice(null, translator, new StandardDialog(translationKey, parameters), choices, 50);
	}
	
	@Override
	public UserPromptAnswer confirm (final String context, final Word original, final String replacement) {
		final com.alexrnl.subtitlecorrector.common.TranslationKeys.Gui.UserPrompt userPromptKey = KEYS.gui().userPrompt();
		final StandardDialog dialog = context != null && !context.isEmpty() ?
				new StandardDialog(userPromptKey.replaceWithContext(), original, replacement) :
				new StandardDialog(userPromptKey.replace(), original, replacement, context);
		final String answer = SwingUtils.askFreeInput(null, translator, dialog, replacement, 50);
		
		return new UserPromptAnswer(answer, answer == null,
				SwingUtils.askConfirmation(null, translator, new StandardDialog(userPromptKey.rememberChoice()), 50));
	}
	
	@Override
	public UserPromptAnswer confirm (final Word original, final String replacement) {
		return confirm(null, original, replacement);
	}
}
