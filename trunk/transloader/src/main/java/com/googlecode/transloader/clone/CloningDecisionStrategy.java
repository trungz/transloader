package com.googlecode.transloader.clone;

public interface CloningDecisionStrategy {
	static final CloningDecisionStrategy MINIMAL = new MinimalCloningDecisionStrategy();

	boolean shouldCloneObjectItself(Object original, ClassLoader targetClassLoader) throws ClassNotFoundException;

	boolean shouldCloneObjectContent(Object original, ClassLoader targetClassLoader) throws ClassNotFoundException;
}
