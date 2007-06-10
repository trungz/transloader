package com.googlecode.transloader;

/**
 * A factory for producing <code>TransloaderWrapper</code>s.
 * 
 * @author Jeremy Wales
 */
public interface TransloaderFactory {
	/**
	 * Wraps the given object in a <code>TransloaderWrapper</code>.
	 * 
	 * @param object the object to wrap.
	 * @return the wrapper around the given object.
	 */
	TransloaderWrapper wrap(Object object);
}
