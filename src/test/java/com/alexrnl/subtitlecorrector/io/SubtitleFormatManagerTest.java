package com.alexrnl.subtitlecorrector.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the {@link SubtitleFormatManager} class.
 * @author Alex
 */
public class SubtitleFormatManagerTest {
	/** The subtitle format manager to test */
	private SubtitleFormatManager	manager;
	/** The SubRip format */
	private SubtitleFormat			subrip;
	/** The SubStation Alpha format */
	private SubtitleFormat			ass;

	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		manager = new SubtitleFormatManager();
		subrip = new SubtitleFormat("SubRip", null, null, "srt");
		manager.registerFormat(subrip);
		ass = new SubtitleFormat("SubStation Alpha", null, null, "ass", "ssa");
		manager.registerFormat(ass);
	}
	
	/**
	 * Test method for {@link SubtitleFormatManager#registerFormat(SubtitleFormat)}.
	 */
	@Test
	public void testRegisterFormat () {
		final int sizeBefore = manager.getAvailableFormats().size();
		assertTrue(manager.registerFormat(new SubtitleFormat("SubRip", null, null)));
		assertEquals(0, manager.getFormatByName("SubRip").getExtensions().size());
		assertFalse(manager.registerFormat(new SubtitleFormat("SubViewer", null, null, "sub")));
		assertEquals(sizeBefore + 1, manager.getAvailableFormats().size());
	}
	
	/**
	 * Test method for {@link SubtitleFormatManager#getAvailableFormats()}.
	 */
	@Test
	public void testGetAvailableFormats () {
		assertEquals(2, manager.getAvailableFormats().size());
	}
	
	/**
	 * Test that format cannot be altered externally.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testUnmodifiableFormats () {
		manager.getAvailableFormats().clear();
	}
	
	/**
	 * Test method for {@link SubtitleFormatManager#getFormatByName(String)}.
	 */
	@Test
	public void testGetFormatByName () {
		assertEquals(subrip, manager.getFormatByName(subrip.getName()));
		assertEquals(ass, manager.getFormatByName(ass.getName()));
		assertNull(manager.getFormatByName("test"));
	}
	
	/**
	 * Test method for {@link SubtitleFormatManager#getFormatByName(String)}.
	 * With invalid name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetFormatByInvalidName () {
		manager.getFormatByName("");
	}
	
	/**
	 * Test method for {@link SubtitleFormatManager#getFormatByExtension(String)}.
	 */
	@Test
	public void testGetFormatByExtension () {
		Set<SubtitleFormat> srtResults = manager.getFormatByExtension("srt");
		assertEquals(1, srtResults.size());
		assertEquals(subrip, srtResults.iterator().next());
		final Set<SubtitleFormat> assResults = manager.getFormatByExtension("ass");
		assertEquals(1, assResults.size());
		assertEquals(ass, assResults.iterator().next());
		
		final SubtitleFormat subrip2 = new SubtitleFormat("SubRip2", null, null, "srt");
		manager.registerFormat(subrip2);
		srtResults = manager.getFormatByExtension("srt");
		assertEquals(2, srtResults.size());
		assertTrue(srtResults.contains(subrip));
		assertTrue(srtResults.contains(subrip2));
	}
	
	/**
	 * Test method for {@link SubtitleFormatManager#getFormatByExtension(String)}.
	 * With invalid extension.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetFormatByInvalidExtension () {
		manager.getFormatByExtension(null);
	}
	
	/**
	 * Test method for {@link SubtitleFormatManager#getFormatByPath(Path)}.
	 */
	@Test
	public void testGetFormatByPath () {
		assertEquals(subrip, manager.getFormatByPath(Paths.get("src", "test", "subtitles", "mysub.srt")).iterator().next());
		assertEquals(ass, manager.getFormatByPath(Paths.get("src", "test", "subtitles", "mysub.ssa")).iterator().next());
	}
	
	/**
	 * Test method for {@link SubtitleFormatManager#getFormatByPath(Path)}.
	 * With invalid path.
	 */
	@Test(expected = NullPointerException.class)
	public void testGetFormatByInvalidPath () {
		manager.getFormatByPath(null);
	}
}
