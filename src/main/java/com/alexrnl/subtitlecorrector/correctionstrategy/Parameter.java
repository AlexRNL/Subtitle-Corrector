package com.alexrnl.subtitlecorrector.correctionstrategy;


/**
 * A parameter of a strategy.<br />
 * @author Alex
 * @param <T>
 *        the type of the parameter.
 */
public class Parameter<T> {
	
	/** The type of parameter represented */
	private final ParameterType	type;
	/** The description of the parameter (usually a translation key) */
	private final String		description;
	/** <code>true</code> if the parameter is required */
	private final boolean		required;
	/** The value of the parameter */
	private T					value;
	/** The parser for the parameter */
	private final Parser<T>		parser;
	
	/**
	 * Interface for parsing String into the parameter type.
	 * @author Alex
	 * @param <U>
	 *        the target parameter type.
	 */
	public static interface Parser<U> {
		/**
		 * Parse the string value specified into the target type.
		 * @param value
		 *        the value to parse.
		 * @return the actual value of the parameter to use.
		 * @throws IllegalArgumentException
		 *         if the parsing could not be completed.
		 */
		U parse (String value) throws IllegalArgumentException;
	}
	
	/**
	 * Constructor #1.<br />
	 * @param type
	 *        the type of the parameter.
	 * @param description
	 *        the description of the parameter.
	 * @param required
	 *        <code>true</code> if the parameter is required.
	 * @param parser
	 *        the parser to convert a string into the parameter type.
	 * @param defaultValue
	 *        the default value of the parameter.
	 */
	public Parameter (final ParameterType type, final String description, final boolean required,
			final Parser<T> parser, final T defaultValue) {
		super();
		this.type = type;
		this.description = description;
		this.required = required;
		this.parser = parser;
		this.value = defaultValue;
	}
	
	/**
	 * Constructor #2.<br />
	 * Build a parameter with no default value (which is therefore, required).
	 * @param type
	 *        the type of the parameter.
	 * @param description
	 *        the description of the parameter.
	 * @param parser
	 *        the parser to convert a string into the parameter type.
	 */
	public Parameter (final ParameterType type, final String description, final Parser<T> parser) {
		this(type, description, true, parser, null);
	}

	/**
	 * Return the attribute type.
	 * @return the attribute type.
	 */
	public ParameterType getType () {
		return type;
	}

	/**
	 * The description of the parameter.
	 * @return the description which can be shown in an HMI.
	 */
	public String getDescription () {
		return description;
	}

	/**
	 * Return the attribute required.
	 * @return the attribute required.
	 */
	public boolean isRequired () {
		return required;
	}

	/**
	 * Return the attribute value.
	 * @return the attribute value.
	 */
	public T getValue () {
		return value;
	}

	/**
	 * Set the attribute value.
	 * @param value the attribute value.
	 */
	public void setValue (final String value) {
		this.value = parser.parse(value);
	}
	
}
