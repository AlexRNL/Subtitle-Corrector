package com.alexrnl.subtitlecorrector.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the {@link Subtitle} class.
 * @author Alex
 */
public class SubtitleTest {
	/** The current time */
	private long			currentTime;
	/** Empty subtitle */
	private Subtitle		empty;
	/** A valid subtitle */
	private Subtitle		validSubtitle;
	/** An invalid subtitle */
	private Subtitle		invalidSubtitle;
	/** List with all the subtitles to test */
	private List<Subtitle>	subtitles;
	
	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		currentTime = System.currentTimeMillis();
		empty = new Subtitle();
		validSubtitle = new Subtitle(currentTime - 1000, currentTime, "<i>This is a test</i>");
		invalidSubtitle = new Subtitle(currentTime + 1000, currentTime, "LDR\n- ABA");
		subtitles = new LinkedList<>();
		subtitles.add(empty);
		subtitles.add(validSubtitle);
		subtitles.add(invalidSubtitle);
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.Subtitle#getBegin()}.
	 */
	@Test
	public void testGetBegin () {
		assertEquals(0, empty.getBegin());
		assertEquals(currentTime - 1000, validSubtitle.getBegin());
		assertEquals(currentTime + 1000, invalidSubtitle.getBegin());
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.Subtitle#setBegin(long)}.
	 */
	@Test
	public void testSetBegin () {
		final long actualTime = System.currentTimeMillis();
		empty.setBegin(actualTime);
		validSubtitle.setBegin(actualTime);
		invalidSubtitle.setBegin(actualTime);
		assertEquals(actualTime, empty.getBegin());
		assertEquals(actualTime, validSubtitle.getBegin());
		assertEquals(actualTime, invalidSubtitle.getBegin());
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.Subtitle#getEnd()}.
	 */
	@Test
	public void testGetEnd () {
		assertEquals(0, empty.getEnd());
		assertEquals(currentTime, validSubtitle.getEnd());
		assertEquals(currentTime, invalidSubtitle.getEnd());
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.Subtitle#setEnd(long)}.
	 */
	@Test
	public void testSetEnd () {
		final long actualTime = System.currentTimeMillis();
		empty.setEnd(actualTime);
		validSubtitle.setEnd(actualTime);
		invalidSubtitle.setEnd(actualTime);
		assertEquals(actualTime, empty.getEnd());
		assertEquals(actualTime, validSubtitle.getEnd());
		assertEquals(actualTime, invalidSubtitle.getEnd());
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.Subtitle#getContent()}.
	 */
	@Test
	public void testGetContent () {
		assertNull(empty.getContent());
		assertEquals("<i>This is a test</i>", validSubtitle.getContent());
		assertEquals("LDR\n- ABA", invalidSubtitle.getContent());
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.Subtitle#setContent(java.lang.String)}.
	 */
	@Test
	public void testSetContent () {
		empty.setContent("");
		validSubtitle.setContent("LLLLLDDDAAA");
		invalidSubtitle.setContent("No, I am your father");
		assertEquals("", empty.getContent());
		assertEquals("LLLLLDDDAAA", validSubtitle.getContent());
		assertEquals("No, I am your father", invalidSubtitle.getContent());
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.Subtitle#getDuration()}.
	 */
	@Test
	public void testGetDuration () {
		assertEquals(0, empty.getDuration());
		assertEquals(1000, validSubtitle.getDuration());
		assertEquals(-1000, invalidSubtitle.getDuration());
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.Subtitle#isValid()}.
	 */
	@Test
	public void testIsValid () {
		assertFalse(empty.isValid());
		assertTrue(validSubtitle.isValid());
		assertFalse(invalidSubtitle.isValid());
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.Subtitle#toString()}.
	 */
	@Test
	public void testToString () {
		assertEquals("[0, 0] null", empty.toString());
		assertEquals("[" + (currentTime - 1000) + ", " + currentTime + "] <i>This is a test</i>", validSubtitle.toString());
		assertEquals("[" + (currentTime + 1000) + ", " + currentTime + "] LDR\n- ABA", invalidSubtitle.toString());
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.Subtitle#compareTo(com.alexrnl.subtitlecorrector.common.Subtitle)}.
	 * @throws CloneNotSupportedException
	 *         if a clone operation fails.
	 */
	@Test
	public void testCompareTo () throws CloneNotSupportedException {
		for (final Subtitle subtitle : subtitles) {
			final Subtitle before = subtitle.clone();
			final Subtitle after = subtitle.clone();
			before.setBegin(before.getBegin() - 1);
			after.setBegin(after.getBegin() + 1);

			assertTrue(before.compareTo(subtitle) < 0);
			assertTrue(after.compareTo(subtitle) > 0);
		}
	}
	
	/**
	 * Test method for {@link com.alexrnl.subtitlecorrector.common.Subtitle#clone()}.
	 * @throws CloneNotSupportedException
	 *         if a clone operation fails.
	 */
	@Test
	public void testClone () throws CloneNotSupportedException {
		for (final Subtitle subtitle : subtitles) {
			final Subtitle clone = subtitle.clone();
			assertEquals(subtitle.getBegin(), clone.getBegin());
			assertEquals(subtitle.getEnd(), clone.getEnd());
			assertEquals(subtitle.getContent(), clone.getContent());
		}
	}
}
