package com.googlecode.transloader;

import com.googlecode.transloader.clone.CloningStrategy;

public final class DefaultTransloaderFactory implements TransloaderFactory {
	private final CloningStrategy cloningStrategy;

	public DefaultTransloaderFactory() {
		this(CloningStrategy.MINIMAL);
	}

	public DefaultTransloaderFactory(CloningStrategy cloningStrategy) {
		this.cloningStrategy = cloningStrategy;
	}

	public TransloaderWrapper wrap(Object object) {
		if (object instanceof Class) return new ClassWrapper(((Class) object), cloningStrategy);
		return new ObjectWrapper(object, cloningStrategy);
	}
}
