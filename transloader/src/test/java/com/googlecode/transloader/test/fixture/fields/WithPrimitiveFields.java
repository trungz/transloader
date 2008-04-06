package com.googlecode.transloader.test.fixture.fields;

import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.NotCommonJavaObject;

public class WithPrimitiveFields extends NotCommonJavaObject {
    private boolean booleanField;
    private byte byteField;
    private char charField;
    private short shortField;
    private int intField;
    private long longField;
    private float floatField;
    private double doubleField;

    public WithPrimitiveFields() {
        booleanField = Triangulate.eitherBoolean();
        byteField = Triangulate.anyByte();
        charField = Triangulate.anyChar();
        shortField = Triangulate.anyShort();
        intField = Triangulate.anyInt();
        longField = Triangulate.anyLong();
        floatField = Triangulate.anyFloat();
        doubleField = Triangulate.anyDouble();
    }
}
