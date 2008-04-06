package com.googlecode.transloader.test.fixture.hierarchy;

import com.googlecode.transloader.test.fixture.NotCommonJavaObject;

public class Bottom extends Middle {
	private boolean bottomBooleanField;

	public Bottom(NotCommonJavaObject superClassFieldValue, int intFieldValue,
			String fieldValueForWithStringField, boolean booleanFieldValue) {
		super(superClassFieldValue, intFieldValue, fieldValueForWithStringField);
		bottomBooleanField = booleanFieldValue;
	}
}
