package com.alexrnl.subtitlecorrector.io.subrip;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the {@link SubRipWriter} class.
 * @author Alex
 */
public class SubRipWriterTest {
	/** The writer to use for the test */
	private SubRipWriter	writer;

	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		writer = new SubRipWriter();
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.io.SubtitleWriter#writeFile(com.alexrnl.subtitlecorrector.common.SubtitleFile, java.nio.file.Path)}.
	 */
	@Test
	public void testWriteFile () {
		fail("Not yet implemented"); // TODO
	}
}
