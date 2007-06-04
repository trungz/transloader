package com.googlecode.transloader.test.fixture;

public class NonCommonJavaObject implements NonCommonJavaType {
	public String toString() {
		return ClassLoaderAndFieldsStringBuilder.toString(this);
	}
}
