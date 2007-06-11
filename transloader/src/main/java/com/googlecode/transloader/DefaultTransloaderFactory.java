package com.googlecode.transloader;

import com.googlecode.transloader.clone.CloningStrategy;

/**
 * The default implementation of <code>TransloaderFactory</code>.
 * 
 * @author Jeremy Wales
 */
public final class DefaultTransloaderFactory implements TransloaderFactory {
	private final CloningStrategy cloningStrategy;

	/**
	 * Contructs a new <code>TransloaderFactory</code> which will produce wrappers, the <code>ObjectWrapper</code>s
	 * being configured with the given <code>CloningStrategy</code>.
	 * 
	 * @param cloningStrategy the <code>CloningStrategy</code> with which to configure <code>ObjectWrapper</code>s
	 */
	public DefaultTransloaderFactory(CloningStrategy cloningStrategy) {
		this.cloningStrategy = cloningStrategy;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return an <code>ObjectWrapper</code> around the given object, configured with the {@link CloningStrategy} that
	 *         this factory is configured with
	 */
	public ObjectWrapper wrap(Object objectToWrap) {
		return new ObjectWrapper(objectToWrap, cloningStrategy);
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
