package com.googlecode.transloader.clone;

public class MaximalCloningDecisionStrategy implements CloningDecisionStrategy {
	public boolean shouldCloneObjectItself(Object original, ClassLoader targetClassLoader)
			throws ClassNotFoundException {
		return true;
	}

	public boolean shouldCloneObjectContent(Object original, ClassLoader targetClassLoader)
			throws ClassNotFoundException {
		return shouldCloneObjectItself(original, targetClassLoader);
	}
}
