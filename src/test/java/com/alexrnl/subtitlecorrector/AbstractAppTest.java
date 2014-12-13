package com.alexrnl.subtitlecorrector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;
import com.alexrnl.subtitlecorrector.io.SubtitleFormatManager;
import com.alexrnl.subtitlecorrector.io.subrip.SubRip;
import com.alexrnl.subtitlecorrector.service.DictionaryManager;
import com.alexrnl.subtitlecorrector.service.UserPrompt;

/**
 * Test suite for the {@link AbstractApp} class.
 * @author Alex
 */
public class AbstractAppTest {
	/** The abstract application to test */
	private AbstractApp app;
	/** The (mocked) user prompt */
	@Mock
	private UserPrompt userPrompt;
	
	/**
	 * Set up test attributes.
	 * @throws URISyntaxException
	 *         if there is an issue when building the abstract app.
	 * @throws IOException
	 *         if there is an I/O issue.
	 */
	@Before
	public void setUp () throws IOException, URISyntaxException {
		initMocks(this);
		app = spy(new AbstractApp(userPrompt) {
			@Override
			public boolean launch () {
				return true;
			}
		});
	}
	
	/**
	 * Test method for {@link AbstractApp#getTranslator()}.
	 */
	@Test
	public void testGetTranslator () {
		assertNotNull(app.getTranslator());
	}
	
	/**
	 * Test method for {@link AbstractApp#getSessionManager()}.
	 */
	@Test
	public void testGetSessionManager () {
		assertNotNull(app.getSessionManager());
	}
	
	/**
	 * Test method for {@link AbstractApp#getDictionariesManager()}.
	 */
	@Test
	public void testGetDictionariesManager () {
		final DictionaryManager dictionaryManager = app.getDictionariesManager();
		assertNotNull(dictionaryManager);
		assertEquals(2, dictionaryManager.getLocaleDictionaries().size());
		assertEquals(new HashSet<>(Arrays.asList(Locale.FRENCH, Locale.ENGLISH)), dictionaryManager.getLocaleDictionaries().keySet());
	}
	
	/**
	 * Test method for {@link AbstractApp#getStrategies()}.
	 */
	@Test
	public void testGetStrategies () {
		final Map<String, Strategy> strategies = app.getStrategies();
		assertNotNull(strategies);
		assertEquals(2, strategies.size());
	}
	
	/**
	 * Test method for {@link AbstractApp#getStrategies()}.
	 * Check that the strategies cannot be modified.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testGetStrategiesNotModifiable () {
		app.getStrategies().clear();
	}
	
	/**
	 * Test method for {@link AbstractApp#addStrategy(Strategy)}.
	 */
	@Test
	public void testAddStrategy () {
		final HashMap<String, Strategy> previousStrategies = new HashMap<>(app.getStrategies());
		final Strategy mockedStrategy = mock(Strategy.class);
		when(mockedStrategy.getTranslationKey()).thenReturn("mockedStrategy");
		assertFalse(app.addStrategy(mockedStrategy));
		app.getSessionManager().addSessionListener(mockedStrategy);
		final HashMap<String, Strategy> newStrategies = new HashMap<>(app.getStrategies());
		newStrategies.keySet().removeAll(previousStrategies.keySet());
		assertEquals(new HashSet<>(Arrays.asList(mockedStrategy.getTranslationKey())), newStrategies.keySet());
		
		final Strategy duplicateNameStrategy = mock(Strategy.class);
		when(duplicateNameStrategy.getTranslationKey()).thenReturn("mockedStrategy");
		assertTrue(app.addStrategy(duplicateNameStrategy));
		
		assertFalse(app.getSessionManager().removeSessionListener(mockedStrategy));
	}
	
	/**
	 * Test method for {@link AbstractApp#getSubtitleFormatManager()}.
	 */
	@Test
	public void testGetSubtitleFormatManager () {
		final SubtitleFormatManager subtitleFormatManager = app.getSubtitleFormatManager();
		assertNotNull(subtitleFormatManager);
		assertEquals(1, subtitleFormatManager.getAvailableFormats().size());
		assertNotNull(subtitleFormatManager.getFormatByName(SubRip.SUBRIP_NAME));
	}
	
	/**
	 * Test method for {@link AbstractApp#getSubtitleProvider()}.
	 */
	@Test
	public void testGetSubtitleProvider () {
		assertNotNull(app.getSubtitleProvider());
	}
}
