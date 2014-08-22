package com.alexrnl.subtitlecorrector.correctionstrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test suite for the {@link StandardParameterParsers} class.
 * @author Alex
 */
public class StandardParameterParsersTest {
	
	/**
	 * Test method for {@link StandardParameterParsers#character()}.
	 */
	@Test
	public void testCharacter () {
		assertEquals('h', StandardParameterParsers.character().parse("hjo").charValue());
	}
	
	/**
	 * Test method for empty strings in {@link StandardParameterParsers#character()}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCharacterEmptyValue () {
		StandardParameterParsers.character().parse("");
	}
	
	/**
	 * Test method for {@link StandardParameterParsers#bool()}.
	 */
	@Test
	public void testBool () {
		assertTrue(StandardParameterParsers.bool().parse("yes"));
		assertTrue(StandardParameterParsers.bool().parse("true"));
		assertFalse(StandardParameterParsers.bool().parse("no"));
		assertFalse(StandardParameterParsers.bool().parse("false"));
	}
	
	/**
	 * Test method for {@link StandardParameterParsers#string()}.
	 */
	@Test
	public void testString () {
		assertEquals("hjo", StandardParameterParsers.string().parse("hjo"));
	}
}
