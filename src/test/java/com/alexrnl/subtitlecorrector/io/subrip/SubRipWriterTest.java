package com.alexrnl.subtitlecorrector.io.subrip;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.alexrnl.subtitlecorrector.common.SubtitleFile;
import com.alexrnl.subtitlecorrector.io.SubtitleWriter;

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
	 * Test method for {@link SubtitleWriter#writeFile(SubtitleFile, Path)}.
	 * @throws URISyntaxException
	 *         if the path to the file is badly formatted.
	 * @throws IOException
	 *         if and IO error occurs.
	 */
	@Test
	public void testWriteFile () throws IOException, URISyntaxException {
		final Path original = Paths.get(getClass().getResource("/Suits.S03E01.srt").toURI());
		final Path output = Files.createTempFile("subtitle", ".srt");
		output.toFile().deleteOnExit();
		
		final SubRipReader reader = new SubRipReader();
		final SubtitleFile subtitle = reader.readFile(original);
		
		writer.writeFile(subtitle, output);
		
		assertArrayEquals(Files.readAllBytes(original), Files.readAllBytes(output));
		
	}
	
	/**
	 * Test method for {@link SubtitleWriter#writeFile(SubtitleFile, Path)}.
	 * @throws IOException
	 *         if and IO error occurs.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWriteFileDirectory () throws IOException {
		final Path temporaryDirectory = Files.createTempDirectory("tmpdir");
		temporaryDirectory.toFile().deleteOnExit();
		writer.writeFile(new SubtitleFile(null), temporaryDirectory);
	}
	

	
	/**
	 * Test method for {@link SubtitleWriter#writeFile(SubtitleFile, Path)}.
	 * @throws IOException
	 *         if and IO error occurs.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWriteFileNotWritable () throws IOException {
		final Path temporaryFile = Files.createTempFile("subtitle", ".srt");
		temporaryFile.toFile().setWritable(false);
		temporaryFile.toFile().deleteOnExit();
		writer.writeFile(new SubtitleFile(null), temporaryFile);
	}
}
