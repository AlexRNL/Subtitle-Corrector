package com.alexrnl.service;

import java.nio.file.Path;
import java.util.NavigableSet;
import java.util.logging.Logger;

/**
 * TODO
 * @author Alex
 */
public class SubtitleFile<T extends Subtitle> {
	/** Logger */
	private static Logger	lg	= Logger.getLogger(SubtitleFile.class.getName());
	
	/** The name of the file is represent */
	private String			fileName;
	/** The actual file which is represented */
	private Path			subtitle;
	/** The list of subtitles of a file */
	private NavigableSet<T>	subtitles;
	/** If <code>true</code>, */
	private boolean			sync;
}
