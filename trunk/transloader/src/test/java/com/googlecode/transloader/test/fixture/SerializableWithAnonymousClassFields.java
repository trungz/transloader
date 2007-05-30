package com.googlecode.transloader.test.fixture;

import com.googlecode.transloader.test.Triangulator;

public class SerializableWithAnonymousClassFields extends SerializableWithFinalFields {
	private java.io.Serializable anonymousClassField = new Serializable() {
		private NonCommonJavaType instanceInitializerField;
		{
			instanceInitializerField = new SerializableWithFinalFields(Triangulator.anyInteger());
		}
		private int enclosingInstanceReliantField = SerializableWithAnonymousClassFields.this.hashCode();
	};

	private java.io.Serializable anonymousClassFromMethodField = getAnonymousClassInstance(Triangulator.anyString());

	public SerializableWithAnonymousClassFields(Integer integer) {
		super(integer);
	}

	private Serializable getAnonymousClassInstance(final String string) {
		return new Serializable() {
			private String setFromExternalVariableField = string;
			private String normalStringField = Triangulator.anyString();
		};
	}
}
