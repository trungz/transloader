package com.googlecode.transloader.test.fixture;

import com.googlecode.transloader.test.StringSerializer;

public class NotCommonJavaObject implements NotCommonJavaType {
    public String toString() {
        return StringSerializer.toString(this);
    }
}
