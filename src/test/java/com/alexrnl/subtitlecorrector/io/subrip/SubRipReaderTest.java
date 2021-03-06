package com.alexrnl.subtitlecorrector.io.subrip;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.alexrnl.subtitlecorrector.common.SubtitleFile;
import com.alexrnl.subtitlecorrector.io.SubtitleReader;

/**
 * Test suite for the {@link SubRipReader} class.
 * @author Alex
 */
public class SubRipReaderTest {
	/** The reader to use for the tests */
	private SubRipReader reader;
	
	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		reader = new SubRipReader();
	}
	
	/**
	 * Test method for {@link SubtitleReader#readFile(Path)}.
	 * @throws URISyntaxException
	 *         if the syntax of the files to test is not valid.
	 * @throws IOException
	 *         if the reading failed.
	 */
	@Test
	public void testReadFile () throws IOException, URISyntaxException {
		final String line = System.lineSeparator();
		final SubtitleFile subtitles = reader.readFile(Paths.get(getClass().getResource("/Suits.S03E01.srt").toURI()));
		assertEquals(1122, subtitles.size());
		assertEquals(-3597953, subtitles.first().getBegin());
		assertEquals(-3593916, subtitles.first().getEnd());
		assertEquals("You lose," + line + "we don't merge." + line, subtitles.first().getContent());
		assertEquals(-761302, subtitles.last().getBegin());
		assertEquals(-756302, subtitles.last().getEnd());
		assertEquals("== sync, corrected by <font color=#00FF00>elderman</font> ==" + line + "<font color=#00FFFF>@elder_man</font>" + line, subtitles.last().getContent());
	}
	
	/**
	 * Test method for {@link SubtitleReader#readFile(Path)}.
	 * @throws IOException
	 *         if there was an issue while reading the subtitle.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadFileNotExists () throws IOException {
		reader.readFile(Paths.get("I", "don't", "exist"));
	}
	
	/**
	 * Test method for {@link SubtitleReader#readFile(Path)}.
	 * @throws IOException
	 *         if there was an issue while reading the subtitle.
	 * @throws URISyntaxException
	 *         if the syntax of the subtitle file is not valid.
	 */
	@Test(expected = IOException.class)
	public void testReadBadDate () throws IOException, URISyntaxException {
		reader.readFile(Paths.get(getClass().getResource("/badDate.srt").toURI()));
	}
	
	/**
	 * Test method for {@link SubtitleReader#readFile(Path)}.
	 * @throws IOException
	 *         if there was an issue while reading the subtitle.
	 * @throws URISyntaxException
	 *         if the syntax of the subtitle file is not valid.
	 */
	@Test(expected = IOException.class)
	public void testReadMissingDate () throws IOException, URISyntaxException {
		reader.readFile(Paths.get(getClass().getResource("/missingDate.srt").toURI()));
	}
	
	/**
	 * Test method for {@link SubtitleReader#readFile(Path)}.
	 * @throws IOException
	 *         if there was an issue while reading the subtitle.
	 * @throws URISyntaxException
	 *         if the syntax of the subtitle file is not valid.
	 */
	@Test(expected = IOException.class)
	public void testReadBadNumber () throws IOException, URISyntaxException {
		reader.readFile(Paths.get(getClass().getResource("/badNumber.srt").toURI()));
	}
}
