package com.googlecode.transloader;

import org.apache.commons.lang.exception.NestableRuntimeException;

public final class TransloaderException extends NestableRuntimeException {
	private static final long serialVersionUID = -4924861331560062177L;

	public TransloaderException(String message, Throwable cause) {
		super(message, cause);
	}
}
