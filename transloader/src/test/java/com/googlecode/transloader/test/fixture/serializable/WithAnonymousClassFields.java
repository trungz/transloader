package com.googlecode.transloader.test.fixture.serializable;

import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.NonCommonJavaType;

public class WithAnonymousClassFields extends WithFinalFields {
	private java.io.Serializable anonymousClassField = new Serializable() {
		private NonCommonJavaType instanceInitializerField;
		{
			instanceInitializerField = new WithFinalFields(Triangulate.anyInteger());
		}
		private int enclosingInstanceReliantField = WithAnonymousClassFields.this.hashCode();
	};

	private java.io.Serializable anonymousClassFromMethodField = getAnonymousClassInstance(Triangulate.anyString());

	public WithAnonymousClassFields(Integer integer) {
		super(integer);
	}

	private Serializable getAnonymousClassInstance(final String string) {
		return new Serializable() {
			private String setFromExternalVariableField = string;
			private String normalStringField = Triangulate.anyString();
		};
	}
}
