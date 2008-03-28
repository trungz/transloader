package com.googlecode.transloader.test.fixture.fields;

import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.NonCommonJavaObject;
import com.googlecode.transloader.test.fixture.NonCommonJavaType;

public class WithNonCommonJavaFields extends NonCommonJavaObject {
	private NonCommonJavaObject object = new WithStringField(Triangulate.anyString());
	private NonCommonJavaType type;

	public WithNonCommonJavaFields(NonCommonJavaType fieldValue) {
		type = fieldValue;
	}
}
