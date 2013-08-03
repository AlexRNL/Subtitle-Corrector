package com.alexrnl.subtitlecorrector.correctionstrategy;

/**
 * Enumeration for the parameter type of a strategy.<br />
 * @author Alex
 */
public enum ParameterType {
	/** A parameter whose value may only be <code>true</code> or <code>false</code> */
	BOOLEAN,
	/** A parameter which can be selected though a list of known values */
	LIST,
	/** A parameter which can be freely set */
	FREE;
}
