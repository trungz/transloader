package com.googlecode.transloader.clone.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.ClassWrapper;
import com.googlecode.transloader.except.ImpossibleTransloaderException;

/**
 * A reflective wrapper around any object, exposing its fields.
 * 
 * @author Jeremy Wales
 */
public final class FieldReflector {
	/**
	 * The list of primitive wrapper <code>Class</code>es.
	 */
	public static final List PRIMITIVE_WRAPPERS =
			Arrays.asList(new Class[] {Boolean.class, Byte.class, Character.class, Short.class, Integer.class,
					Long.class, Float.class, Double.class});

	private final Object wrappedObject;
	private final ClassLoader classLoader;

	/**
	 * Wraps the given object in a new <code>FieldReflector</code>. Retrieves the <code>ClassLoader</code> of the
	 * wrapped object's <code>Class</code> as the one to use for all class loading.
	 * 
	 * @param objectToWrap the object whose fields you want to expose.
	 */
	public FieldReflector(Object objectToWrap) {
		this(objectToWrap, ClassWrapper.getClassLoaderFrom(objectToWrap));
	}

	/**
	 * Wraps the given object in a new <code>FieldReflector</code>.
	 * 
	 * @param objectToWrap the object whose fields you want to expose.
	 * @param classLoaderToUse the <code>ClassLoader</code> to use for loading <code>Class</code>es from
	 *            {@link FieldDescription#getDeclaringClassName()}
	 */
	public FieldReflector(Object objectToWrap, ClassLoader classLoaderToUse) {
		Assert.areNotNull(objectToWrap, classLoaderToUse);
		wrappedObject = objectToWrap;
		classLoader = classLoaderToUse;
	}

	/**
	 * Gets descriptions of all the instance fields in the entire class hierarchy extended by the class of the wrapped
     * object.
	 * 
	 * @return a description of each instance field in the relevant class hierarchy (<code>final</code>
	 *         fields are not excluded, even though {@link #setValue(FieldDescription, Object)} can be attempted with
	 *         such descriptions)
	 */
	public FieldDescription[] getAllInstanceFieldDescriptions() {
		return getAllInstanceFieldDescriptions(wrappedObject.getClass());
	}

    /**
     * A convenience method combining {@link #getAllInstanceFieldDescriptions()} with
     * {@link #getValue(FieldDescription)} to just get the values.
     *
     * @return the value of each instance field in the wrapped object.
	 * @throws IllegalAccessException if the installed Security Manager does not allow access to a field
     */
    public Object[] getAllInstanceFieldValues() throws IllegalAccessException {
        List values = new ArrayList();
        FieldDescription[] descriptions = getAllInstanceFieldDescriptions(wrappedObject.getClass());
        for (int i = 0; i < descriptions.length; i++) {
            try {
                values.add(getValue(descriptions[i]));
            } catch (ClassNotFoundException e) {
                throw new ImpossibleTransloaderException(e);
            } catch (NoSuchFieldException e) {
                throw new ImpossibleTransloaderException(e);
            }
        }
        return values.toArray();
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

	/**
	 * Gets the value of the field matching the given description on the wrapped object.
	 * 
	 * @param description the description of the field
	 * @return the value of the field from the wrapped object
	 * @throws ClassNotFoundException if the <code>Class</code> named by
	 *             {@link FieldDescription#getDeclaringClassName()} cannot be found by the <code>ClassLoader</code>
	 *             selected by {@link #FieldReflector(Object)} or injected through
	 *             {@link #FieldReflector(Object, ClassLoader)}
	 * @throws NoSuchFieldException if the field named by {@link FieldDescription#getFieldName()} does not exist on the
	 *             <code>Class</code> named by {@link FieldDescription#getDeclaringClassName()} or the
	 *             <code>Class</code> named by {@link FieldDescription#getDeclaringClassName()} is not in the wrapped
	 *             object's class hierarchy
	 * @throws IllegalAccessException if the installed Security Manager does not allow access to the field
	 */
	public Object getValue(FieldDescription description) throws ClassNotFoundException, NoSuchFieldException,
			IllegalAccessException {
		Assert.isNotNull(description);
		return getFieldHavingMadeItAccessible(description, classLoader).get(wrappedObject);
	}

	/**
	 * Sets the value of the field matching the given description on the wrapped object.
	 * 
	 * @param description the description of the field to set
	 * @param fieldValue the value to set
	 * @throws ClassNotFoundException if the <code>Class</code> named by
	 *             {@link FieldDescription#getDeclaringClassName()} cannot be found by the <code>ClassLoader</code>
	 *             selected by {@link #FieldReflector(Object)} or injected through
	 *             {@link #FieldReflector(Object, ClassLoader)}
	 * @throws NoSuchFieldException if the field named by {@link FieldDescription#getFieldName()} does not exist on the
	 *             <code>Class</code> named by {@link FieldDescription#getDeclaringClassName()} or the
	 *             <code>Class</code> named by {@link FieldDescription#getDeclaringClassName()} is not in the wrapped
	 *             object's class hierarchy
	 * @throws IllegalAccessException if the installed Security Manager does not allow access to the field
	 */
	public void setValue(FieldDescription description, Object fieldValue) throws ClassNotFoundException,
			NoSuchFieldException, IllegalAccessException {
		Assert.areNotNull(description, fieldValue);
		getFieldHavingMadeItAccessible(description, classLoader).set(wrappedObject, fieldValue);
	}

	private static Field getFieldHavingMadeItAccessible(FieldDescription description, ClassLoader classLoader)
            throws ClassNotFoundException, NoSuchFieldException {
		Class declaringClass = ClassWrapper.getClassFrom(classLoader, description.getDeclaringClassName());
		Field field = declaringClass.getDeclaredField(description.getFieldName());
		field.setAccessible(true);
		return field;
	}
}
