package com.googlecode.transloader.test.fixture.hierarchy;

import com.googlecode.transloader.test.fixture.fields.WithNotCommonJavaFields;
import com.googlecode.transloader.test.fixture.fields.WithStringField;
import com.googlecode.transloader.test.fixture.NotCommonJavaObject;

public class Middle extends WithNotCommonJavaFields {
	private int middleIntField;
	private WithStringField middleFieldWithStringField;

	public Middle(NotCommonJavaObject superClassFieldValue, int intFieldValue,
			String fieldValueForWithStringField) {
		super(superClassFieldValue);
		middleIntField = intFieldValue;
		middleFieldWithStringField = new WithStringField(fieldValueForWithStringField);
	}
}
