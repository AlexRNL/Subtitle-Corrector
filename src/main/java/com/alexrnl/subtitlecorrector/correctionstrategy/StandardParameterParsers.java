package com.alexrnl.subtitlecorrector.correctionstrategy;

import com.alexrnl.subtitlecorrector.correctionstrategy.Parameter.Parser;

/**
 * Standard charset parameter which are used across different strategies.
 * @author Alex
 */
public final class StandardParameterParsers {
	/** Parser for character parameters */
	private static final Parser<Character> CHAR_PARSER = new Parser<Character>() {
		@Override
		public Character parse (final String value) {
			try {
				return value.charAt(0);
			} catch (final IndexOutOfBoundsException e) {
				throw new IllegalArgumentException(e);
			}
		}
	};
	/** Parser for boolean parameters */
	private static final Parser<Boolean> BOOL_PARSER = new Parser<Boolean>() {
		@Override
		public Boolean parse (final String value) {
			// TODO replace with translation
			return value.startsWith("y") || value.startsWith("true");
		}
	};
	/** Parser for string parameters */
	private static final Parser<String> STRING_PARSER = new Parser<String>() {
		@Override
		public String parse (final String value) {
			return value;
		}
	};
	
	/**
	 * Constructor #1.<br />
	 * Default private constructor to avoid instantiation.
	 */
	private StandardParameterParsers () {
		super();
	}
	
	/**
	 * Return the parser for the character parameters.
	 * @return the parser to use.
	 */
	public static Parser<Character> character () {
		return CHAR_PARSER;
	}
	
	/**
	 * Return the parser for the boolean parameters.
	 * @return the parser to use.
	 */
	public static Parser<Boolean> bool () {
		return BOOL_PARSER;
	}
	
	/**
	 * Return the parser for the string parameters.
	 * @return the parser to use.
	 */
	public static Parser<String> string () {
		return STRING_PARSER;
	}
}
