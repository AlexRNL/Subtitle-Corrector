package com.alexrnl.service;

import java.util.logging.Logger;

/**
 * TODO
 * @author Alex
 */
public abstract class Subtitle {
	/** Logger */
	private static Logger	lg	= Logger.getLogger(Subtitle.class.getName());
	
	/** Timestamp for beginning of subtitle display */
	private long			begin;
	/** Timestamp for end of subtitle display */
	private long			end;
	/** The content of the subtitle */
	private String			content;
}
