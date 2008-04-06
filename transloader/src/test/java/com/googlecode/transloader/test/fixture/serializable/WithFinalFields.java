package com.googlecode.transloader.test.fixture.serializable;

import com.googlecode.transloader.test.Triangulate;

public class WithFinalFields extends Serializable {
    private final String string = Triangulate.anyString();
    private final int intField;

    public WithFinalFields(Integer integer) {
        intField = integer.intValue();
    }
}
