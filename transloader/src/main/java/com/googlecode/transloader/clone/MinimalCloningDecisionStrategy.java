package com.googlecode.transloader.clone;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import com.googlecode.transloader.TransloaderClass;

// TODO create MaximalCloningDecisionStrategy
public class MinimalCloningDecisionStrategy implements CloningDecisionStrategy {
	private static final List KNOWN_IMMUTABLES =
			Arrays.asList(new Class[] {Boolean.class, Byte.class, Character.class, Short.class, Integer.class,
					Long.class, Float.class, Double.class, Void.class, BigInteger.class, BigDecimal.class, String.class});

	public boolean shouldCloneObjectItself(Object original, ClassLoader targetClassLoader)
			throws ClassNotFoundException {
		return !isSameInClassLoader(original.getClass(), targetClassLoader);
	}

	private boolean isSameInClassLoader(Class originalClass, ClassLoader targetClassLoader)
			throws ClassNotFoundException {
		return originalClass.equals(new TransloaderClass(originalClass).getEquivalentFrom(targetClassLoader));
	}

	public boolean shouldCloneObjectContent(Object original, ClassLoader targetClassLoader)
			throws ClassNotFoundException {
		return !isEffectivelyPrimitive(original.getClass());
	}

	private boolean isEffectivelyPrimitive(Class originalClass) {
		return originalClass.isPrimitive() || KNOWN_IMMUTABLES.contains(originalClass);
	}
}
