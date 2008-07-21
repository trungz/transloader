package com.googlecode.transloader.test.fixture.fields;

import com.googlecode.transloader.test.Triangulate;

public class WithTransientFinalFields extends WithTransientFields {
    private transient final String transientFinalString = Triangulate.anyString();
    private transient final Object transientFinalObject = Triangulate.anyInstanceOf(WithNotCommonJavaFields.class);
    private transient final int transientFinalInt;


    public WithTransientFinalFields(Integer integer) {
        super(Triangulate.anyLong());
        transientFinalInt = integer.intValue();
    }
}