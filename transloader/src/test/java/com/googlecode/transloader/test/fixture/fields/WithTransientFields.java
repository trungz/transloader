package com.googlecode.transloader.test.fixture.fields;

import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.NotCommonJavaObject;

public class WithTransientFields extends NotCommonJavaObject {
    private String notTransientOrFinalString = Triangulate.anyString();
    private transient String TransientNotFinalString = Triangulate.anyString();
    private transient long transientNotFinalLong;
    private Object notTransientOrFinalObject = Triangulate.anyInstanceOf(WithNotCommonJavaFields.class);
    private transient Object transientNotFinalObject = Triangulate.anyInstanceOf(WithNotCommonJavaFields.class);
    private int notTransientOrFinalInt = Triangulate.anyInt();

    public WithTransientFields(long longValue) {
        transientNotFinalLong = longValue;
    }
}