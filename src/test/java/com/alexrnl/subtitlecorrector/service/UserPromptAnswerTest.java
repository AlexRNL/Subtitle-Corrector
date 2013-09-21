package com.alexrnl.subtitlecorrector.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the {@link UserPromptAnswer} class.
 * @author Alex
 */
public class UserPromptAnswerTest {
	/** An object with only the answer */
	private UserPromptAnswer	onlyAnswer;
	/** An object with an answer to remember */
	private UserPromptAnswer	rememberAnswer;
	/** An answer which was cancelled */
	private UserPromptAnswer	cancelledQuestion;

	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		onlyAnswer = new UserPromptAnswer("ldr");
		rememberAnswer = new UserPromptAnswer("aba & ldr", true);
		cancelledQuestion = new UserPromptAnswer("LDR", true, false);
	}
	
	/**
	 * Test method for {@link UserPromptAnswer#getAnswer()}.
	 */
	@Test
	public void testGetAnswer () {
		assertEquals("ldr", onlyAnswer.getAnswer());
		assertEquals("aba & ldr", rememberAnswer.getAnswer());
		assertEquals("LDR", cancelledQuestion.getAnswer());
	}
	
	/**
	 * Test method for {@link UserPromptAnswer#isCancelled()}.
	 */
	@Test
	public void testIsCancelled () {
		assertFalse(onlyAnswer.isCancelled());
		assertFalse(rememberAnswer.isCancelled());
		assertTrue(cancelledQuestion.isCancelled());
	}
	
	/**
	 * Test method for {@link UserPromptAnswer#isRememberChoice()}.
	 */
	@Test
	public void testIsRememberChoice () {
		assertFalse(onlyAnswer.isRememberChoice());
		assertTrue(rememberAnswer.isRememberChoice());
		assertFalse(cancelledQuestion.isRememberChoice());
	}
}
