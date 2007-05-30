package com.googlecode.transloader.test.fixture;

import com.googlecode.transloader.test.Triangulator;

public class SerializableWithFinalFields extends Serializable {
	private final String string = Triangulator.anyString();
	private final int intField;

	public SerializableWithFinalFields(Integer integer) {
		intField = integer.intValue();
	}
}
