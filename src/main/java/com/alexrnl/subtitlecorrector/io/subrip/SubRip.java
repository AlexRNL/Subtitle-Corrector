package com.alexrnl.subtitlecorrector.io.subrip;

import java.nio.charset.Charset;

import com.alexrnl.subtitlecorrector.io.SubtitleFormat;

/**
 * Constants used for the definition of the SubRip format.<br />
 * @author Alex
 */
public class SubRip extends SubtitleFormat {
	/** The name of the subtitle format */
	public static final String	SUBRIP_NAME				= "SubRip";
	/** The extension associated to the SubRip */
	public static final String	SUBRIP_EXTENSION		= "srt";
	/** The date format of the subtitle */
	public static final String	SUBRIP_DATE_FORMAT		= "HH:mm:ss,SSS";
	/** The separator between the begin and end date of a subtitle */
	public static final String	SUBRIP_DATE_SEPARATOR	= " --> ";
	
	/**
	 * Constructor #1.<br />
	 * Default constructor.
	 */
	public SubRip () {
		this(Charset.defaultCharset());
	}
	
	/**
	 * Constructor #2.<br />
	 * @param charset
	 *        the character set to use to build the reader/writers.
	 */
	public SubRip (final Charset charset) {
		super(SUBRIP_NAME, new SubRipReader(charset), new SubRipWriter(charset), SUBRIP_EXTENSION);
	}
}
