package com.alexrnl.subtitlecorrector.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.alexrnl.commons.io.EditableInputStream;
import com.alexrnl.commons.utils.Word;
import com.alexrnl.subtitlecorrector.service.SessionParameters;
import com.alexrnl.subtitlecorrector.service.UserPromptAnswer;

/**
 * Test suite for the {@link ConsoleUserPrompt} class.
 * @author Alex
 */
public class ConsoleUserPromptTest {
	/** The input used */
	private EditableInputStream	input;
	/** The mocked output */
	@Mock
	private PrintStream			output;
	/** The console user prompt to test */
	private ConsoleUserPrompt	consolePrompt;
	
	/**
	 * Set up test attributes.
	 */
	@SuppressWarnings("unused")
	@Before
	public void setUp () {
		initMocks(this);
		input = new EditableInputStream("");
		consolePrompt = new ConsoleUserPrompt(input, output);
		
		// Check that the default constructor is not throwing anything
		new ConsoleUserPrompt();
	}
	
	/**
	 * Test that a session state cannot be started twice in a row.
	 */
	@Test(expected = IllegalStateException.class)
	public void testBadStartSession () {
		consolePrompt.startSession(new SessionParameters());
		consolePrompt.startSession(new SessionParameters());
	}
	
	/**
	 * Test that a session state cannot be stop if it was not started.
	 */
	@Test(expected = IllegalStateException.class)
	public void testBadStopSession () {
		consolePrompt.stopSession();
	}
	
	/**
	 * Test that a the confirmation of an answer cannot be requested if a session is not started.
	 */
	@Test(expected = IllegalStateException.class)
	public void testBadConfirm () {
		consolePrompt.confirm(new Word("", 0, 0), "");
	}
	
	/**
	 * Test method for {@link ConsoleUserPrompt#confirm(String, Word, String)}.
	 * @throws IOException
	 *         if there is an IO issue.
	 */
	@Test
	public void testConfirmStringWordString () throws IOException {
		consolePrompt.startSession(new SessionParameters());
		
		// Test with a confirmation of the replacement
		input.updateStream("y\nn\n");
		UserPromptAnswer answer = consolePrompt.confirm("hello, i am the new man in chxrge.", new Word("chxrge", 27, 33), "charge");
		assertEquals("charge", answer.getAnswer());
		assertFalse(answer.isCancelled());
		assertFalse(answer.isRememberChoice());
		
		// Test with a modification of the replacement
		input.updateStream("n\nCHARGE\nn\n");
		answer = consolePrompt.confirm("hello, i am the new man in chxrge.", new Word("chxrge", 27, 33), "charge");
		assertEquals("CHARGE", answer.getAnswer());
		assertFalse(answer.isCancelled());
		assertFalse(answer.isRememberChoice());
		
		// Test with a cancellation
		input.updateStream("n\n\nn\n");
		answer = consolePrompt.confirm("hello, i am the new man in chxrge.", new Word("chxrge", 27, 33), "charge");
		assertEquals("", answer.getAnswer());
		assertTrue(answer.isCancelled());
		assertFalse(answer.isRememberChoice());
		
		// Test with remember choice
		input.updateStream("n\ncharged\ny\n");
		answer = consolePrompt.confirm(new Word("chxrge", 27, 33), "charge");
		assertEquals("charged", answer.getAnswer());
		assertFalse(answer.isCancelled());
		assertTrue(answer.isRememberChoice());
		
		consolePrompt.stopSession();
	}
	
}
