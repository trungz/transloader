package com.googlecode.transloader.test.fixture.fields;

import com.googlecode.transloader.test.fixture.NotCommonJavaObject;
import com.googlecode.transloader.test.fixture.NotCommonJavaTypeWithMethods;
import com.googlecode.transloader.test.fixture.NotCommonJavaType;

public class WithMethods extends NotCommonJavaObject implements NotCommonJavaTypeWithMethods {
	private String stringField;

	public String getStringField() {
		return stringField;
	}

	public void setStringField(String stringFieldValue) {
		stringField = stringFieldValue;
	}

	public String concatenate(NotCommonJavaType first, NotCommonJavaType second) {
		return first.toString() + second.toString();
	}
}
