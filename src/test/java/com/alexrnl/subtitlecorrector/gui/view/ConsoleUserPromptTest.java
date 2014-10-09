package com.alexrnl.subtitlecorrector.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.alexrnl.commons.io.EditableInputStream;
import com.alexrnl.commons.translation.Translator;
import com.alexrnl.commons.utils.Word;
import com.alexrnl.subtitlecorrector.common.TranslationKeys;
import com.alexrnl.subtitlecorrector.service.SessionParameters;
import com.alexrnl.subtitlecorrector.service.UserPromptAnswer;

/**
 * Test suite for the {@link ConsoleUserPrompt} class.
 * @author Alex
 */
public class ConsoleUserPromptTest {
	/** The translator to use */
	private Translator			translator;
	/** The input used */
	private EditableInputStream	input;
	/** The mocked output */
	@Mock
	private PrintStream			output;
	/** The console user prompt to test */
	private ConsoleUserPrompt	consolePrompt;
	
	/**
	 * Set up test attributes.
	 * @throws URISyntaxException
	 *         if there is a problem with the translation file path.
	 */
	@SuppressWarnings("unused")
	@Before
	public void setUp () throws URISyntaxException {
		initMocks(this);
		translator = new Translator(Paths.get(ConsoleUserPrompt.class.getResource("/locale/en.xml").toURI()));
		input = new EditableInputStream("");
		consolePrompt = new ConsoleUserPrompt(input, output);
		consolePrompt.setTranslator(translator);
		
		// Check that the default constructor is not throwing anything
		new ConsoleUserPrompt();
	}
	
	/**
	 * Test that a session state cannot be started twice in a row.
	 */
	@Test(expected = IllegalStateException.class)
	public void testStartSessionWithoutTranslator () {
		consolePrompt.setTranslator(null);
		consolePrompt.startSession(null);
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
	 * Test method for {@link ConsoleUserPrompt#information(String, Object...)}.
	 */
	@Test
	public void testInformation () {
		consolePrompt.information(TranslationKeys.KEYS.subtitleProvider().noAccess(), "myFile");
		verify(output).println("Cannot access files at myFile");
	}
	
	/**
	 * Test method for {@link ConsoleUserPrompt#warning(String, Object...)}.
	 */
	@Test
	public void testWarning () {
		consolePrompt.warning(TranslationKeys.KEYS.subtitleProvider().noAccess(), "myFile");
		verify(output).println("Cannot access files at myFile");
	}
	
	/**
	 * Test method for {@link ConsoleUserPrompt#error(String, Object...)}.
	 */
	@Test
	public void testError () {
		consolePrompt.error(TranslationKeys.KEYS.subtitleProvider().noAccess(), "myFile");
		verify(output).println("Cannot access files at myFile");
	}
	
	/**
	 * Test that a <code>null</code> list is not allowed.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAskChoiceNullList () {
		consolePrompt.askChoice(null, "");
	}
	
	/**
	 * Test that an empty list is not allowed.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAskChoiceEmptyList () {
		consolePrompt.askChoice(Collections.emptyList(), "");
	}
	
	/**
	 * Test method for {@link ConsoleUserPrompt#askChoice(java.util.Collection, String, Object...)}.
	 * @throws IOException
	 *         if there is an IO issue.
	 */
	@Test
	public void testAskChoice () throws IOException {
		input.updateStream("1\n");
		assertEquals("Aba", consolePrompt.askChoice(Arrays.asList("Aba", "Hjo", "Ldr"), "unknown"));
		verify(output).print("unknown\n\t1\tAba\n\t2\tHjo\n\t3\tLdr\n\t> ");
		
		// Check choice 0 => cancelled input
		input.updateStream("0\n");
		assertNull(consolePrompt.askChoice(Arrays.asList("Aba", "Hjo", "Ldr"), "unknown"));
	}
	
	/**
	 * Test method for {@link ConsoleUserPrompt#askChoice(java.util.Collection, String, Object...)}.
	 * Out of range integer.
	 * @throws IOException
	 *         if there is an IO issue.
	 */
	@Test
	public void testAskChoiceOutOfRangeInteger () throws IOException {
		input.updateStream("8\n2\n");
		assertEquals("Hjo", consolePrompt.askChoice(Arrays.asList("Aba", "Hjo", "Ldr"), "unknown"));
		verify(output).println("Value '8' could not be parsed as a valid integer between 0 and 3.");
		
		input.updateStream("-1\n2\n");
		assertEquals("Hjo", consolePrompt.askChoice(Arrays.asList("Aba", "Hjo", "Ldr"), "unknown"));
		verify(output).println("Value '-1' could not be parsed as a valid integer between 0 and 3.");
	}
	
	/**
	 * Test method for {@link ConsoleUserPrompt#askChoice(java.util.Collection, String, Object...)}.<br />
	 * Invalid integer case.
	 * @throws IOException
	 *         if there is an IO issue.
	 */
	@Test
	public void testAskChoiceInvalidInteger () throws IOException {
		input.updateStream("aba\n2\n");
		assertEquals("Hjo", consolePrompt.askChoice(Arrays.asList("Aba", "Hjo", "Ldr"), "unknown"));
		verify(output).println("Value 'aba' could not be parsed as a valid integer between 0 and 3.");
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
