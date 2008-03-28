package com.googlecode.transloader.test.fixture.fields;

import com.googlecode.transloader.test.fixture.NonCommonJavaObject;
import com.googlecode.transloader.test.fixture.NonCommonJavaTypeWithMethods;
import com.googlecode.transloader.test.fixture.NonCommonJavaType;

public class WithMethods extends NonCommonJavaObject implements NonCommonJavaTypeWithMethods {
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
