package com.googlecode.transloader.test.fixture;

import com.googlecode.transloader.test.Triangulator;

public class WithArrayFields extends NonCommonJavaObject {
	private int[] ints = {Triangulator.anyInt(), Triangulator.anyInt()};
	private Object[] objects = {Triangulator.anyString(), new WithPrimitiveFields(), Triangulator.anyString()};
	private boolean[] noBooleans = {};
	private NonCommonJavaObject[] nonCommonJavaObjects = {new WithStringField(Triangulator.anyString())};
	private NonCommonJavaType[] nonCommonJavaTypes = {new WithPrimitiveFields()};
}
