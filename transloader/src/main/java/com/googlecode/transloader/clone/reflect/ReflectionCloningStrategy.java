package com.googlecode.transloader.clone.reflect;

import java.util.Map;

import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.clone.reflect.CyclicReferenceSafeTraverser.Traversal;

public final class ReflectionCloningStrategy implements CloningStrategy {
	private final CyclicReferenceSafeTraverser cyclicReferenceSafeTraverser = new CyclicReferenceSafeTraverser();

	private final CloningDecisionStrategy decider;
	private final InnerCloner arrayCloner;
	private final InnerCloner normalObjectCloner;
	private final CloningStrategy fallbackCloner;

	public ReflectionCloningStrategy(CloningDecisionStrategy cloningDecisionStrategy,
			InstantiationStrategy instantiator, CloningStrategy fallbackCloningStrategy) {
		decider = cloningDecisionStrategy;
		arrayCloner = new InnerArrayCloner(this);
		normalObjectCloner = new InnerNormalObjectCloner(this, instantiator);
		fallbackCloner = fallbackCloningStrategy;
	}

	public Object cloneObjectUsingClassLoader(final Object original, final ClassLoader targetClassLoader) throws Exception {
		Traversal cloningTraversal = new Traversal() {
			public Object traverse(Object currentObject, Map referenceHistory) throws Exception {
				return ReflectionCloningStrategy.this.clone(currentObject, targetClassLoader, referenceHistory);
			}
		};
		return cyclicReferenceSafeTraverser.performWithoutFollowingCircles(cloningTraversal, original);
	}

	private Object clone(Object original, ClassLoader targetClassLoader, Map cloneHistory) throws Exception {
		if (original == null) return null;
		try {
			return performIntendedCloning(original, targetClassLoader, cloneHistory);
		} catch (Exception e) {
			return performFallbackCloning(original, targetClassLoader);
		}
	}

	private Object performIntendedCloning(Object original, ClassLoader targetClassLoader, Map cloneHistory)
			throws Exception {
		InnerCloner innerCloner = original.getClass().isArray() ? arrayCloner : normalObjectCloner;
		Object clone = original;
		if (decider.shouldCloneObjectItself(original, targetClassLoader))
			clone = innerCloner.instantiateClone(original, targetClassLoader);
		cloneHistory.put(original, clone);
		if (decider.shouldCloneObjectContent(original, targetClassLoader))
			innerCloner.cloneContent(original, clone, targetClassLoader);
		return clone;
	}

	private Object performFallbackCloning(Object original, ClassLoader targetClassLoader) throws Exception {
		return fallbackCloner.cloneObjectUsingClassLoader(original, targetClassLoader);
	}
}
