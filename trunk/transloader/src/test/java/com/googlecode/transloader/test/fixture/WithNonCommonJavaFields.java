package com.googlecode.transloader.test.fixture;

import com.googlecode.transloader.test.Triangulator;

public class WithNonCommonJavaFields extends NonCommonJavaObject {
	private NonCommonJavaObject object = new WithStringField(Triangulator.anyString());
	private NonCommonJavaType type;

	public WithNonCommonJavaFields(NonCommonJavaType fieldValue) {
		type = fieldValue;
	}
}
