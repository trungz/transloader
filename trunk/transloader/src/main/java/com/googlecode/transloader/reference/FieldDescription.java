package com.googlecode.transloader.reference;

import com.googlecode.transloader.except.Assert;

import java.lang.reflect.Field;

/**
 * Describes a field by its name, declaring class name and whether or not it is of a primitive type.
 *
 * @author Jeremy Wales
 */
public final class FieldDescription implements ReferenceDescription {
    private final String declaringClassName;
    private final String fieldName;
    private final boolean primitive;

    /**
     * Constructs a <code>FieldDescription</code> derived from the given <code>Field</code>.
     *
     * @param field the field from which this description is derived
     */
    public FieldDescription(Field field) {
        this(((Field) Assert.isNotNull(field)).getDeclaringClass().getName(), field.getName(), field.getType().isPrimitive());
    }

    /**
     * Constructs a <code>FieldDescription</code> with the given declaring class name, field name and
     * primitive indicator.
     *
     * @param declaringClassName the name of the <code>Class</code> that declares the field
     * @param fieldName          the name of the field
     * @param primitive          indicator of whether the reference is to a primitive rather than an {@link Object}
     */
    public FieldDescription(String declaringClassName, String fieldName, boolean primitive) {
        Assert.areNotNull(declaringClassName, fieldName);
        this.declaringClassName = declaringClassName;
        this.fieldName = fieldName;
        this.primitive = primitive;
    }

    /**
     * Retrieves the value assigned to the field matching <code>this</code> description on the given object.
     *
     * @param referer the object from which to retrieve the field
     * @return the value of the field on the <code>referer</code>
     * @throws NoSuchFieldException   if the <code>Field</code> named by <code>this</code> description does not exist on
     *                                the <code>Class</code> named by <code>this</code> description or the
     *                                <code>Class</code> named by <code>this</code> description is not in the
     *                                <code>referer</code>'s <code>Class</code> hierarchy
     * @throws IllegalAccessException if the installed Security Manager does not allow access to the field
     */
    public Object getValueFrom(Object referer) throws NoSuchFieldException, IllegalAccessException {
        Assert.isNotNull(referer);
        return getFieldFrom(referer).get(referer);
    }

    /**
     * Assigns the given value to the field matching <code>this</code> description on the given object.
     *
     * @param referer the object on which to set the field
     * @param value   the value to set (can be <code>null</code>)
     * @throws NoSuchFieldException   if the <code>Field</code> named by <code>this</code> description does not exist on
     *                                the <code>Class</code> named by <code>this</code> description or the
     *                                <code>Class</code> named by <code>this</code> description is not in the
     *                                <code>referer</code>'s <code>Class</code> hierarchy
     * @throws IllegalAccessException if the installed Security Manager does not allow access to the field
     */
    public void setValueIn(Object referer, Object value) throws NoSuchFieldException, IllegalAccessException {
        Assert.isNotNull(referer);
        getFieldFrom(referer).set(referer, value);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isOfPrimitiveType() {
        return primitive;
    }

    /**
     * Retrieves the name of the field.
     * 
     * @return the name of the field within the class.
     */
    public String getName() {
        return fieldName;
    }

    private Field getFieldFrom(Object referer) throws NoSuchFieldException {
        Class declaringClass = getMatchingClassFrom(referer.getClass());
        if (declaringClass == null)
            throw new NoSuchFieldException("No Class named '" + declaringClassName + "' in the hierarchy of '" +
                    referer.getClass().getName() + "' in ClassLoader '" + referer.getClass().getClassLoader() + "'.");
        Field field = declaringClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    private Class getMatchingClassFrom(Class subclass) {
        if (subclass == Object.class) return null;
        return declaringClassName.equals(subclass.getName()) ? subclass : getMatchingClassFrom(subclass.getSuperclass());
    }
}
