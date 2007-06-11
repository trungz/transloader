package com.googlecode.transloader;

import com.googlecode.transloader.clone.CloningStrategy;

/**
 * The API by which to wrap objects referencing <code>Class</code>es from foreign <code>ClassLoader</code>s.
 * 
 * @author Jeremy Wales
 */
public interface TransloaderFactory {
	/**
	 * The default <code>TransloaderFactory</code> which will produce {@link ObjectWrapper}s configured with
	 * {@link CloningStrategy#MINIMAL} for <code>Object</code>s and {@link ClassWrapper}s for <code>Class</code>es.
	 */
	TransloaderFactory DEFAULT = new DefaultTransloaderFactory(CloningStrategy.MINIMAL);

	/**
	 * Wraps the given object in an <code>ObjectWrapper</code>.
	 * 
	 * @param objectToWrap the object to wrap
	 * @return the wrapper around the given object
	 */
	ObjectWrapper wrap(Object objectToWrap);

	/**
	 * Wraps the given <code>Class</code> in a <code>ClassWrapper</code>.
	 * 
	 * @param classToWrap the <code>Class</code> to wrap
	 * @return the wrapper around the given <code>Class</code>
	 */
	ClassWrapper wrap(Class classToWrap);
}
