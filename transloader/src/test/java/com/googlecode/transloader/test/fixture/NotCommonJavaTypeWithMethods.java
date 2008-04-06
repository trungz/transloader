package com.googlecode.transloader.test.fixture;

public interface NotCommonJavaTypeWithMethods extends NotCommonJavaType {
	String getStringField();

	void setStringField(String stringFieldValue);

	String concatenate(NotCommonJavaType first, NotCommonJavaType second);
}
