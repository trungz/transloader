package com.googlecode.transloader.test.fixture;

public class WithMethods extends NonCommonJavaObject {
	private String stringField;

	public String getStringField() {
		return stringField;
	}

	public void setStringField(String stringFieldValue) {
		stringField = stringFieldValue;
	}

	public String concatenate(NonCommonJavaType first, NonCommonJavaType second) {
		return first.toString() + second.toString();
	}
}
