package com.alexrnl.subtitlecorrector.correctionstrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the {@link Parameter} class.
 * @author Alex
 */
public class ParameterTest {
	/** A parameter which is required (and has no default value) */
	private Parameter<Boolean>	requiredParameter;
	/** A parameter without default value */
	private Parameter<String>	otherParameter;
	/** A parameter with a list of possible values */
	private Parameter<String>	listParameter;
	
	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		requiredParameter = new Parameter<>(ParameterType.BOOLEAN, "ldr/rnr", StandardParameterParsers.bool());
		otherParameter = new Parameter<>(ParameterType.FREE, "aba/gta", false, StandardParameterParsers.string(), "snow");
		listParameter = new Parameter<>(ParameterType.LIST, "hjo//", StandardParameterParsers.string(), Arrays.asList("hjo", "mad"));
	}
	
	/**
	 * Test that a list parameter cannot be build with no possible values.
	 */
	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void testListWithNoValues () {
		new Parameter<String>(ParameterType.LIST, "aba", StandardParameterParsers.string());
	}
	
	/**
	 * Test method for {@link Parameter#getType()}.
	 */
	@Test
	public void testGetType () {
		assertEquals(ParameterType.BOOLEAN, requiredParameter.getType());
		assertEquals(ParameterType.FREE, otherParameter.getType());
		assertEquals(ParameterType.LIST, listParameter.getType());
	}
	
	/**
	 * Test method for {@link Parameter#getDescription()}.
	 */
	@Test
	public void testGetDescription () {
		assertEquals("ldr/rnr", requiredParameter.getDescription());
		assertEquals("aba/gta", otherParameter.getDescription());
		assertEquals("hjo//", listParameter.getDescription());
	}
	
	/**
	 * Test method for {@link Parameter#isRequired()}.
	 */
	@Test
	public void testIsRequired () {
		assertTrue(requiredParameter.isRequired());
		assertFalse(otherParameter.isRequired());
		assertTrue(listParameter.isRequired());
	}
	
	/**
	 * Test method for {@link Parameter#getValue()}.
	 */
	@Test
	public void testGetValue () {
		assertNull(requiredParameter.getValue());
		assertEquals("snow", otherParameter.getValue());
		assertNull(listParameter.getValue());
	}
	
	/**
	 * Test method for {@link Parameter#setValue(String)}.
	 */
	@Test
	public void testSetValue () {
		requiredParameter.setValue("y");
		otherParameter.setValue("ls lights");
		listParameter.setValue("hjo");
		
		assertTrue(requiredParameter.getValue());
		assertEquals("ls lights", otherParameter.getValue());
		assertEquals("hjo", listParameter.getValue());
	}
	
	/**
	 * Test method for {@link Parameter#getPossibleValues()}.
	 */
	@Test
	public void testGetPossibleValues () {
		assertNull(requiredParameter.getPossibleValues());
		assertNull(otherParameter.getPossibleValues());
		assertEquals(Arrays.asList("hjo", "mad"), new ArrayList<>(listParameter.getPossibleValues()));
	}
	
	/**
	 * Test method for {@link Parameter#setValue(String)} for a list parameter.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetValueNotInList () {
		listParameter.setValue("aba");
	}
}
