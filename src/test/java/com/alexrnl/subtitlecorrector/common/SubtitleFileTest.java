package com.alexrnl.subtitlecorrector.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the {@link SubtitleFile} class.
 * @author Alex
 */
public class SubtitleFileTest {
	/** An empty subtitle file */
	private SubtitleFile	emptyFile;
	/** A file with subtitles */
	private SubtitleFile	file;
	
	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		emptyFile = new SubtitleFile(null);
		file = new SubtitleFile(Paths.get("this", "is", "dumb"));
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.SubtitleFile#getFile()}.
	 */
	@Test
	public void testGetFile () {
		assertNull(emptyFile.getFile());
		assertEquals("this" + File.separatorChar + "is" + File.separatorChar + "dumb", file.getFile().toString());
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.SubtitleFile#update()}.
	 */
	@Test
	public void testUpdate () {
		fail("Not yet implemented"); // TODO
	}
}
