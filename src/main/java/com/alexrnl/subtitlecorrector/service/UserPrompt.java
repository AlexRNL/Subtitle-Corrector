package com.alexrnl.subtitlecorrector.service;

import java.util.Collection;

import com.alexrnl.commons.translation.Translator;
import com.alexrnl.commons.utils.Word;

/**
 * Interface for a user prompt class.<br />
 * This class will be used by the correction strategies to confirm string replacement.
 * @author Alex
 */
public interface UserPrompt extends SessionStateListener {
	
	/**
	 * Set the translator to use by the prompt.
	 * @param translator
	 *        the translator to use.
	 */
	void setTranslator (Translator translator);
	
	/**
	 * Display an information message to the user.
	 * @param translationKey
	 *        the translation key matching the message.
	 * @param parameters
	 *        the parameters of the translation message.
	 */
	void information (String translationKey, Object... parameters);
	
	/**
	 * Display a warning message to the user.
	 * @param translationKey
	 *        the translation key matching the message.
	 * @param parameters
	 *        the parameters of the translation message.
	 */
	void warning (String translationKey, Object... parameters);
	
	/**
	 * Display an error message to the user.
	 * @param translationKey
	 *        the translation key matching the message.
	 * @param parameters
	 *        the parameters of the translation message.
	 */
	void error (String translationKey, Object... parameters);
	
	/**
	 * Ask the user to choose between several options.
	 * @param choices the available choices.
	 * @param translationKey the translation key of the message to the user.
	 * @param parameters the parameters of the translation.
	 * @return the option chosen, or <code>null</code> if the user cancelled its choice.
	 */
	<T> T askChoice (Collection<T> choices, String translationKey, Object... parameters);
	
	/**
	 * Prompt the user to confirm a replacement of a {@link Word}.<br />
	 * Ask the user to confirm the replacement of the <code>original</code> {@link Word} by the
	 * <code>replacement</code> String.
	 * @param context
	 *        The context of the replacement, which will be shown to the user. <code>null</code> for
	 *        no context.
	 * @param original
	 *        the original {@link Word}, which will be replaced.
	 * @param replacement
	 *        the replacement which should be used in place of the original word.
	 * @return the answer of the user.
	 */
	UserPromptAnswer confirm (String context, Word original, String replacement);
	
	/**
	 * Prompt the user to confirm a replacement of a {@link Word}.<br />
	 * Ask the user to confirm the replacement of the <code>original</code> {@link Word} by the
	 * <code>replacement</code> String.
	 * @param original
	 *        the original {@link Word}, which will be replaced.
	 * @param replacement
	 *        the replacement which should be used in place of the original word.
	 * @return the answer of the user.
	 * @see #confirm(String, Word, String)
	 */
	UserPromptAnswer confirm (Word original, String replacement);
}
