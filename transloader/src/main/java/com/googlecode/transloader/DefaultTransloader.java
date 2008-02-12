package com.googlecode.transloader;

import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.load.CollectedClassLoader;

/**
 * The default implementation of <code>Transloader</code>.
 * 
 * @author Jeremy Wales
 */
public final class DefaultTransloader implements Transloader {
	private final CloningStrategy cloningStrategy;

	/**
	 * Constructs a new <code>Transloader</code> to produce wrappers, the <code>ObjectWrapper</code>s being
	 * configured with the given <code>CloningStrategy</code>.
	 * 
	 * @param cloningStrategy the <code>CloningStrategy</code> with which to configure <code>ObjectWrapper</code>s
	 */
	public DefaultTransloader(CloningStrategy cloningStrategy) {
		Assert.isNotNull(cloningStrategy);
		this.cloningStrategy = cloningStrategy;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return an <code>ObjectWrapper</code> around the given object, configured with the {@link CloningStrategy} that
	 *         this factory is configured with and with a {@link CollectedClassLoader} for the given object as the
     *         parameter <code>ClassLoader</code>
	 */
	public ObjectWrapper wrap(Object objectToWrap) {
		return new ObjectWrapper(objectToWrap, cloningStrategy, new CollectedClassLoader(objectToWrap));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return a <code>ClassWrapper</code> around the given <code>Class</code>
	 */
	public ClassWrapper wrap(Class classToWrap) {
		return new ClassWrapper(classToWrap);
	}
}
