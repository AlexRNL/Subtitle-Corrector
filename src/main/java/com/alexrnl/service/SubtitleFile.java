package com.alexrnl.service;

import java.nio.file.Path;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.logging.Logger;

/**
 * TODO
 * @author Alex
 */
public class SubtitleFile {
	/** Logger */
	private static Logger					lg	= Logger.getLogger(SubtitleFile.class.getName());
	
	/** The actual file which is represented */
	private final Path						file;
	/** The list of subtitles of a file */
	private final NavigableSet<Subtitle>	subtitles;
	
	public SubtitleFile (final Path file) {
		super();
		this.file = file;
		this.subtitles = new TreeSet<>();
	}
	
	/**
	 * Add a subtitle to the file.
	 * @param subtitle
	 *        the subtitle to add.
	 */
	public void addSubtitle (final Subtitle subtitle) {
		if (subtitle == null) {
			lg.warning("Cannot add null subtitle");
			return;
		}
		subtitles.add(subtitle);
	}
	
}
