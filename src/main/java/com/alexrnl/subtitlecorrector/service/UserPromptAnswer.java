package com.alexrnl.subtitlecorrector.service;


/**
 * Class representing a answer (from the user) to the prompt.<br />
 * This class is immutable.
 * @author Alex
 */
public class UserPromptAnswer {
	
	/** The answer of the user to the question */
	private final String	answer;
	/** <code>true</code> if the user cancelled the prompt */
	private final boolean	cancelled;
	/** <code>true</code> if the user's choice should be remembered */
	private final boolean	rememberChoice;
	
	/**
	 * Constructor #1.<br />
	 * @param answer
	 *        the answer of the user.
	 * @param cancelled
	 *        <code>true</code> if the user cancelled the prompt.
	 * @param rememberChoice
	 *        <code>true</code> if the user's choice should be remembered.
	 */
	public UserPromptAnswer (final String answer, final boolean cancelled, final boolean rememberChoice) {
		super();
		this.answer = answer;
		this.cancelled = cancelled;
		this.rememberChoice = rememberChoice;
	}
	
	/**
	 * Constructor #2.<br />
	 * This constructor set the {@link #cancelled} flag to <code>false</code>.
	 * @param answer
	 *        the answer of the user.
	 * @param rememberChoice
	 *        <code>true</code> if the user's choice should be remembered.
	 */
	public UserPromptAnswer (final String answer, final boolean rememberChoice) {
		this(answer, false, rememberChoice);
	}
	
	/**
	 * Constructor #3.<br />
	 * This constructor set the {@link #cancelled} flag to <code>false</code> and the
	 * {@link #rememberChoice} to <code>true</code>.
	 * @param answer
	 *        the answer of the user.
	 */
	public UserPromptAnswer (final String answer) {
		this(answer, false);
	}
	
	/**
	 * Return the attribute answer.
	 * @return the attribute answer.
	 */
	public String getAnswer () {
		return answer;
	}
	
	/**
	 * Return the attribute cancelled.
	 * @return the attribute cancelled.
	 */
	public boolean isCancelled () {
		return cancelled;
	}
	
	/**
	 * Return the attribute rememberChoice.
	 * @return the attribute rememberChoice.
	 */
	public boolean isRememberChoice () {
		return rememberChoice;
	}
}
