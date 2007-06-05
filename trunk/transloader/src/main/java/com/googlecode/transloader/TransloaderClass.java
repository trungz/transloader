package com.googlecode.transloader;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

public final class TransloaderClass {
	private Class wrappedClass;

	public TransloaderClass(Class classToWrap) {
		wrappedClass = classToWrap;
	}

	public Class getEquivalentFrom(ClassLoader classLoader) throws ClassNotFoundException {
		return wrappedClass.isPrimitive() ? wrappedClass : getClass(wrappedClass.getName(), classLoader);
	}

	public boolean isAssignableTo(String typeName) {
		return classIsAssignableToType(wrappedClass, typeName);
	}

	public static Class getClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
		return ClassUtils.getClass(classLoader, className, false);
	}

	public static Class[] getClasses(String[] classNames, ClassLoader classLoader) throws ClassNotFoundException {
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
