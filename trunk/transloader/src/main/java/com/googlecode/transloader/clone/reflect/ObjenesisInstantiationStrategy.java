package com.googlecode.transloader.clone.reflect;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public final class ObjenesisInstantiationStrategy implements InstantiationStrategy {
	private final Objenesis objenesis = new ObjenesisStd();

	public Object newInstance(Class type) throws Exception {
		return objenesis.newInstance(type);
	}
}
