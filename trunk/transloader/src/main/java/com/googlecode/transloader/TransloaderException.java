package com.googlecode.transloader;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * The <code>RuntimeException</code> thrown by the Transloader library itself.
 * 
 * @author Jeremy Wales
 */
public final class TransloaderException extends NestableRuntimeException {
	private static final long serialVersionUID = -4924861331560062177L;

	/**
	 * {@inheritDoc}
	 * 
	 * @see NestableRuntimeException#NestableRuntimeException(String, Throwable)
	 */
	public TransloaderException(String message, Throwable cause) {
		super(message, cause);
	}
}
