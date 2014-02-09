package com.alexrnl.subtitlecorrector.io.subrip;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

/**
 * Test suite for the {@link SubRip} class.
 * @author Alex
 */
public class SubRipTest {
	
	/**
	 * Test the validity of the {@link SubRip} constructors.
	 */
	@Test
	public void testConstructors () {
		final SubRip defaultCharset = new SubRip();
		final SubRip latin_1Charset = new SubRip(StandardCharsets.ISO_8859_1);

		assertEquals("SubRip", defaultCharset.getName());
		assertEquals(new HashSet<>(Arrays.asList("srt")), defaultCharset.getExtensions());
		assertEquals("SubRip", latin_1Charset.getName());
		assertEquals(new HashSet<>(Arrays.asList("srt")), latin_1Charset.getExtensions());
	}
}
