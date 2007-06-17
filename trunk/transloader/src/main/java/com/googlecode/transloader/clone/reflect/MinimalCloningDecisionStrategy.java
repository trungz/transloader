package com.googlecode.transloader.clone.reflect;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import com.googlecode.transloader.ClassWrapper;

public final class MinimalCloningDecisionStrategy implements CloningDecisionStrategy {
	private static final List KNOWN_SHARED_IMMUTABLES =
			Arrays.asList(new Class[] {String.class, BigInteger.class, BigDecimal.class});

	public boolean shouldCloneObjectItself(Object original, ClassLoader targetClassLoader)
			throws ClassNotFoundException {
		return !isSameInClassLoader(original.getClass(), targetClassLoader);
	}

	private boolean isSameInClassLoader(Class originalClass, ClassLoader targetClassLoader)
			throws ClassNotFoundException {
		return originalClass.equals(ClassWrapper.getClass(originalClass.getName(), targetClassLoader));
	}

	public boolean shouldCloneObjectContent(Object original, ClassLoader targetClassLoader)
			throws ClassNotFoundException {
		return !isEffectivelyPrimitive(original.getClass());
	}

	private boolean isEffectivelyPrimitive(Class originalClass) {
		return FieldReflector.PRIMITIVE_WRAPPERS.contains(originalClass) || KNOWN_SHARED_IMMUTABLES.contains(originalClass);
	}
}
