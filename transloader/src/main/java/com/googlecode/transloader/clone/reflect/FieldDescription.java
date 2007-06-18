package com.googlecode.transloader.clone.reflect;

/**
 * Describes a field by its name, declaring class name and whether or not it is it of primitive type.
 * 
 * @author Jeremy Wales
 */
public final class FieldDescription {
	private final String declaringClassName;
	private final String fieldName;
	private final boolean primitive;

	/**
	 * Constructs a <code>FieldDescription</code> with the given declaring <code>Class</code>, field name and
	 * declared field type.
	 * 
	 * @param declaringClass the <code>Class</code> that declares the field
	 * @param fieldName the name of the field
	 * @param declaredType the declared type of the field
	 */
	public FieldDescription(Class declaringClass, String fieldName, Class declaredType) {
		this(declaringClass.getName(), fieldName, declaredType.isPrimitive());
	}

	// TODO storing whether or not a field is primitive here feels slightly broken
	/**
	 * Constructs a <code>FieldDescription</code> with the given declaring class name, field name and primitive type
	 * indicator.
	 * 
	 * @param declaringClassName the <code>Class</code> that declares the field
	 * @param fieldName the name of the field
	 * @param primitive indicates whether or the declared type of the field is primitive
	 */
	public FieldDescription(String declaringClassName, String fieldName, boolean primitive) {
		this.declaringClassName = declaringClassName;
		this.fieldName = fieldName;
		this.primitive = primitive;
	}

	/**
	 * Gets the name of the <code>Class</code> that declares the field.
	 * 
	 * @return the declaring class name
	 */
	public String getDeclaringClassName() {
		return declaringClassName;
	}

	/**
	 * Gets the name of the field.
	 * 
	 * @return the field name
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Indicates whether or not the declared type of the field is primitive.
	 * 
	 * @return <code>true</code> if the field is primitive
	 */
	public boolean isPrimitive() {
		return primitive;
	}
}
