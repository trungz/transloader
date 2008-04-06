package com.googlecode.transloader.test.fixture.fields;

import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.NotCommonJavaObject;
import com.googlecode.transloader.test.fixture.NotCommonJavaType;

public class WithNotCommonJavaFields extends NotCommonJavaObject {
    private NotCommonJavaObject object = new WithStringField(Triangulate.anyString());
    private NotCommonJavaType type;

    public WithNotCommonJavaFields(NotCommonJavaType fieldValue) {
        type = fieldValue;
    }
}
