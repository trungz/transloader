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
	 * Contructs a new <code>TransloaderFactory</code> which will produce <code>TransloaderWrapper</code>s
	 * configured with {@link CloningStrategy#MINIMAL}.
	 */
	public DefaultTransloaderFactory() {
		this(CloningStrategy.MINIMAL);
	}

	/**
	 * Contructs a new <code>TransloaderFactory</code> which will produce <code>TransloaderWrapper</code>s
	 * configured with the given <code>CloningStrategy</code>.
	 * 
	 * @param cloningStrategy the <code>CloningStrategy</code> with which to configure <code>TransloaderWrapper</code>s.
	 */
	public DefaultTransloaderFactory(CloningStrategy cloningStrategy) {
		this.cloningStrategy = cloningStrategy;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TransloaderFactory#wrap(Object)
	 * @return a {@link ClassWrapper} if the given object is a <code>Class</code>, an {@link ObjectWrapper}
	 *         otherwise, both constructed with the {@link CloningStrategy} that this factory is configured with
	 */
	public TransloaderWrapper wrap(Object object) {
		if (object instanceof Class) return new ClassWrapper(((Class) object), cloningStrategy);
		return new ObjectWrapper(object, cloningStrategy);
	}
}
