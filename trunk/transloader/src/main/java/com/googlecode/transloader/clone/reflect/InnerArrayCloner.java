package com.googlecode.transloader.clone.reflect;

import java.lang.reflect.Array;

import com.googlecode.transloader.ClassWrapper;
import com.googlecode.transloader.clone.CloningStrategy;

final class InnerArrayCloner implements InnerCloner {
	private final CloningStrategy parent;

	InnerArrayCloner(CloningStrategy outerCloner) {
		parent = outerCloner;
	}

	public Object instantiateCloneOf(Object originalArray, ClassLoader targetClassLoader) throws Exception {
		Class originalComponentType = originalArray.getClass().getComponentType();
		Class cloneComponentType = ClassWrapper.getClassFrom(targetClassLoader, originalComponentType.getName());
		return Array.newInstance(cloneComponentType, Array.getLength(originalArray));
	}

	public void cloneObjectsReferencedBy(Object original, Object clone, ClassLoader targetClassLoader) throws Exception {
		for (int i = 0; i < Array.getLength(original); i++) {
			Object originalComponent = Array.get(original, i);
			Object cloneComponent = parent.cloneObjectUsing(targetClassLoader, originalComponent);
			Array.set(clone, i, cloneComponent);
		}
	}

}
