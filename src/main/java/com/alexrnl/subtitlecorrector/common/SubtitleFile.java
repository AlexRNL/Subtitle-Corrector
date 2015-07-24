package com.alexrnl.subtitlecorrector.common;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.alexrnl.commons.utils.CollectionUtils;

/**
 * Represent a single subtitle file.<br />
 * @author Alex
 */
public class SubtitleFile extends TreeSet<Subtitle> {
	/** Serial Version UID */
	private static final long	serialVersionUID	= -5783605599009372323L;
	
	/** The actual file which is represented */
	private final URI			file;
	
	/**
	 * Constructor #1.<br />
	 * @param file
	 *        the file backed-up by this instance.
	 */
	public SubtitleFile (final Path file) {
		super();
		this.file = file == null ? null : file.toUri();
	}
	
	/**
	 * Return the file represented by this instance.
	 * @return the file.
	 */
	public Path getFile () {
		return file == null ? null : Paths.get(file);
	}
	
	/**
	 * Update the subtitle so the ordering is updated if necessary.
	 */
	public void update () {
		if (CollectionUtils.isSorted(this)) {
			return;
		}
		
		final List<Subtitle> backup = new ArrayList<>(this);
		this.clear();
		this.addAll(backup);
	}
}
