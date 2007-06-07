package com.googlecode.transloader;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

import com.googlecode.transloader.clone.CloningStrategy;

public final class ClassWrapper extends ObjectWrapper {
	private Class wrappedClass;

	public ClassWrapper(Class classToWrap, CloningStrategy cloningStrategy) {
		super(classToWrap, cloningStrategy);
		wrappedClass = (Class) getUnwrappedSelf();
	}

	public Object getEquivalentFrom(ClassLoader classLoader) {
		return wrappedClass.isPrimitive() ? wrappedClass : getClass(wrappedClass.getName(), classLoader);
	}

	public boolean isAssignableTo(String typeName) {
		return classIsAssignableToType(wrappedClass, typeName);
	}

	public static Class getClass(String className, ClassLoader classLoader) {
		try {
			return ClassUtils.getClass(classLoader, className, false);
		} catch (ClassNotFoundException e) {
			throw new TransloaderException(
					"Unable to load Class '" + className + "' from ClassLoader '" + classLoader + "'.", e);
		}
	}

	public static Class[] getClasses(String[] classNames, ClassLoader classLoader) {
		Class[] classes = new Class[classNames.length];
		for (int i = 0; i < classes.length; i++) {
			classes[i] = getClass(classNames[i], classLoader);
		}
		return classes;
	}

	private static boolean classIsAssignableToType(Class rootClass, String typeName) {
		List allClasses = new ArrayList();
		allClasses.add(rootClass);
		allClasses.addAll(ClassUtils.getAllSuperclasses(rootClass));
		allClasses.addAll(ClassUtils.getAllInterfaces(rootClass));
		return ClassUtils.convertClassesToClassNames(allClasses).contains(typeName);
	}
}
