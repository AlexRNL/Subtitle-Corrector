package com.alexrnl.subtitlecorrector.io.subrip;


/**
 * Constants used for the definition of the SubRip format.<br />
 * @author Alex
 */
public final class SubRip {
	
	/**
	 * Constructor #1.<br />
	 * Default private constructor.
	 */
	private SubRip () {
		super();
	}
	
	/** The date format of the subtitle */
	public static final String	SUBRIP_DATE_FORMAT		= "HH:mm:ss,SSS";
	/** The separator between the begin and end date of a subtitle */
	public static final String	SUBRIP_DATE_SEPARATOR	= " --> ";
	
}
