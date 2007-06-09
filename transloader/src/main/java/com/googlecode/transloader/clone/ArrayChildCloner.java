package com.googlecode.transloader.clone;

import java.lang.reflect.Array;

import com.googlecode.transloader.ClassWrapper;

final class ArrayChildCloner implements ChildCloner {
	private final CloningStrategy parent;

	ArrayChildCloner(CloningStrategy parentCloner) {
		parent = parentCloner;
	}

	public Object instantiateClone(Object originalArray, ClassLoader targetClassLoader) throws Exception {
		Class originalComponentType = originalArray.getClass().getComponentType();
		Class cloneComponentType = ClassWrapper.getClass(originalComponentType.getName(), targetClassLoader);
		return Array.newInstance(cloneComponentType, Array.getLength(originalArray));
	}

	public void cloneContent(Object original, Object clone, ClassLoader targetClassLoader) throws Exception {
		for (int i = 0; i < Array.getLength(original); i++) {
			Object originalComponent = Array.get(original, i);
			Object cloneComponent = parent.cloneObjectToClassLoader(originalComponent, targetClassLoader);
			Array.set(clone, i, cloneComponent);
		}
	}

}
