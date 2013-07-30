package com.alexrnl.subtitlecorrector.io.subrip;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.alexrnl.subtitlecorrector.common.SubtitleFile;

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
	 * Test method for {@link com.alexrnl.subtitlecorrector.io.SubtitleReader#readFile(java.nio.file.Path)}.
	 * @throws URISyntaxException
	 *         if the syntax of the files to test is not valid.
	 * @throws IOException
	 *         if the reading failed.
	 */
	@Test
	public void testReadFile () throws IOException, URISyntaxException {
		final SubtitleFile subtitles = reader.readFile(Paths.get(getClass().getResource("/Suits.S03E01.srt").toURI()));
		
		System.out.println(subtitles);
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.io.SubtitleReader#readFile(java.nio.file.Path)}.
	 * @throws IOException
	 *         if there was an issue while reading the subtitle.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadFileNotExists () throws IOException {
		reader.readFile(Paths.get("I", "don't", "exist"));
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.io.SubtitleReader#readFile(java.nio.file.Path)}.
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
	 * Test method for {@link com.alexrnl.subtitlecorrector.io.SubtitleReader#readFile(java.nio.file.Path)}.
	 * @throws IOException
	 *         if there was an issue while reading the subtitle.
	 * @throws URISyntaxException
	 *         if the syntax of the subtitle file is not valid.
	 */
	@Test(expected = IOException.class)
	public void testReadMissingDate () throws IOException, URISyntaxException {
		reader.readFile(Paths.get(getClass().getResource("/missingDate.srt").toURI()));
	}
}
