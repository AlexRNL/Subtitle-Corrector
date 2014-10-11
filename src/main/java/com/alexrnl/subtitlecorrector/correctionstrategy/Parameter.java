package com.alexrnl.subtitlecorrector.correctionstrategy;

import java.util.Collection;
import java.util.Collections;


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
	/** The possible values for the parameter */
	private final Collection<T>	possibleValues;
	
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
	 * Decorator for a parser which allow to validate values when a collection of possible values is
	 * provided.
	 * @author Alex
	 */
	private class ParameterValueValidator implements Parser<T> {
		/** The parser to decorate */
		private final Parser<T>	innerParser;
		
		/**
		 * Constructor #1.<br />
		 * @param innerParser
		 *        the innerParser to decorate.
		 */
		private ParameterValueValidator (final Parser<T> innerParser) {
			super();
			this.innerParser = innerParser;
		}
		
		@Override
		public T parse (final String strValue) throws IllegalArgumentException {
			final T parameter = innerParser.parse(strValue);
			if (!possibleValues.contains(parameter)) {
				throw new IllegalArgumentException("The value " + parameter + " is not in the allowed values: " + possibleValues);
			}
			return parameter;
		}
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
	 * @param possibleValues
	 *        the possible values for the parameter, or <code>null</code> if it is a free parameter.
	 */
	public Parameter (final ParameterType type, final String description, final boolean required,
			final Parser<T> parser, final T defaultValue, final Collection<T> possibleValues) {
		super();
		this.type = type;
		this.description = description;
		this.required = required;
		this.value = defaultValue;
		this.possibleValues = possibleValues == null ? null : Collections.unmodifiableCollection(possibleValues);
		this.parser = this.possibleValues != null ? new ParameterValueValidator(parser) : parser;
		if (this.type == ParameterType.LIST && this.possibleValues == null) {
			throw new IllegalArgumentException("Cannot build a LIST type parameter without a " +
					"possible value collection provided.");
		}
	}
	
	/**
	 * Constructor #2.<br />
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
		this(type, description, required, parser, defaultValue, null);
	}

	/**
	 * Constructor #3.<br />
	 * Build a parameter with no default value (which is therefore, required).
	 * @param type
	 *        the type of the parameter.
	 * @param description
	 *        the description of the parameter.
	 * @param parser
	 *        the parser to convert a string into the parameter type.
	 */
	public Parameter (final ParameterType type, final String description, final Parser<T> parser) {
		this(type, description, parser, null);
	}

	/**
	 * Constructor #4.<br />
	 * Build a parameter with no default value (which is therefore, required).
	 * @param type
	 *        the type of the parameter.
	 * @param description
	 *        the description of the parameter.
	 * @param parser
	 *        the parser to convert a string into the parameter type.
	 * @param possibleValues
	 *        the possible values for the parameter, or <code>null</code> if it is a free parameter.
	 */
	public Parameter (final ParameterType type, final String description, final Parser<T> parser,
			final Collection<T> possibleValues) {
		this(type, description, true, parser, null, possibleValues);
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

	/**
	 * Return the attribute possibleValues.
	 * @return the attribute possibleValues.
	 */
	public Collection<T> getPossibleValues () {
		return possibleValues;
	}
	
}
