package com.googlecode.transloader;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class TransloadingException extends NestableRuntimeException {
	private static final long serialVersionUID = -4924861331560062177L;

	public TransloadingException(String message, Throwable cause) {
		super(message, cause);
	}
}
