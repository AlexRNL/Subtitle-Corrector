package com.alexrnl.subtitlecorrector.correctionstrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
	
	/**
	 * Set up test attributes.
	 */
	@Before
	public void setUp () {
		requiredParameter = new Parameter<>(ParameterType.BOOLEAN, "ldr/rnr");
		otherParameter = new Parameter<>(ParameterType.LIST, "aba/gta", false, "snow");
	}
	
	/**
	 * Test method for {@link Parameter#getType()}.
	 */
	@Test
	public void testGetType () {
		assertEquals(ParameterType.BOOLEAN, requiredParameter.getType());
		assertEquals(ParameterType.LIST, otherParameter.getType());
	}
	
	/**
	 * Test method for {@link Parameter#getDescription()}.
	 */
	@Test
	public void testGetDescription () {
		assertEquals("ldr/rnr", requiredParameter.getDescription());
		assertEquals("aba/gta", otherParameter.getDescription());
	}
	
	/**
	 * Test method for {@link Parameter#isRequired()}.
	 */
	@Test
	public void testIsRequired () {
		assertTrue(requiredParameter.isRequired());
		assertFalse(otherParameter.isRequired());
	}
	
	/**
	 * Test method for {@link Parameter#getValue()}.
	 */
	@Test
	public void testGetValue () {
		assertNull(requiredParameter.getValue());
		assertEquals("snow", otherParameter.getValue());
	}
	
	/**
	 * Test method for {@link Parameter#setValue(Object)}.
	 */
	@Test
	public void testSetValue () {
		requiredParameter.setValue(true);
		otherParameter.setValue("ls lights");
		
		assertTrue(requiredParameter.getValue());
		assertEquals("ls lights", otherParameter.getValue());
	}
}
