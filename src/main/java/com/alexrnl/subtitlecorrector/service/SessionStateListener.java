package com.alexrnl.subtitlecorrector.service;


/**
 * Interface for listeners on the session state.<br />
 * @author Alex
 */
public interface SessionStateListener {
	
	/**
	 * Called when the correcting session is starting.
	 * @param parameters
	 *        the parameters of the session.
	 */
	void startSession (SessionParameters parameters);
	
	/**
	 * Called when the correcting session is finished.
	 */
	void stopSession ();
}
