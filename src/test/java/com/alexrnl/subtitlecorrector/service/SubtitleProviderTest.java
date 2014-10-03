package com.alexrnl.subtitlecorrector.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;

import com.alexrnl.subtitlecorrector.common.SubtitleFile;
import com.alexrnl.subtitlecorrector.common.TranslationKeys;
import com.alexrnl.subtitlecorrector.io.SubtitleFormat;
import com.alexrnl.subtitlecorrector.io.SubtitleFormatManager;
import com.alexrnl.subtitlecorrector.io.SubtitleReader;

/**
 * Test suite for the {@link SubtitleProvider} class.
 * @author Alex
 */
public class SubtitleProviderTest {
	/** The key to the subtitle provider translations */
	private static final com.alexrnl.subtitlecorrector.common.TranslationKeys.SubtitleProvider	TRANSLATION_KEY	= TranslationKeys.KEYS.subtitleProvider();
	
	/** The provider to test */
	private SubtitleProvider		subtitleProvider;
	/** The mocked subtitle format manager */
	@Mock
	private SubtitleFormatManager	subtitleFormatManager;
	/** The mocked user prompt */
	@Mock
	private UserPrompt				userPrompt;
	/** The temporary folder for the subtitles */
	@Rule
	public final TemporaryFolder	subtitleFolder	= new TemporaryFolder();
	
	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		initMocks(this);
		subtitleProvider = new SubtitleProvider(subtitleFormatManager, userPrompt);
	}
	
	/**
	 * Test method for {@link SubtitleProvider#loadSubtitles(Path)}.
	 */
	@Test
	public void testLoadSubtitles () {
		fail("Not yet implemented"); // TODO
	}
	
	/**
	 * Test that no subtitles are loaded when the path does not exists.
	 */
	@Test
	public void testLoadNonExistingPath () {
		final Path path = Paths.get("abcdefghijkl", "mnopqrstuvwxyz");
		assertTrue(subtitleProvider.loadSubtitles(path).isEmpty());
		verify(userPrompt).error(TRANSLATION_KEY.noAccess(), path);
	}
	
	/**
	 * Test with an empty directory.
	 * @throws IOException
	 *         if there was an I/O error.
	 */
	@Test
	public void testEmptyDirectory () throws IOException {
		assertTrue(subtitleProvider.loadSubtitles(subtitleFolder.newFolder().toPath()).isEmpty());
		verify(userPrompt).warning(TRANSLATION_KEY.noSubtitleToCorrect());
	}
	
	/**
	 * Test with a single subtitle to load.
	 * @throws IOException
	 *         if there was an I/O error.
	 */
	@Test
	public void testSingleSubtitle () throws IOException {
		final SubtitleFormat subtitleFormat = mock(SubtitleFormat.class);
		final SubtitleReader subtitleReader = mock(SubtitleReader.class);
		final SubtitleFile subtitleFile = mock(SubtitleFile.class);
		final Path file = subtitleFolder.newFile("single.srt").toPath();
		
		when(subtitleFormatManager.getFormatByPath(file)).thenReturn(new HashSet<>(Arrays.asList(subtitleFormat)));
		when(subtitleFormat.getReader()).thenReturn(subtitleReader);
		when(subtitleReader.readFile(file)).thenReturn(subtitleFile);
		final Map<SubtitleFile, SubtitleFormat> subtitles = subtitleProvider.loadSubtitles(file);
		assertEquals(1, subtitles.size());
		final Entry<SubtitleFile, SubtitleFormat> loaded = subtitles.entrySet().iterator().next();
		assertEquals(subtitleFile, loaded.getKey());
		assertEquals(subtitleFormat, loaded.getValue());
	}
}
