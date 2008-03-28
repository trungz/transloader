package com.googlecode.transloader.test.fixture.hierarchy;

import com.googlecode.transloader.test.fixture.fields.WithNonCommonJavaFields;
import com.googlecode.transloader.test.fixture.fields.WithStringField;
import com.googlecode.transloader.test.fixture.NonCommonJavaObject;

public class Middle extends WithNonCommonJavaFields {
	private int middleIntField;
	private WithStringField middleFieldWithStringField;

	public Middle(NonCommonJavaObject superClassFieldValue, int intFieldValue,
			String fieldValueForWithStringField) {
		super(superClassFieldValue);
		middleIntField = intFieldValue;
		middleFieldWithStringField = new WithStringField(fieldValueForWithStringField);
	}
}
