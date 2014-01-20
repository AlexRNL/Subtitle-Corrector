package com.alexrnl.subtitlecorrector.io;

import java.util.Map;
import java.util.logging.Logger;

import com.alexrnl.subtitlecorrector.common.SubtitleFile;

/**
 * Manager to ease the use of the {@link SubtitleFile} reading / writing.<br />
 * TODO
 * @author Alex
 */
public class SubtitleFormatManager {
	/** Logger */
	private static Logger	lg	= Logger.getLogger(SubtitleFormatManager.class.getName());
	
	/** The subtitle readers */
	private Map<String, SubtitleReader> readers;
	/** The subtitle writers */
	private Map<String, SubtitleWriter> writers;
}
