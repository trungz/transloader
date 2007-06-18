package com.googlecode.transloader.clone.reflect;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * Uses {@link ObjenesisStd} to create new instances of <code>Class</code>es without invoking their consrtuctors.
 * 
 * @author Jeremy Wales
 */
public final class ObjenesisInstantiationStrategy implements InstantiationStrategy {
	private final Objenesis objenesis = new ObjenesisStd();

	/**
	 * {@inheritDoc}
	 */
	public Object newInstance(Class type) throws Exception {
		return objenesis.newInstance(type);
	}
}
