package com.googlecode.transloader.clone;

public class FieldDescription {
	private final String declaringClassName;
	private final String fieldName;

	public FieldDescription(Class declaringClass, String fieldName) {
		this(declaringClass.getName(), fieldName);
	}

	public FieldDescription(String declaringClassName, String fieldName) {
		this.declaringClassName = declaringClassName;
		this.fieldName = fieldName;
	}

	public String getDeclaringClassName() {
		return declaringClassName;
	}

	public String getFieldName() {
		return fieldName;
	}
}
