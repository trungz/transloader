package com.googlecode.transloader.clone;

import java.util.Map;

import com.googlecode.transloader.clone.CyclicReferenceSafeTraverser.Traversal;

public final class NewObjectAndContentCloningStrategy implements CloningStrategy {
	private final CyclicReferenceSafeTraverser cyclicReferenceSafeTraverser = new CyclicReferenceSafeTraverser();

	private final CloningDecisionStrategy decider;
	private final ChildCloner arrayCloner;
	private final ChildCloner normalObjectCloner;
	private final CloningStrategy fallbackCloner;

	public NewObjectAndContentCloningStrategy(CloningDecisionStrategy cloningDecisionStrategy,
			InstantiationStrategy instantiator, CloningStrategy fallbackCloningStrategy) {
		decider = cloningDecisionStrategy;
		arrayCloner = new ArrayChildCloner(this);
		normalObjectCloner = new NormalObjectChildCloner(this, instantiator);
		fallbackCloner = fallbackCloningStrategy;
	}

	public Object cloneObjectToClassLoader(final Object original, final ClassLoader targetClassLoader) throws Exception {
		Traversal cloningTraversal = new Traversal() {
			public Object traverse(Object currentObject, Map referenceHistory) throws Exception {
				return NewObjectAndContentCloningStrategy.this.clone(currentObject, targetClassLoader, referenceHistory);
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
		ChildCloner childCloner = original.getClass().isArray() ? arrayCloner : normalObjectCloner;
		Object clone = original;
		if (decider.shouldCloneObjectItself(original, targetClassLoader))
			clone = childCloner.instantiateClone(original, targetClassLoader);
		cloneHistory.put(original, clone);
		if (decider.shouldCloneObjectContent(original, targetClassLoader))
			childCloner.cloneContent(original, clone, targetClassLoader);
		return clone;
	}

	private Object performFallbackCloning(Object original, ClassLoader targetClassLoader) throws Exception {
		return fallbackCloner.cloneObjectToClassLoader(original, targetClassLoader);
	}
}
