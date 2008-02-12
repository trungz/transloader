package com.googlecode.transloader.clone.reflect;

import com.googlecode.transloader.ClassWrapper;
import com.googlecode.transloader.clone.CloningStrategy;

final class InnerNormalObjectCloner implements InnerCloner {
	private final InstantiationStrategy instantiator;
	private final CloningStrategy parent;

	InnerNormalObjectCloner(CloningStrategy outerCloner, InstantiationStrategy instantiationStrategy) {
		parent = outerCloner;
		instantiator = instantiationStrategy;
	}

	public Object instantiateCloneOf(Object original, ClassLoader targetClassLoader) throws Exception {
		Class cloneClass = ClassWrapper.getClassFrom(targetClassLoader, original.getClass().getName());
		return instantiator.newInstanceOf(cloneClass);
	}

	public void cloneObjectsReferencedBy(Object original, Object clone, ClassLoader targetClassLoader) throws Exception {
		FieldReflector originalReflector = new FieldReflector(original);
		FieldReflector cloneReflector = new FieldReflector(clone, targetClassLoader);
		FieldDescription[] fieldDescriptions = originalReflector.getAllInstanceFieldDescriptions();
		for (int i = 0; i < fieldDescriptions.length; i++)
            cloneField(fieldDescriptions[i], originalReflector, cloneReflector, targetClassLoader);
	}

	private void cloneField(FieldDescription description, FieldReflector originalReflector,
			FieldReflector cloneReflector, ClassLoader targetClassLoader) throws Exception {
		Object originalFieldValue = originalReflector.getValue(description);
		Object cloneFieldValue = originalFieldValue;
		if (!description.isOfPrimitiveType())
			cloneFieldValue = parent.cloneObjectUsing(targetClassLoader, originalFieldValue);
		cloneReflector.setValue(description, cloneFieldValue);
	}
}
