package com.alexrnl.subtitlecorrector.service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Session manager.<br />
 * This class can manage several {@link SessionStateListener} and synchronize the
 * {@link #startSession(SessionParameters)} and {@link #stopSession()} session.<br />
 * The manager is thread-safe: it can be used across different thread as both the listeners and
 * session start/stop are synchronized.
 * @author Alex
 */
public class SessionManager implements SessionStateListener {
	/** Logger */
	private static Logger	lg	= Logger.getLogger(SessionManager.class.getName());
	
	/** Flag indicating that a session is started and in progress */
	private Boolean								inSession;
	/** The session state listener list */
	private final List<SessionStateListener>	sessionListeners;
	
	/**
	 * Constructor #1.<br />
	 */
	public SessionManager () {
		super();
		sessionListeners = new LinkedList<>();
		inSession = false;
	}
	
	/**
	 * Return an unmodifiable view of the session listeners.
	 * @return the session listeners.
	 */
	protected Collection<SessionStateListener> getSessionListeners () {
		synchronized (sessionListeners) {
			return Collections.unmodifiableCollection(new LinkedList<>(sessionListeners));
		}
	}
	
	/**
	 * Add a session listener to the internal collection of the manager.
	 * @param listener
	 *        the listener to add.
	 * @return <code>true</code> if the session listener was added (<code>false</code> if it is
	 *         already in the list).
	 */
	public boolean addSessionListener (final SessionStateListener listener) {
		if (listener == this) {
			throw new IllegalArgumentException("The session manager cannot add itself to the collection");
		}
		if (listener == null) {
			throw new IllegalArgumentException("Cannot add null session listener to the session manager");
		}
		synchronized (sessionListeners) {
			if (sessionListeners.contains(listener)) {
				return false;
			}
			return sessionListeners.add(listener);
		}
	}
	
	/**
	 * Remove a session listener from the internal collection of the manager.
	 * @param listener
	 *        the listener to remove.
	 * @return <code>true</code> if the session listener was in the collection (and thus removed).
	 */
	public boolean removeSessionListener (final SessionStateListener listener) {
		synchronized (sessionListeners) {
			return sessionListeners.remove(listener);
		}
	}
	
	/**
	 * Check if the session is in progress.
	 * @return <code>true</code> if the manager is in session.
	 */
	public boolean isInSession () {
		synchronized (inSession) {
			return inSession;
		}
	}
	
	@Override
	public void startSession (final SessionParameters parameters) {
		synchronized (inSession) {
			if (inSession) {
				throw new IllegalStateException("Session is already started");
			}
			inSession = true;
		}
		for (final SessionStateListener sessionListener : getSessionListeners()) {
			sessionListener.startSession(parameters);
		}
		if (lg.isLoggable(Level.INFO)) {
			lg.info("Session manager has successfully started the session");
		}
	}
	
	@Override
	public void stopSession () {
		synchronized (inSession) {
			if (!inSession) {
				throw new IllegalStateException("Session is not started");
			}
			inSession = false;
		}
		for (final SessionStateListener sessionListener : getSessionListeners()) {
			sessionListener.stopSession();
		}
		if (lg.isLoggable(Level.INFO)) {
			lg.info("Session manager has successfully stopped the session");
		}
	}
}
