package com.googlecode.transloader.test.fixture.fields;

import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.NotCommonJavaObject;
import com.googlecode.transloader.test.fixture.NotCommonJavaType;

public class WithArrayFields extends NotCommonJavaObject {
	private int[] ints = {Triangulate.anyInt(), Triangulate.anyInt()};
	private Object[] objects = {Triangulate.anyString(), new WithPrimitiveFields(), Triangulate.anyString()};
	private boolean[] noBooleans = {};
	private NotCommonJavaObject[] notCommonJavaObjects = {new WithStringField(Triangulate.anyString())};
	private NotCommonJavaType[] notCommonJavaTypes = {new WithPrimitiveFields()};
}
