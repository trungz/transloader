package com.googlecode.transloader.test.fixture;

import com.googlecode.transloader.test.Triangulator;

public class WithPrimitiveFields extends NonCommonJavaObject {
	private boolean booleanField;
	private byte byteField;
	private char charField;
	private short shortField;
	private int intField;
	private long longField;
	private float floatField;
	private double doubleField;

	public WithPrimitiveFields() {
		booleanField = Triangulator.eitherBoolean();
		byteField = Triangulator.anyByte();
		charField = Triangulator.anyChar();
		shortField = Triangulator.anyShort();
		intField = Triangulator.anyInt();
		longField = Triangulator.anyLong();
		floatField = Triangulator.anyFloat();
		doubleField = Triangulator.anyDouble();
	}
}
