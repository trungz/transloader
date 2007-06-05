package com.googlecode.transloader.clone;

public final class FieldDescription {
	private final String declaringClassName;
	private final String fieldName;
	private final boolean primitive;

	public FieldDescription(Class declaringClass, String fieldName, Class declaredType) {
		this(declaringClass.getName(), fieldName, declaredType.isPrimitive());
	}

	// TODO storing whether or not a field is primitive here feels slightly broken
	public FieldDescription(String declaringClassName, String fieldName, boolean primitive) {
		this.declaringClassName = declaringClassName;
		this.fieldName = fieldName;
		this.primitive = primitive;
	}

	public String getDeclaringClassName() {
		return declaringClassName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public boolean isPrimitive() {
		return primitive;
	}
}
