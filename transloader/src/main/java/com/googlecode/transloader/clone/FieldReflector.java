package com.googlecode.transloader.clone;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.googlecode.transloader.ClassWrapper;

public final class FieldReflector {
	public static final List PRIMITIVE_WRAPPERS =
			Arrays.asList(new Class[] {Boolean.class, Byte.class, Character.class, Short.class, Integer.class,
					Long.class, Float.class, Double.class});

	private final Object wrappedObject;
	private final ClassLoader classLoader;

	public FieldReflector(Object objectToWrap) {
		this(objectToWrap, getClassLoader(objectToWrap));
	}

	public FieldReflector(Object objectToWrap, ClassLoader classLoaderToUse) {
		wrappedObject = objectToWrap;
		classLoader = classLoaderToUse;
	}

	public FieldDescription[] getAllInstanceFieldDescriptions() {
		return getAllInstanceFieldDescriptions(wrappedObject.getClass());
	}

	private static FieldDescription[] getAllInstanceFieldDescriptions(Class currentClass) {
		List descriptions = new ArrayList();
		while (currentClass != null) {
			descriptions.addAll(getInstanceFieldDescriptions(currentClass));
			currentClass = currentClass.getSuperclass();
		}
		return (FieldDescription[]) descriptions.toArray(new FieldDescription[descriptions.size()]);
	}

	private static List getInstanceFieldDescriptions(Class currentClass) {
		Field[] fields = currentClass.getDeclaredFields();
		List descriptions = new ArrayList(fields.length);
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (!Modifier.isStatic(field.getModifiers())) {
				descriptions.add(new FieldDescription(currentClass, field.getName(), field.getType()));
			}
		}
		return descriptions;
	}

	public Object getValue(FieldDescription description) throws ClassNotFoundException, NoSuchFieldException,
			IllegalAccessException {
		return getFieldHavingMadeItAccessible(wrappedObject, description, classLoader).get(wrappedObject);
	}

	public void setValue(FieldDescription description, Object fieldValue) throws ClassNotFoundException,
			NoSuchFieldException, IllegalAccessException {
		getFieldHavingMadeItAccessible(wrappedObject, description, classLoader).set(wrappedObject, fieldValue);
	}

	private static Field getFieldHavingMadeItAccessible(Object object, FieldDescription description,
			ClassLoader classLoader) throws ClassNotFoundException, NoSuchFieldException {
		Class declaringClass = ClassWrapper.getClass(description.getDeclaringClassName(), classLoader);
		Field field = declaringClass.getDeclaredField(description.getFieldName());
		field.setAccessible(true);
		return field;
	}

	private static ClassLoader getClassLoader(Object object) {
		ClassLoader classLoader = object.getClass().getClassLoader();
		return classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader;
	}
}
