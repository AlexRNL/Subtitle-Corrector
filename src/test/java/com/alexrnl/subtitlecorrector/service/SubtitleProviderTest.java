package com.alexrnl.subtitlecorrector.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.nio.file.Files;
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
	 * Check that multiple subtitles are loaded.
	 * @throws IOException
	 *         if there was an I/O error.
	 */
	@Test
	public void testLoadSubtitles () throws IOException {
		final SubtitleFormat subtitleFormat = mock(SubtitleFormat.class);
		final SubtitleReader subtitleReader = mock(SubtitleReader.class);
		final SubtitleFile subtitleFile = mock(SubtitleFile.class);
		final SubtitleFile secondSubtitle = mock(SubtitleFile.class);
		final Path folder = subtitleFolder.newFolder().toPath();
		final Path file = folder.resolve("single.srt");
		final Path second = folder.resolve("other.srt");
		Files.createFile(file);
		Files.createFile(second);
		Files.createFile(folder.resolve("test.sub"));
		
		when(subtitleFormatManager.getFormatByPath(file)).thenReturn(new HashSet<>(Arrays.asList(subtitleFormat)));
		when(subtitleFormatManager.getFormatByPath(second)).thenReturn(new HashSet<>(Arrays.asList(subtitleFormat)));
		when(subtitleFormat.getReader()).thenReturn(subtitleReader);
		when(subtitleReader.readFile(file)).thenReturn(subtitleFile);
		when(subtitleReader.readFile(second)).thenReturn(secondSubtitle);
		final Map<SubtitleFile, SubtitleFormat> subtitles = subtitleProvider.loadSubtitles(folder);
		assertEquals(2, subtitles.size());
//		final Entry<SubtitleFile, SubtitleFormat> loaded = subtitles.entrySet().iterator().next();
//		assertEquals(subtitleFile, loaded.getKey());
//		assertEquals(subtitleFormat, loaded.getValue());
	}
	
	/**
	 * Test when a {@link SubtitleReader} throws an exception.
	 * @throws IOException
	 *         if there is an I/O error.
	 */
	@Test
	public void testLoadSubtitleReaderError () throws IOException {
		final SubtitleFormat subtitleFormat = mock(SubtitleFormat.class);
		final SubtitleReader subtitleReader = mock(SubtitleReader.class);
		final SubtitleFile subtitleFile = mock(SubtitleFile.class);
		final Path file = subtitleFolder.newFile("readexception.srt").toPath();
		
		when(subtitleFormatManager.getFormatByPath(file)).thenReturn(new HashSet<>(Arrays.asList(subtitleFormat)));
		when(subtitleFormat.getReader()).thenReturn(subtitleReader);
		when(subtitleReader.readFile(file)).thenThrow(IOException.class);
		final Map<SubtitleFile, SubtitleFormat> subtitles = subtitleProvider.loadSubtitles(file);
		assertTrue(subtitles.isEmpty());
		verify(userPrompt).warning(TRANSLATION_KEY.subtitleFileReadError(), file);
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
	
	/**
	 * Test with a subtitle which has no proper format to read.
	 * @throws IOException
	 *         if there was an I/O error.
	 */
	@Test
	public void testSingleSubtitleWithBadFormat () throws IOException {
		final Path file = subtitleFolder.newFile("single.srt").toPath();
		when(subtitleFormatManager.getFormatByPath(file)).thenReturn(new HashSet<SubtitleFormat>());
		final Map<SubtitleFile, SubtitleFormat> subtitles = subtitleProvider.loadSubtitles(file);
		assertTrue(subtitles.isEmpty());
		verify(userPrompt).askChoice(anyCollectionOf(SubtitleFormat.class), eq(TRANSLATION_KEY.chooseSubtitleFormat()), eq(file));
	}
	
	/**
	 * Test with a subtitle with multiple format to read.
	 * @throws IOException
	 *         if there was an I/O error.
	 */
	@Test
	public void testMultipleFormatSubtitle () throws IOException {
		final SubtitleFormat subtitleFormat = mock(SubtitleFormat.class);
		final SubtitleReader subtitleReader = mock(SubtitleReader.class);
		final SubtitleFile subtitleFile = mock(SubtitleFile.class);
		final Path file = subtitleFolder.newFile("single.srt").toPath();
		
		when(subtitleFormatManager.getFormatByPath(file)).thenReturn(new HashSet<>(Arrays.asList(subtitleFormat, mock(SubtitleFormat.class))));
		when(userPrompt.askChoice(anyCollectionOf(SubtitleFormat.class), eq(TRANSLATION_KEY.chooseSubtitleFormat()), eq(file))).thenReturn(subtitleFormat);
		when(subtitleFormat.getReader()).thenReturn(subtitleReader);
		when(subtitleReader.readFile(file)).thenReturn(subtitleFile);
		final Map<SubtitleFile, SubtitleFormat> subtitles = subtitleProvider.loadSubtitles(file);
		assertEquals(1, subtitles.size());
		final Entry<SubtitleFile, SubtitleFormat> loaded = subtitles.entrySet().iterator().next();
		assertEquals(subtitleFile, loaded.getKey());
		assertEquals(subtitleFormat, loaded.getValue());
		verify(userPrompt).askChoice(anyCollectionOf(SubtitleFormat.class), eq(TRANSLATION_KEY.chooseSubtitleFormat()), eq(file));
	}
}
