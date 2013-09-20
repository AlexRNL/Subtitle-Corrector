package com.alexrnl.subtitlecorrector.service;

import com.alexrnl.commons.utils.Word;

/**
 * Interface for a user prompt class.<br />
 * This class will be used by the correction strategies to confirm string replacement.
 * @author Alex
 */
public interface UserPrompt {
	
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
