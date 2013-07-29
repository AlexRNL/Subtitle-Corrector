package com.alexrnl.subtitlecorrector.io.subrip;

import static org.junit.Assert.fail;

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
	 * Test method for {@link com.alexrnl.subtitlecorrector.io.subrip.SubRipReader#readSubtitle(com.alexrnl.subtitlecorrector.common.SubtitleFile, java.io.BufferedReader)}.
	 */
	@Test
	public void testReadSubtitle () {
		fail("Not yet implemented"); // TODO
	}
	
	/**
	 * Test method for
	 * {@link com.alexrnl.subtitlecorrector.io.SubtitleReader#readFile(java.nio.file.Path)}.
	 * @throws URISyntaxException
	 *         if the syntax of the files to test is not valid.
	 * @throws IOException
	 *         if the reading failed.
	 */
	@Test
	public void testReadFile () throws IOException, URISyntaxException {
		final SubtitleFile subtitles = reader.readFile(Paths.get(getClass().getResource("/Suits - 03x01 - The Arrangement.EVOLVE.English.C.updated.Addic7ed.com.srt").toURI()));
		
		System.out.println(subtitles);
	}
}
